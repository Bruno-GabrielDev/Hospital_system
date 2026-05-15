package br.ifsp.hospital.mutation;

import br.ifsp.hospital.annotation.Mutation;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Money;
import br.ifsp.hospital.domain.model.Procedure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@Mutation
@DisplayName("Mutation – Procedure")
public class ProcedureMutationTest {

    @Test
    @DisplayName("Deve gerar hashCodes diferentes para instâncias distintas (IDs diferentes)")
    void hashCodeDifferentProceduresShouldReturnDifferentHashCodes() {
        Money defaultCost = new Money(new BigDecimal("150.00"));

        Procedure procedure1 = Procedure.of("Consulta de Rotina", defaultCost);
        Procedure procedure2 = Procedure.of("Consulta de Rotina", defaultCost);

        assertThat(procedure1.hashCode()).isNotEqualTo(procedure2.hashCode());
    }
}