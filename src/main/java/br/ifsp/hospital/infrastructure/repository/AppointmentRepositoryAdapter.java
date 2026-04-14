package br.ifsp.hospital.infrastructure.repository;

import br.ifsp.hospital.domain.model.*;
import br.ifsp.hospital.domain.repository.AppointmentRepository;
import br.ifsp.hospital.infrastructure.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AppointmentRepositoryAdapter implements AppointmentRepository {

    private final SpringDataAppointmentRepository jpa;
    private final SpringDataPatientRepository     patientJpa;
    private final SpringDataDoctorRepository      doctorJpa;
    private final SpringDataProcedureRepository   procedureJpa;

    @Override
    public MedicalAppointment save(MedicalAppointment appt) {
        PatientEntity  patientEntity  = patientJpa.findById(appt.getPatient().getId())
                .orElseGet(() -> patientJpa.save(PatientEntity.from(appt.getPatient())));
        DoctorEntity   doctorEntity   = doctorJpa.findById(appt.getDoctor().getId())
                .orElseGet(() -> doctorJpa.save(DoctorEntity.from(appt.getDoctor())));

        // Reconstrói a entidade JPA preservando os procedimentos convertidos
        List<AppointmentProcedureEntity> procEntities = appt.getProcedures().stream()
                .map(ap -> {
                    ProcedureEntity pe = procedureJpa.findById(ap.getProcedure().getId())
                            .orElseGet(() -> procedureJpa.save(ProcedureEntity.from(ap.getProcedure())));
                    return AppointmentProcedureEntity.from(ap, pe);
                }).toList();

        MedicalAppointmentEntity entity = MedicalAppointmentEntity.builder()
                .id(appt.getId())
                .patient(patientEntity)
                .doctor(doctorEntity)
                .status(appt.getStatus())
                .createdAt(appt.getCreatedAt())
                .scheduledAt(appt.getScheduledAt())
                .rescheduleCount(appt.getRescheduleCount())
                .procedures(new java.util.ArrayList<>(procEntities))
                .build();

        return jpa.save(entity).toDomain();
    }

    @Override
    public Optional<MedicalAppointment> findById(UUID id) {
        return jpa.findById(id).map(MedicalAppointmentEntity::toDomain);
    }

    @Override
    public List<MedicalAppointment> findAll() {
        return jpa.findAll().stream().map(MedicalAppointmentEntity::toDomain).toList();
    }

    @Override
    public List<MedicalAppointment> findByPatientId(UUID patientId) {
        return jpa.findByPatientId(patientId).stream()
                .map(MedicalAppointmentEntity::toDomain).toList();
    }

    @Override
    public List<MedicalAppointment> findByDoctorId(UUID doctorId) {
        return jpa.findByDoctorId(doctorId).stream()
                .map(MedicalAppointmentEntity::toDomain).toList();
    }

    @Override
    public List<MedicalAppointment> findByDoctorIdAndScheduledAt(UUID doctorId, LocalDateTime scheduledAt) {
        return jpa.findByDoctorIdAndScheduledAt(doctorId, scheduledAt).stream()
                .map(MedicalAppointmentEntity::toDomain).toList();
    }

    @Override
    public List<MedicalAppointment> findByPatientIdAndStatus(UUID patientId, AppointmentStatus status) {
        return jpa.findByPatientIdAndStatus(patientId, status).stream()
                .map(MedicalAppointmentEntity::toDomain).toList();
    }
}
