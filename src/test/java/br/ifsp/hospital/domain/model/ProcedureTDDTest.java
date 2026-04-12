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
@DisplayName("TDD – Procedure")
class ProcedureTDDTest {

    @Test
    @DisplayName("#6 – restore deve retornar instância com todos os valores corretos")
    void shouldReturnProcedureInstanceWithAllValuesCorrect() {
        UUID id = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        Money cost = new Money(new BigDecimal("150.00"));
        Procedure restored = Procedure.restore(id, "Consulta", cost);

        assertThat(restored).isNotNull();
        assertThat(restored.getId()).isEqualTo(id);
        assertThat(restored.getName()).isEqualTo("Consulta");
        assertThat(restored.getCost().getAmount()).isEqualByComparingTo("150.00");
    }
}