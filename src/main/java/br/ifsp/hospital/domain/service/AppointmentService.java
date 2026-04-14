package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.*;
import br.ifsp.hospital.domain.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ProcedureRepository procedureRepository;
    private final InsuranceCoverageService insuranceCoverageService;
    private final DoctorScheduleValidator doctorScheduleValidator;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              ProcedureRepository procedureRepository,
                              InsuranceCoverageService insuranceCoverageService,
                              DoctorScheduleValidator doctorScheduleValidator) {
        this.appointmentRepository    = appointmentRepository;
        this.patientRepository        = patientRepository;
        this.doctorRepository         = doctorRepository;
        this.procedureRepository      = procedureRepository;
        this.insuranceCoverageService = insuranceCoverageService;
        this.doctorScheduleValidator = doctorScheduleValidator;
    }

    public MedicalAppointment create(UUID patientId, UUID doctorId, LocalDateTime scheduledAt) {
        if (scheduledAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data do atendimento não pode estar no passado.");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado: " + patientId));

        List<MedicalAppointment> openAppointments = appointmentRepository
                .findByPatientIdAndStatus(patientId, AppointmentStatus.OPEN);

        if (!openAppointments.isEmpty()) {
            throw new IllegalStateException("Paciente já possui um atendimento em aberto.");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado: " + doctorId));

        MedicalAppointment appointment = MedicalAppointment.of(patient, doctor, scheduledAt);

        return appointmentRepository.save(appointment);
    }

    public MedicalAppointment addProcedure(UUID appointmentId, UUID procedureId, int quantity) {
        if (procedureId == null) {
            throw new IllegalArgumentException("ID do procedimento é obrigatório.");
        }

        Procedure procedure = procedureRepository.findById(procedureId)
                .orElseThrow(() -> new EntityNotFoundException("Procedimento não encontrado: " + procedureId));

        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento não encontrado: " + appointmentId));

        AppointmentProcedure appointmentProcedure = AppointmentProcedure.of(procedure, quantity);
        appointment.addProcedure(appointmentProcedure);

        return appointmentRepository.save(appointment);
    }

    public MedicalAppointment close(UUID appointmentId) {
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento não encontrado: " + appointmentId));
        appointment.close();
        return appointmentRepository.save(appointment);
    }

    public MedicalAppointment markAsBilled(UUID appointmentId) {
        return null;
    }

    public BillDetail calculateBill(UUID appointmentId) {
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Atendimento não encontrado: " + appointmentId));

        InsuranceType insuranceType  = appointment.getPatient().getInsuranceType();
        Money grossTotal             = appointment.calculateGrossTotal();
        Money patientAmount          = insuranceCoverageService.applyCoverage(grossTotal, insuranceType);
        Money insuranceAmount        = insuranceCoverageService.getCoveredAmount(grossTotal, insuranceType);

        return new BillDetail(appointmentId, grossTotal, patientAmount, insuranceAmount, insuranceType);
    }

    public MedicalAppointment reschedule(UUID appointmentId, LocalDateTime newScheduledAt) {
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento não encontrado: " + appointmentId));

        appointment.reschedule(newScheduledAt, doctorScheduleValidator);

        return appointmentRepository.save(appointment);
    }

    public MedicalAppointment cancel(UUID appointmentId, String reason) {
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento não encontrado: " + appointmentId));

        appointment.cancel(reason);

        return appointmentRepository.save(appointment);
    }

    public MedicalAppointment findById(UUID id) {
        return null;
    }

    public List<MedicalAppointment> findAll()                     { return null; }
    public List<MedicalAppointment> findByPatient(UUID patientId) { return null; }
    public List<MedicalAppointment> findByDoctor(UUID doctorId)   { return null; }

    public List<Invoice> generateInvoices(UUID appointmentId) {
        BillDetail bill = this.calculateBill(appointmentId);

        List<Invoice> invoices = new ArrayList<>();

        invoices.add(new Invoice(appointmentId, InvoiceType.PATIENT, bill.patientAmount()));

        if (bill.insuranceAmount().getAmount().compareTo(BigDecimal.ZERO) > 0) {
            invoices.add(new Invoice(appointmentId, InvoiceType.INSURANCE, bill.insuranceAmount()));
        }

        return invoices;
    }
}
