package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.*;
import br.ifsp.hospital.domain.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ProcedureRepository procedureRepository;
    private final InsuranceCoverageService insuranceCoverageService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              ProcedureRepository procedureRepository,
                              InsuranceCoverageService insuranceCoverageService) {
        this.appointmentRepository    = appointmentRepository;
        this.patientRepository        = patientRepository;
        this.doctorRepository         = doctorRepository;
        this.procedureRepository      = procedureRepository;
        this.insuranceCoverageService = insuranceCoverageService;
    }

    public MedicalAppointment create(UUID patientId, UUID doctorId, LocalDateTime scheduledAt) {
        return null;
    }

    public MedicalAppointment addProcedure(UUID appointmentId, UUID procedureId, int quantity) {
        return null;
    }

    public MedicalAppointment close(UUID appointmentId) {
        return null;
    }

    public MedicalAppointment markAsBilled(UUID appointmentId) {
        return null;
    }

    public Money calculateBill(UUID appointmentId) {
        return null;
    }

    public MedicalAppointment reschedule(UUID appointmentId, LocalDateTime newScheduledAt) {
        return null;
    }

    public MedicalAppointment findById(UUID id) {
        return null;
    }

    public List<MedicalAppointment> findAll()                     { return null; }
    public List<MedicalAppointment> findByPatient(UUID patientId) { return null; }
    public List<MedicalAppointment> findByDoctor(UUID doctorId)   { return null; }
}
