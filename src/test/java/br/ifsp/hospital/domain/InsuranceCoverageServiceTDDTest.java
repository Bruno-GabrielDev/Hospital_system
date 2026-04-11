package br.ifsp.hospital.domain;

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Money;
import br.ifsp.hospital.domain.service.InsuranceCoverageService;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;

@UnitTest
@TDD
@DisplayName("TDD – InsuranceCoverageService (#16–#20)")

class InsuranceCoverageServiceTDDTest {
    private InsuranceCoverageService service;
    private Money total200;

    @BeforeEach
    void setUp() {
        service  = new InsuranceCoverageService();
        total200 = new Money(new BigDecimal("200.00"));
    }
    @Test
    @DisplayName("#18 – NONE → sem desconto (paciente paga 100% = 200.00)")
    void t18_semDescontoParaNone() {

        Money result = service.applyCoverage(total200, InsuranceType.NONE);

        assertThat(result.getAmount()).isEqualByComparingTo("200.00");
    }
}
