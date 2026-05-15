package br.ifsp.hospital.mutation;

import br.ifsp.hospital.annotation.Mutation;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.AppointmentProcedure;
import br.ifsp.hospital.domain.model.Money;
import br.ifsp.hospital.domain.model.Procedure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@Mutation
@DisplayName("Mutation – AppointmentProcedure")
public class AppointmentProcedureMutationTest {

    @Test
    @DisplayName("Deve retornar false para isRefunded logo após a criação")
    void isRefundedNewProceduredShouldReturnFalse() {
        Procedure dummyProcedure = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        AppointmentProcedure appointmentProcedure = AppointmentProcedure.of(dummyProcedure, 1);

        assertThat(appointmentProcedure.isRefunded()).isFalse();
    }

    @Test
    @DisplayName("Deve gerar hashCodes diferentes para instâncias distintas (IDs diferentes)")
    void hashCodeDifferentInstancesShouldReturnDifferentHashCodes() {
        Procedure dummyProcedure = Procedure.of("Consulta", new Money(new java.math.BigDecimal("100.00")));

        AppointmentProcedure ap1 = AppointmentProcedure.of(dummyProcedure, 1);
        AppointmentProcedure ap2 = AppointmentProcedure.of(dummyProcedure, 1);

        assertThat(ap1.hashCode()).isNotEqualTo(ap2.hashCode());
    }

    @Test
    @DisplayName("Deve atribuir um ID ao criar via of (#90)")
    void shouldAssignIdWhenCreatedUsingOf() {
        Procedure procedure = Procedure.of("RX", new Money(new java.math.BigDecimal("100.00")));
        AppointmentProcedure ap = AppointmentProcedure.of(procedure, 1);
        assertThat(ap.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve seguir o contrato do hashCode (#92)")
    void shouldFollowHashCodeContract() {
        java.util.UUID id = java.util.UUID.randomUUID();
        Procedure p = Procedure.of("RX", new Money(new java.math.BigDecimal("100.00")));
        AppointmentProcedure ap1 = AppointmentProcedure.restore(id, p, 1);
        AppointmentProcedure ap2 = AppointmentProcedure.restore(id, p, 1);

        assertThat(ap1.hashCode()).isEqualTo(ap2.hashCode());
        assertThat(ap1.hashCode()).isNotZero();
    }
}
