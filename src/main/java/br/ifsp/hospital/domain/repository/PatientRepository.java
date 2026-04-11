package br.ifsp.hospital.domain.repository;

import br.ifsp.hospital.domain.model.Patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository {
    Patient save(Patient patient);
    Optional<Patient> findById(UUID id);
    Optional<Patient> findByDocument(String document);
    List<Patient> findAll();
}
