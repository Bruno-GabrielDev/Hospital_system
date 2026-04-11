package br.ifsp.hospital.domain.repository;

import br.ifsp.hospital.domain.model.Procedure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProcedureRepository {
    Procedure save(Procedure procedure);
    Optional<Procedure> findById(UUID id);
    List<Procedure> findAll();
}
