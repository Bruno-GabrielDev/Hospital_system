package br.ifsp.hospital.domain.model;

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@TDD
@DisplayName("TDD – AppointmentProcedure")
class AppointmentProcedureTDDTest {

    @Test
    @DisplayName("#6 – restore deve retornar instância com valores corretos")
    void shouldReturnAppointmentProcedureInstanceWithAllValuesCorrect() {
        UUID id = UUID.randomUUID();
        Procedure procedure = Procedure.of("RX", new Money(new BigDecimal("100.00")));
        AppointmentProcedure restored = AppointmentProcedure.restore(id, procedure, 3);

        assertThat(restored).isNotNull();
        assertThat(restored.getId()).isEqualTo(id);
        assertThat(restored.getQuantity()).isEqualTo(3);
        assertThat(restored.getProcedure()).isEqualTo(procedure);
    }

    @Test
    @DisplayName("#6 – getTotalCost deve ser custo × quantidade")
    void totalCostShouldBeCostMultipliedByQuantity() {
        Procedure procedure = Procedure.of("RX", new Money(new BigDecimal("100.00")));
        AppointmentProcedure appointmentProcedure = AppointmentProcedure.of(procedure, 3);
        assertThat(appointmentProcedure.getTotalCost().getAmount()).isEqualByComparingTo("300.00");
    }
}