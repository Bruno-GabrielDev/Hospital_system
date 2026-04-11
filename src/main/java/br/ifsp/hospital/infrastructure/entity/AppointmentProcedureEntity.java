package br.ifsp.hospital.infrastructure.entity;

import br.ifsp.hospital.domain.model.AppointmentProcedure;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "appointment_procedure")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentProcedureEntity {

    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_id", nullable = false)
    private ProcedureEntity procedure;

    @Column(nullable = false)
    private int quantity;

    public static AppointmentProcedureEntity from(AppointmentProcedure ap, ProcedureEntity procEntity) {
        return AppointmentProcedureEntity.builder()
                .id(ap.getId())
                .procedure(procEntity)
                .quantity(ap.getQuantity())
                .build();
    }

    public AppointmentProcedure toDomain() {
        return AppointmentProcedure.restore(id, procedure.toDomain(), quantity);
    }
}
