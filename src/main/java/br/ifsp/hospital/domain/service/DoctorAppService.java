package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.Doctor;
import br.ifsp.hospital.domain.repository.DoctorRepository;
import br.ifsp.hospital.exception.EntityAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorAppService {

    private final DoctorRepository doctorRepository;

    public Doctor create(String name, String specialty, String license) {
        doctorRepository.findByLicense(license).ifPresent(d -> {
            throw new EntityAlreadyExistsException("Médico já cadastrado com CRM: " + license);
        });
        return doctorRepository.save(Doctor.of(name, specialty, license));
    }

    public Doctor findById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado: " + id));
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }
}
