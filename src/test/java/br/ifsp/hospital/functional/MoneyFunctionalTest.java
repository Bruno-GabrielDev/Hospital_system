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



}
