package br.ifsp.hospital.domain.repository;

import br.ifsp.hospital.domain.model.AppointmentStatus;
import br.ifsp.hospital.domain.model.MedicalAppointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta de saída do domínio para persistência de atendimentos.
 * Implementada na camada de infraestrutura.
 */
public interface AppointmentRepository {
    MedicalAppointment save(MedicalAppointment appointment);
    Optional<MedicalAppointment> findById(UUID id);
    List<MedicalAppointment> findAll();
    List<MedicalAppointment> findByPatientId(UUID patientId);
    List<MedicalAppointment> findByDoctorId(UUID doctorId);

    /** Verifica conflito de horário para o médico (#62, #63, #53) */
    List<MedicalAppointment> findByDoctorIdAndScheduledAt(UUID doctorId, LocalDateTime scheduledAt);

    List<MedicalAppointment> findByPatientIdAndStatus(UUID patientId, AppointmentStatus status);
}
