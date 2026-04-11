package br.ifsp.hospital.functional;

import br.ifsp.hospital.annotation.Functional;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Money;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

@UnitTest
@Functional
@DisplayName("Functional – Money (Value Object)")

class MoneyFunctionalTest {

    @Test
    @DisplayName("PE – valor nulo → IllegalArgumentException")
    void pe_valorNulo() {
        assertThatThrownBy(() -> new Money(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "-1.00", "-999.99"})
    @DisplayName("PE – valor negativo → IllegalArgumentException")
    void pe_valorNegativo(String valor) {
        assertThatThrownBy(() -> new Money(new BigDecimal(valor)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("AVL – valor = 0.00 → válido (limite inferior)")
    void avl_construcao_zero() {
        Money m = new Money(BigDecimal.ZERO);
        assertThat(m.getAmount()).isEqualByComparingTo("0.00");
    }

    @Test
    @DisplayName("multiply – multiplica por quantidade positiva")
    void multiply_deveMultiplicar() {
        Money m = new Money(new BigDecimal("25.00"));
        assertThat(m.multiply(4).getAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("PE – multiply por 0 → IllegalArgumentException")
    void pe_multiply_por0() {
        Money m = new Money(new BigDecimal("10.00"));
        assertThatThrownBy(() -> m.multiply(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("add – soma dois valores corretamente")
    void add_deveSomar() {
        Money a = new Money(new BigDecimal("100.00"));
        Money b = new Money(new BigDecimal("50.00"));
        assertThat(a.add(b).getAmount()).isEqualByComparingTo("150.00");
    }



}
