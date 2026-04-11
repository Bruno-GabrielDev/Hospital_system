package br.ifsp.hospital.infrastructure.repository;

import br.ifsp.hospital.domain.model.Procedure;
import br.ifsp.hospital.domain.repository.ProcedureRepository;
import br.ifsp.hospital.infrastructure.entity.ProcedureEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProcedureRepositoryAdapter implements ProcedureRepository {

    private final SpringDataProcedureRepository jpa;

    @Override
    public Procedure save(Procedure procedure) {
        return jpa.save(ProcedureEntity.from(procedure)).toDomain();
    }

    @Override
    public Optional<Procedure> findById(UUID id) {
        return jpa.findById(id).map(ProcedureEntity::toDomain);
    }

    @Override
    public List<Procedure> findAll() {
        return jpa.findAll().stream().map(ProcedureEntity::toDomain).toList();
    }
}
