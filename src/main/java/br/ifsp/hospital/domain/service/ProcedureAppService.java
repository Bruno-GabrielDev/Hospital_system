package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.Money;
import br.ifsp.hospital.domain.model.Procedure;
import br.ifsp.hospital.domain.repository.ProcedureRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcedureAppService {

    private final ProcedureRepository procedureRepository;

    public Procedure create(String name, BigDecimal cost) {
        return procedureRepository.save(Procedure.of(name, new Money(cost)));
    }

    public Procedure findById(UUID id) {
        return procedureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Procedimento não encontrado: " + id));
    }

    public List<Procedure> findAll() {
        return procedureRepository.findAll();
    }
}
