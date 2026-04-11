package br.ifsp.hospital.infrastructure.repository;

import br.ifsp.hospital.infrastructure.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataPatientRepository extends JpaRepository<PatientEntity, UUID> {
    Optional<PatientEntity> findByDocument(String document);
}
