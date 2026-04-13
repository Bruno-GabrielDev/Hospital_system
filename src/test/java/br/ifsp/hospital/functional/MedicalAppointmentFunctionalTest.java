package br.ifsp.hospital.functional;

import br.ifsp.hospital.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Testes Funcionais – MedicalAppointment")
public class MedicalAppointmentFunctionalTest {

    private Patient dummyPatient;
    private Doctor dummyDoctor;

    @BeforeEach
    void setUp() {
        dummyPatient = Patient.of("John Doe", "123456789", InsuranceType.BASIC);
        dummyDoctor = Doctor.of("Dr. House", "Cardiology", "54321");
    }

    @ParameterizedTest(name = "[{index}] Quantidade: {0} -> Esperado válido: {1}")
    @DisplayName("AVL: Criação de procedimento com quantidades nas fronteiras")
    @CsvSource({
            "-1, false", // Valor limite inferior (Partição Inválida)
    })
    void testProcedureQuantityBoundary(int quantity, boolean isValid) {
        Procedure dummyProcedure = Procedure.of("Consulta", new Money(BigDecimal.valueOf(100)));

        if (isValid) {
            AppointmentProcedure ap = AppointmentProcedure.of(dummyProcedure, quantity);
            assertThat(ap).isNotNull();
            assertThat(ap.getTotalCost().getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100 * quantity));
        } else {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> AppointmentProcedure.of(dummyProcedure, quantity));
        }
    }
}