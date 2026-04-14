package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.Money;
import br.ifsp.hospital.domain.model.Procedure;
import br.ifsp.hospital.domain.repository.ProcedureRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProcedureAppService {

    private final ProcedureRepository procedureRepository;

    public ProcedureAppService(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    public Procedure create(String name, Money price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do procedimento é obrigatório");
        }
        if (price == null) {
            throw new IllegalArgumentException("Preço é obrigatório");
        }

        Procedure procedure = Procedure.of(name, price);
        return procedureRepository.save(procedure);
    }

    public Procedure findById(UUID id) {
        return procedureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Procedimento não encontrado com ID: " + id));
    }

    public List<Procedure> findAll() {
        return procedureRepository.findAll();
    }
}