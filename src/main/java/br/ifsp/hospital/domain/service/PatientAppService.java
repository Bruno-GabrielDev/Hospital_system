package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Patient;
import br.ifsp.hospital.domain.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientAppService {

    private final PatientRepository patientRepository;

    public PatientAppService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient create(String name, String cpf, InsuranceType insuranceType) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }

        Patient patient = Patient.of(name, cpf, insuranceType);
        return patientRepository.save(patient);
    }


    public Patient findById(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com ID: " + id));
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }
}