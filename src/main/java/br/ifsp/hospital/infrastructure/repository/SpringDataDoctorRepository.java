package br.ifsp.hospital.infrastructure.repository;

import br.ifsp.hospital.infrastructure.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataDoctorRepository extends JpaRepository<DoctorEntity, UUID> {
    Optional<DoctorEntity> findByLicense(String license);
}
