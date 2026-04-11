package br.ifsp.hospital.infrastructure.repository;

import br.ifsp.hospital.domain.model.Patient;
import br.ifsp.hospital.domain.repository.PatientRepository;
import br.ifsp.hospital.infrastructure.entity.PatientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador: implementa a interface de domínio PatientRepository
 * delegando para o SpringDataPatientRepository (JPA).
 */
@Repository
@RequiredArgsConstructor
public class PatientRepositoryAdapter implements PatientRepository {

    private final SpringDataPatientRepository jpa;

    @Override
    public Patient save(Patient patient) {
        return jpa.save(PatientEntity.from(patient)).toDomain();
    }

    @Override
    public Optional<Patient> findById(UUID id) {
        return jpa.findById(id).map(PatientEntity::toDomain);
    }

    @Override
    public Optional<Patient> findByDocument(String document) {
        return jpa.findByDocument(document).map(PatientEntity::toDomain);
    }

    @Override
    public List<Patient> findAll() {
        return jpa.findAll().stream().map(PatientEntity::toDomain).toList();
    }
}
