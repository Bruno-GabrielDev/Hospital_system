package br.ifsp.hospital.functional;

import br.ifsp.hospital.annotation.Functional;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@UnitTest
@Functional
@DisplayName("Functional – Invalidity")
class InvalidityTest {

    @Nested
    @DisplayName("Patient – campos inválidos")
    class PatientInvalidity {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        @DisplayName("PE – name nulo, vazio ou só espaços → exceção")
        void pe_nameInvalido(String name) {
            assertThatThrownBy(() -> Patient.of(name, "111", InsuranceType.BASIC))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("PE – insuranceType nulo → NullPointerException")
        void pe_insuranceTypeNulo() {
            assertThatThrownBy(() -> Patient.of("Bruno", "111", null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("MedicalAppointment – Suposição de Erros (Estados)")
    class AppointmentInvalidity {

        private MedicalAppointment appointment;
        private Procedure procedure;

        @BeforeEach
        void setUp() {
            Patient patient = Patient.of("Carlos", "123", InsuranceType.BASIC);
            Doctor doctor = Doctor.of("Dr. Silva", "Cardio", "CRM-1");
            procedure = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
            appointment = MedicalAppointment.of(patient, doctor, LocalDateTime.now().plusDays(1));
        }

        @Test
        @DisplayName("Erro Sugerido: Não deve permitir adicionar procedimento a um atendimento FECHADO")
        void shouldNotAllowAddProcedureToClosedAppointment() {
            appointment.close();

            assertThatThrownBy(() -> appointment.addProcedure(AppointmentProcedure.of(procedure, 1)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("encerrado");
        }

        @Test
        @DisplayName("Erro Sugerido: Não deve permitir cancelar um atendimento que já está FECHADO")
        void shouldNotAllowCancelClosedAppointment() {
            appointment.close();

            assertThatThrownBy(() -> appointment.cancel("Paciente desistiu"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("encerrado");
        }

        @Test
        @DisplayName("Erro Sugerido: Deve impedir quantidade zero ou negativa de procedimentos")
        @Tag("BusinessRule")
        void shouldRejectInvalidQuantity() {
            assertThatThrownBy(() -> AppointmentProcedure.of(procedure, 0))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> AppointmentProcedure.of(procedure, -1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Doctor & Procedure – Campos Obrigatórios")
    class StructuralInvalidity {

        @Test
        @DisplayName("Suposição: CRM nulo deve disparar erro de validação")
        void crmInvalido() {
            assertThatThrownBy(() -> Doctor.of("Dr. House", "Diagnóstico", null))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Suposição: Preço negativo em procedimento deve ser proibido")
        void precoNegativo() {
            Money negativeMoney = new Money(new BigDecimal("-50.00"));
            assertThatThrownBy(() -> Procedure.of("Exame", negativeMoney))
                    .isInstanceOf(Exception.class);
        }
    }
}