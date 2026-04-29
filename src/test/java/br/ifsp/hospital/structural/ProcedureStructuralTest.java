package br.ifsp.hospital.structural;

import br.ifsp.hospital.annotation.Structural;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Money;
import br.ifsp.hospital.domain.model.Procedure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@Structural
@DisplayName("Structural – Procedure equals/hashCode")
class ProcedureStructuralTest {

    @Test
    @DisplayName("equals – objeto nulo → false")
    void equals_objetoNulo_retornaFalse() {
        Procedure p = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        assertThat(p.equals(null)).isFalse();
    }

    @Test
    @DisplayName("equals – classe diferente → false")
    void equals_classeDiferente_retornaFalse() {
        Procedure p = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        assertThat(p.equals("não é Procedure")).isFalse();
    }

    @Test
    @DisplayName("equals – mesmo ID → true")
    void equals_mesmoId_retornaTrue() {
        Procedure p1 = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        Procedure p2 = Procedure.restore(p1.getId(), "Outro Nome", new Money(new BigDecimal("999.00")));
        assertThat(p1.equals(p2)).isTrue();
    }

    @Test
    @DisplayName("equals – IDs diferentes → false")
    void equals_idsDiferentes_retornaFalse() {
        Procedure p1 = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        Procedure p2 = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        assertThat(p1.equals(p2)).isFalse();
    }
}