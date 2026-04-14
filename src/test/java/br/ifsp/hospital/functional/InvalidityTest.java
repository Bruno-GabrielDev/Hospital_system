package br.ifsp.hospital.functional;

import br.ifsp.hospital.annotation.Functional;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@UnitTest
@Functional
@DisplayName("Functional – Invalidity & Boundary Value Analysis")
class InvalidityTest {

    @Nested
    @DisplayName("Patient – Invalid Fields (EP)")
    class PatientInvalidity {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        @DisplayName("EP – Invalid name should throw IllegalArgumentException")
        void invalidNameShouldThrowIllegalArgumentException(String name) {
            assertThatThrownBy(() -> Patient.of(name, "111", InsuranceType.BASIC))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("EP – Null insurance type should throw NullPointerException")
        void nullInsuranceTypeShouldThrowNullPointerException() {
            assertThatThrownBy(() -> Patient.of("Bruno", "111", null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("MedicalAppointment – States & Quantities (BVA)")
    class AppointmentInvalidity {

        private MedicalAppointment appointment;
        private Procedure procedure;

        @BeforeEach
        void setUp() {
            Patient patient = Patient.of("Carlos", "123", InsuranceType.BASIC);
            Doctor doctor = Doctor.of("Dr. Silva", "Cardio", "LICENSE-123");
            procedure = Procedure.of("Consultation", new Money(new BigDecimal("100.00")));
            appointment = MedicalAppointment.of(patient, doctor, LocalDateTime.now().plusDays(1));
        }

        @Test
        @DisplayName("State – Should not allow adding procedures to a closed appointment")
        void shouldNotAllowAddingProcedureToClosedAppointment() {
            appointment.close();

            assertThatThrownBy(() -> appointment.addProcedure(AppointmentProcedure.of(procedure, 1)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("não está aberto");
        }

        @Test
        @DisplayName("State – Should not allow cancelling a closed appointment")
        void shouldNotAllowCancellingClosedAppointment() {
            appointment.close();

            assertThatThrownBy(() -> appointment.cancel("Patient withdrew"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Closed appointment cannot be canceled");
        }

        @ParameterizedTest(name = "Quantity {0} is invalid (BVA)")
        @ValueSource(ints = {0, -1})
        @DisplayName("BVA – Invalid quantities on the boundary")
        void invalidQuantityBoundaryShouldThrowIllegalArgumentException(int quantity) {
            assertThatThrownBy(() -> AppointmentProcedure.of(procedure, quantity))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Doctor & Money – Structural Constraints (BVA)")
    class StructuralInvalidity {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("EP – Doctor license is mandatory")
        void invalidLicenseShouldThrowIllegalArgumentException(String license) {
            assertThatThrownBy(() -> Doctor.of("Dr. House", "Diagnostics", license))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "Value {0} should be rejected (BVA)")
        @CsvSource({
                "-0.01",
                "-100.00"
        })
        @DisplayName("BVA – Negative monetary amounts below 0.00")
        void negativeMoneyBoundaryShouldThrowIllegalArgumentException(String value) {
            BigDecimal negativeValue = new BigDecimal(value);
            assertThatThrownBy(() -> new Money(negativeValue))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("não pode ser negativo");
        }

        @Test
        @DisplayName("BVA – Zero amount should be valid (Lower boundary)")
        void zeroMoneyBoundaryShouldBeValid() {
            assertThatCode(() -> new Money(BigDecimal.ZERO))
                    .doesNotThrowAnyException();
        }
    }
}