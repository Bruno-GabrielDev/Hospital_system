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
        Money defaultCost = new Money(new java.math.BigDecimal("150.00"));

        Procedure procedure1 = Procedure.of("Consulta de Rotina", defaultCost);
        Procedure procedure2 = Procedure.of("Consulta de Rotina", defaultCost);

        assertThat(procedure1.hashCode()).isNotEqualTo(procedure2.hashCode());
    }

    @Test
    @DisplayName("Deve ter período de carência de 0 dias por padrão (#98)")
    void shouldHaveDefaultGracePeriodOfZeroDays() {
        Procedure procedure = Procedure.of("Consulta", new Money(new java.math.BigDecimal("100.00")));
        assertThat(procedure.getGracePeriodDays()).isZero();
    }

    @Test
    @DisplayName("Deve seguir o contrato do hashCode (#99)")
    void shouldFollowHashCodeContract() {
        java.util.UUID id = java.util.UUID.randomUUID();
        Procedure p1 = Procedure.restore(id, "Consulta", new Money(new java.math.BigDecimal("100.00")));
        Procedure p2 = Procedure.restore(id, "Consulta", new Money(new java.math.BigDecimal("100.00")));

        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
        assertThat(p1.hashCode()).isNotZero();
    }
    }