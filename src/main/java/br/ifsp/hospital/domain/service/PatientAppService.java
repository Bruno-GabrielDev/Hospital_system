package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Patient;
import br.ifsp.hospital.domain.repository.PatientRepository;
import br.ifsp.hospital.exception.EntityAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientAppService {

    private final PatientRepository patientRepository;

    public Patient create(String name, String document, InsuranceType insuranceType) {
        patientRepository.findByDocument(document).ifPresent(p -> {
            throw new EntityAlreadyExistsException("Paciente já cadastrado com documento: " + document);
        });
        return patientRepository.save(Patient.of(name, document, insuranceType));
    }

    public Patient findById(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado: " + id));
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }
}
