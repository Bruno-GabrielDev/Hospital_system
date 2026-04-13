package br.ifsp.hospital.functional;

import br.ifsp.hospital.annotation.Functional;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Functional
@UnitTest
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
            "0, false",  // Valor limite exato (Partição Inválida)
            "1, true",    // Valor limite válido (Partição Válida)
            "10, true"   // Valor nominal (Partição Válida)
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

    @ParameterizedTest(name = "[{index}] Horas de antecedência: {0} -> Esperado permitido: {1}")
    @DisplayName("AVL: Cancelamento com horas de antecedência nas fronteiras")
    @CsvSource({
            "2, false", // Valor limite inferior (Partição Inválida - Bloqueia)
            "3, true"   // Valor limite exato (Partição Válida - Permite)
    })
    void testCancellationTimeBoundary(int hoursInAdvance, boolean isAllowed) {
        LocalDateTime scheduledAt = LocalDateTime.now().plusHours(hoursInAdvance);
        MedicalAppointment appointment = MedicalAppointment.of(dummyPatient, dummyDoctor, scheduledAt);

        if (isAllowed) {
            appointment.cancel("Imprevisto");
            assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELED);
        } else {
            assertThatExceptionOfType(IllegalStateException.class)
                    .isThrownBy(() -> appointment.cancel("Imprevisto"))
                    .withMessageContaining("Cannot cancel an appointment with less than");
        }
    }
}