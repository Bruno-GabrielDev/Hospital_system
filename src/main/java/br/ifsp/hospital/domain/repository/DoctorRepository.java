package br.ifsp.hospital.domain.repository;

import br.ifsp.hospital.domain.model.Doctor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository {
    Doctor save(Doctor doctor);
    Optional<Doctor> findById(UUID id);
    Optional<Doctor> findByLicense(String license);
    List<Doctor> findAll();
}
