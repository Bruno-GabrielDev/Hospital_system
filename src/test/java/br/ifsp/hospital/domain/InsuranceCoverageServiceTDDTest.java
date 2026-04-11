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

    @Test
    @DisplayName("#16 – BASIC → 30% de desconto (paciente paga 70% = 140.00)")
    void t16_deveAplicar30PorCentoParaBasic() {

        Money result = service.applyCoverage(total200, InsuranceType.BASIC);

        assertThat(result.getAmount()).isEqualByComparingTo("140.00");
    }

    @Test
    @DisplayName("#17 – PREMIUM → 70% de desconto (paciente paga 30% = 60.00)")
    void t17_deveAplicar70PorCentoParaPremium() {

        Money result = service.applyCoverage(total200, InsuranceType.PREMIUM);

        assertThat(result.getAmount()).isEqualByComparingTo("60.00");
    }

}
