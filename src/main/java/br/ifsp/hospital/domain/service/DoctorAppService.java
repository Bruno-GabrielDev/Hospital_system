package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.Doctor;
import br.ifsp.hospital.domain.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorAppService {

    private final DoctorRepository doctorRepository;

    public DoctorAppService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor create(String name, String specialty, String crm) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do médico é obrigatório");
        }
        if (specialty == null || specialty.trim().isEmpty()) {
            throw new IllegalArgumentException("Especialidade é obrigatória");
        }
        if (crm == null || crm.trim().isEmpty()) {
            throw new IllegalArgumentException("CRM é obrigatório");
        }

        Doctor doctor = Doctor.of(name, specialty, crm);
        return doctorRepository.save(doctor);
    }

    public Doctor findById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado com ID: " + id));
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }
}