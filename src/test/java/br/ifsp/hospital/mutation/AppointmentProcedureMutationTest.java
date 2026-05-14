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
}
