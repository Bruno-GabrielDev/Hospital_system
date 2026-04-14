package br.ifsp.hospital.functional;

import br.ifsp.hospital.annotation.Functional;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Money;
import br.ifsp.hospital.domain.service.InsuranceCoverageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@Functional
@DisplayName("Functional – Insurance Coverage BVA & EP")
class InsuranceCoverageFunctionalTest {

    private InsuranceCoverageService sut;

    @BeforeEach
    void setUp() {
        sut = new InsuranceCoverageService();
    }

    @ParameterizedTest(name = "Value {0} with {1} insurance should round correctly")
    @CsvSource({
            "100.05, BASIC, 70.04, 30.01",
            "100.05, PREMIUM, 30.02, 70.03",
            "0.01, BASIC, 0.01, 0.00",
            "0.01, PREMIUM, 0.00, 0.01"
    })
    @DisplayName("BVA – Should handle precision and rounding for small/complex decimals")
    void shouldHandlePrecisionAndRoundingBva(String amount, InsuranceType type, String expectedPatient, String expectedCovered) {
        Money input = new Money(new BigDecimal(amount));

        Money patientResult = sut.applyCoverage(input, type);
        Money insuranceResult = sut.getCoveredAmount(input, type);

        assertThat(patientResult.getAmount()).isEqualByComparingTo(expectedPatient);
        assertThat(insuranceResult.getAmount()).isEqualByComparingTo(expectedCovered);

        assertThat(patientResult.getAmount().add(insuranceResult.getAmount()))
                .isEqualByComparingTo(amount);
    }

    @ParameterizedTest(name = "Large value {0} with {1}")
    @CsvSource({
            "1000000.00, BASIC, 700000.00, 300000.00",
            "1000000.00, PREMIUM, 300000.00, 700000.00"
    })
    @DisplayName("BVA – Should handle extremely large values without overflow")
    void shouldHandleLargeValuesBva(String amount, InsuranceType type, String expectedPatient, String expectedCovered) {
        Money input = new Money(new BigDecimal(amount));

        Money patientResult = sut.applyCoverage(input, type);
        Money insuranceResult = sut.getCoveredAmount(input, type);

        assertThat(patientResult.getAmount()).isEqualByComparingTo(expectedPatient);
        assertThat(insuranceResult.getAmount()).isEqualByComparingTo(expectedCovered);
    }

    @ParameterizedTest(name = "Zero value with {0}")
    @CsvSource({
            "BASIC",
            "PREMIUM",
            "NONE"
    })
    @DisplayName("BVA – Should return zero for zero amount input")
    void shouldReturnZeroForZeroAmountBva(InsuranceType type) {
        Money zero = new Money(BigDecimal.ZERO);

        Money patientResult = sut.applyCoverage(zero, type);
        Money insuranceResult = sut.getCoveredAmount(zero, type);

        assertThat(patientResult.getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(insuranceResult.getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}