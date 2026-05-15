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
        assertThat(money50.hashCode()).isNotZero();
    }

    @Test
    @DisplayName("Deve seguir o contrato do equals e hashCode para valores iguais")
    void shouldFollowEqualsAndHashCodeContractForSameValues() {
        Money money1 = new Money(new BigDecimal("100.00"));
        Money money2 = new Money(new BigDecimal("100.00"));

        assertThat(money1).isEqualTo(money2);
        assertThat(money1.hashCode()).isEqualTo(money2.hashCode());
    }

    @Test
    @DisplayName("Não deve ser igual a null ou classes diferentes")
    void shouldNotBeEqualToNullOrDifferentClasses() {
        Money money = new Money(new BigDecimal("100.00"));

        assertThat(money).isNotEqualTo(null);
        assertThat(money).isNotEqualTo("100.00");
    }
}