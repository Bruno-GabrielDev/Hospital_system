package br.ifsp.hospital.infrastructure.entity;

import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Patient;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

/**
 * Entidade JPA: representa a tabela "patient".
 * Separada do domínio — sem regras de negócio.
 */
@Entity
@Table(name = "patient")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientEntity {

    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String document;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsuranceType insuranceType;

    public static PatientEntity from(Patient p) {
        return PatientEntity.builder()
                .id(p.getId()).name(p.getName())
                .document(p.getDocument()).insuranceType(p.getInsuranceType())
                .build();
    }

    public Patient toDomain() {
        return Patient.restore(id, name, document, insuranceType);
    }
}
