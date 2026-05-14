package br.ifsp.hospital.mutation;

import br.ifsp.hospital.annotation.Mutation;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@Mutation
@DisplayName("Mutation – Money")
public class MoneyMutationTest {

    @Test
    @DisplayName("Deve gerar hashCodes diferentes para valores monetários distintos")
    void hashCodeDifferentAmountsShouldReturnDifferentHashCodes() {
        Money money50 = new Money(new BigDecimal("50.00"));
        Money money100 = new Money(new BigDecimal("100.00"));

        assertThat(money50.hashCode()).isNotEqualTo(money100.hashCode());
    }
}