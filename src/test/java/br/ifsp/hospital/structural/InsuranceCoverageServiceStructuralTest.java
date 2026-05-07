package br.ifsp.hospital.structural;

import br.ifsp.hospital.annotation.Structural;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Money;
import br.ifsp.hospital.domain.service.InsuranceCoverageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@Structural
@DisplayName("Structural – InsuranceCoverageService")
class InsuranceCoverageServiceStructuralTest {

    private final InsuranceCoverageService service = new InsuranceCoverageService();

    @Test
    @DisplayName("getCoveredAmount – type null → retorna zero")
    void getCoveredAmount_typeNull_retornaZero() {
        Money total = new Money(new BigDecimal("200.00"));

        Money result = service.getCoveredAmount(total, null);

        assertThat(result.getAmount()).isEqualByComparingTo("0.00");
    }
}