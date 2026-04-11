package br.ifsp.hospital.infrastructure.repository;

import br.ifsp.hospital.infrastructure.entity.ProcedureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataProcedureRepository extends JpaRepository<ProcedureEntity, UUID> {
}
