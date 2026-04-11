package br.ifsp.hospital.infrastructure.repository;

import br.ifsp.hospital.domain.model.Doctor;
import br.ifsp.hospital.domain.repository.DoctorRepository;
import br.ifsp.hospital.infrastructure.entity.DoctorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DoctorRepositoryAdapter implements DoctorRepository {

    private final SpringDataDoctorRepository jpa;

    @Override
    public Doctor save(Doctor doctor) {
        return jpa.save(DoctorEntity.from(doctor)).toDomain();
    }

    @Override
    public Optional<Doctor> findById(UUID id) {
        return jpa.findById(id).map(DoctorEntity::toDomain);
    }

    @Override
    public Optional<Doctor> findByLicense(String license) {
        return jpa.findByLicense(license).map(DoctorEntity::toDomain);
    }

    @Override
    public List<Doctor> findAll() {
        return jpa.findAll().stream().map(DoctorEntity::toDomain).toList();
    }
}
