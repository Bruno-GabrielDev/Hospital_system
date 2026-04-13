package br.ifsp.hospital.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MedicalAppointment {

    public static final int MAX_PROCEDURES       = 50;
    public static final int MAX_RESCHEDULES      = 3;
    public static final int MIN_RESCHEDULE_HOURS = 6;
    public static final int MIN_CANCEL_HOURS     = 3;

    private UUID id;
    private Patient patient;
    private Doctor doctor;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime scheduledAt;
    private int rescheduleCount;
    private List<AppointmentProcedure> procedures;
    private boolean refunded;

    private MedicalAppointment(Patient patient, Doctor doctor, LocalDateTime scheduledAt) {
        this.patient = patient;
        this.doctor = doctor;
        this.scheduledAt = scheduledAt;

        this.status = AppointmentStatus.OPEN;
        this.procedures = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public static MedicalAppointment of(Patient patient, Doctor doctor, LocalDateTime scheduledAt) {
        return new MedicalAppointment(patient, doctor, scheduledAt);
    }

    public static MedicalAppointment restore(UUID id, Patient patient, Doctor doctor,
                                             LocalDateTime scheduledAt, LocalDateTime createdAt,
                                             AppointmentStatus status, int rescheduleCount,
                                             List<AppointmentProcedure> procedures) {
        MedicalAppointment appointment = new MedicalAppointment(patient, doctor, scheduledAt);
        appointment.id              = id;
        appointment.createdAt       = createdAt;
        appointment.status          = status;
        appointment.rescheduleCount = rescheduleCount;
        appointment.procedures      = new ArrayList<>(procedures);
        return appointment;
    }

    public void reschedule(LocalDateTime newScheduledAt, DoctorScheduleValidator validator) {
        validateReschedule(newScheduledAt, validator);

        this.scheduledAt = newScheduledAt;
        this.rescheduleCount++;
    }

    public void addProcedure(AppointmentProcedure procedure) {
        if (status != AppointmentStatus.OPEN)
            throw new IllegalStateException("Não é possível adicionar procedimentos: atendimento não está aberto.");
        if (this.procedures.size() >= MAX_PROCEDURES) {
            throw new IllegalStateException("Cannot add more than " + MAX_PROCEDURES + " procedures to an appointment.");
        }

        this.procedures.add(procedure);
    }

    public void close() {
        this.status = AppointmentStatus.CLOSED;
    }

    public void markAsBilled() {
        this.status = AppointmentStatus.BILLED;
    }

    public void cancel(String reason) {
        validateCancellation(reason);

        if (this.status == AppointmentStatus.BILLED) {
            processRefunds();
        }

        this.procedures.forEach(AppointmentProcedure::cancel);
        this.status = AppointmentStatus.CANCELED;
    }

    public Money calculateGrossTotal() {
        if (procedures.isEmpty())
            throw new IllegalStateException("Atendimento sem procedimentos: não é possível calcular a conta.");

        return procedures.stream()
                .map(AppointmentProcedure::getTotalCost)
                .reduce(new Money(BigDecimal.ZERO), Money::add);
    }

    private void validateCancellation(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Cancellation reason is required.");
        }
        if (this.status == AppointmentStatus.CANCELED) {
            throw new IllegalStateException("Cannot cancel an already canceled appointment.");
        }
        if (this.status == AppointmentStatus.CLOSED) {
            throw new IllegalStateException("Closed appointment cannot be canceled.");
        }
        if (LocalDateTime.now().plusHours(MIN_CANCEL_HOURS).isAfter(this.scheduledAt)) {
            throw new IllegalStateException("Cannot cancel an appointment with less than " + MIN_CANCEL_HOURS + " hours in advance.");
        }
    }

    private void validateReschedule(LocalDateTime newScheduledAt, DoctorScheduleValidator validator) {
        if (newScheduledAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment cannot be rescheduled to a past date.");
        }
        if (this.rescheduleCount >= MAX_RESCHEDULES) {
            throw new IllegalStateException("Exceeded maximum number of reschedules (" + MAX_RESCHEDULES + ").");
        }
        if (!validator.isWithinWorkingHours(this.doctor, newScheduledAt)) {
            throw new IllegalStateException("Cannot reschedule to outside of doctor's working hours.");
        }
        if (!validator.isAvailable(this.doctor, newScheduledAt)) {
            throw new IllegalStateException("Doctor is not available at this time.");
        }
    }

    private void processRefunds() {
        this.refund();
        this.procedures.forEach(AppointmentProcedure::refund);
    }

    private void refund() {
        this.refunded = true;
    }

    public UUID getId() { return id; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public AppointmentStatus getStatus(){
        return this.status;
    }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public int getRescheduleCount() { return rescheduleCount; }
    public List<AppointmentProcedure> getProcedures() {
        return this.procedures;
    }

    public boolean isRefunded() {
        return this.refunded;
    }

    @Override public boolean equals(Object o) { return false; }
    @Override public int hashCode()            { return 0; }
}
