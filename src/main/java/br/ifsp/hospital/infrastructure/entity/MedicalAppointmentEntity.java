package br.ifsp.hospital.infrastructure.entity;

import br.ifsp.hospital.domain.model.AppointmentStatus;
import br.ifsp.hospital.domain.model.MedicalAppointment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "medical_appointment")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalAppointmentEntity {

    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity doctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Column(nullable = false)
    private int rescheduleCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    @Builder.Default
    private List<AppointmentProcedureEntity> procedures = new ArrayList<>();

    public MedicalAppointment toDomain() {
        var domainProcs = procedures.stream()
                .map(AppointmentProcedureEntity::toDomain)
                .toList();
        return MedicalAppointment.restore(
                id, patient.toDomain(), doctor.toDomain(),
                scheduledAt, createdAt, status, rescheduleCount, domainProcs
        );
    }
}
