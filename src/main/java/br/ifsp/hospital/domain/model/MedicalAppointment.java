package br.ifsp.hospital.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MedicalAppointment {

    public static final int MAX_PROCEDURES       = 50;
    public static final int MAX_RESCHEDULES      = 3;
    public static final int MIN_RESCHEDULE_HOURS = 6;

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
        return null;
    }

    public void reschedule(LocalDateTime newScheduledAt) {}

    public void addProcedure(AppointmentProcedure procedure) {
        this.procedures.add(procedure);
    }

    public void close() {
        this.status = AppointmentStatus.CLOSED;
    }

    public void markAsBilled() {
        this.status = AppointmentStatus.BILLED;
    }

    public void cancel() {
        if (this.status == AppointmentStatus.CLOSED) {
            throw new IllegalStateException("Closed appointment cannot be canceled.");
        }
        if (this.status == AppointmentStatus.BILLED) {
            this.refund();
        }

        this.procedures.forEach(AppointmentProcedure::cancel);
        this.status = AppointmentStatus.CANCELED;
    }

    public Money calculateGrossTotal() {
        return null;
    }

    private void refund() {
        this.refunded = true;
    }

    public UUID getId()                           { return null; }
    public Patient getPatient()                   { return null; }
    public Doctor getDoctor()                     { return null; }
    public AppointmentStatus getStatus(){
        return this.status;
    }
    public LocalDateTime getCreatedAt()           { return null; }
    public LocalDateTime getScheduledAt()         { return null; }
    public int getRescheduleCount()               { return 0; }
    public List<AppointmentProcedure> getProcedures() {
        return this.procedures;
    }

    public boolean isRefunded() {
        return this.refunded;
    }

    @Override public boolean equals(Object o) { return false; }
    @Override public int hashCode()            { return 0; }
}
