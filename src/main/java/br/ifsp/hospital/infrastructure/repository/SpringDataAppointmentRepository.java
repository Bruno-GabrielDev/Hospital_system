package br.ifsp.hospital.infrastructure.repository;

import br.ifsp.hospital.domain.model.AppointmentStatus;
import br.ifsp.hospital.infrastructure.entity.MedicalAppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataAppointmentRepository extends JpaRepository<MedicalAppointmentEntity, UUID> {

    List<MedicalAppointmentEntity> findByPatientId(UUID patientId);

    List<MedicalAppointmentEntity> findByDoctorId(UUID doctorId);

    List<MedicalAppointmentEntity> findByDoctorIdAndScheduledAt(UUID doctorId, LocalDateTime scheduledAt);


    @Query("SELECT a FROM MedicalAppointmentEntity a WHERE a.patient.id = :patientId AND a.status = :status")
    List<MedicalAppointmentEntity> findByPatientIdAndStatus(UUID patientId, AppointmentStatus status);
}
