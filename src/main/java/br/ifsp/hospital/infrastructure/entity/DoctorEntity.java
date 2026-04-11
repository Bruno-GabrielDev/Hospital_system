package br.ifsp.hospital.infrastructure.entity;

import br.ifsp.hospital.domain.model.Doctor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "doctor")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorEntity {

    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialty;

    @Column(nullable = false, unique = true)
    private String license;

    public static DoctorEntity from(Doctor d) {
        return DoctorEntity.builder()
                .id(d.getId()).name(d.getName())
                .specialty(d.getSpecialty()).license(d.getLicense())
                .build();
    }

    public Doctor toDomain() {
        return Doctor.restore(id, name, specialty, license);
    }
}
