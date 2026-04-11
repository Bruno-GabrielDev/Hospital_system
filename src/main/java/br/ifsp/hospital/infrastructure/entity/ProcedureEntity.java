package br.ifsp.hospital.infrastructure.entity;

import br.ifsp.hospital.domain.model.Money;
import br.ifsp.hospital.domain.model.Procedure;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "procedure")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedureEntity {

    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "cost_amount", nullable = false)
    private BigDecimal costAmount;

    public static ProcedureEntity from(Procedure p) {
        return ProcedureEntity.builder()
                .id(p.getId()).name(p.getName())
                .costAmount(p.getCost().getAmount())
                .build();
    }

    public Procedure toDomain() {
        return Procedure.restore(id, name, new Money(costAmount));
    }
}
