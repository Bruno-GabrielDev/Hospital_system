package br.ifsp.hospital.domain;

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TDD
@UnitTest
@DisplayName("TDD – MedicalAppointment")
public class MedicalAppointmentTDDTest {

    private MedicalAppointment appointment;
    private Patient dummyPatient;
    private Doctor dummyDoctor;

    @BeforeEach
    void setUp() {
        dummyPatient = Patient.of("John Doe", "123456789", InsuranceType.BASIC);
        dummyDoctor = Doctor.of("Dr. House", "Cardiology", "54321");
        LocalDateTime dummyScheduledAt = LocalDateTime.now().plusDays(1);

        appointment = MedicalAppointment.of(dummyPatient, dummyDoctor, dummyScheduledAt);
    }

    @Test
    void shouldCancelOpenAppointment() {
        appointment.cancel("PATIENT_REQUEST");

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELED);
    }

    @Test
    void shouldNotCancelClosedAppointment() {
        appointment.close();

        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> appointment.cancel("PATIENT_REQUEST"))
            .withMessage("Closed appointment cannot be canceled.");
    }

    @Test
    void shouldCancelBilledAppointmentAndTriggerRefund() {
        appointment.markAsBilled();

        appointment.cancel("PATIENT_REQUEST");

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELED);
        assertThat(appointment.isRefunded()).isTrue();
    }

    @Test
    void shouldCancelProceduresWhenCancelingAppointment() {
        Procedure dummyProcedure = Procedure.of("Dummy Procedure", new Money(BigDecimal.valueOf(100)));
        AppointmentProcedure dummyAppointmentProcedure = AppointmentProcedure.of(dummyProcedure, 2);

        appointment.addProcedure(dummyAppointmentProcedure);
        appointment.cancel("PATIENT_REQUEST");

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELED);

        assertThat(appointment.getProcedures())
                .isNotEmpty()
                .allSatisfy(proc -> assertThat(proc.getStatus()).isEqualTo(ProcedureStatus.CANCELED));
    }

    @Test
    void shouldRefundProceduresWhenCancelingBilledAppointment() {
        Procedure dummyProcedure = Procedure.of("Dummy Procedure", new Money(BigDecimal.valueOf(100)));
        AppointmentProcedure dummyAppointmentProcedure = AppointmentProcedure.of(dummyProcedure, 2);

        appointment.addProcedure(dummyAppointmentProcedure);
        appointment.markAsBilled();

        appointment.cancel("PATIENT_REQUEST");

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELED);
        assertThat(appointment.isRefunded()).isTrue();

        assertThat(appointment.getProcedures())
                .isNotEmpty()
                .allSatisfy(proc -> {
                    assertThat(proc.getStatus()).isEqualTo(ProcedureStatus.CANCELED);
                    assertThat(proc.isRefunded()).isTrue();
                });
    }

    @Test
    void shouldNotCancelAppointmentWithoutReason() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> appointment.cancel(""))
                .withMessage("Cancellation reason is required.");
    }

    @Test
    void shouldNotCancelAppointmentWithLessThanThreeHoursNotice() {
        LocalDateTime urgentScheduledAt = LocalDateTime.now().plusHours(2);
        MedicalAppointment urgentAppointment = MedicalAppointment.of(dummyPatient, dummyDoctor, urgentScheduledAt);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> urgentAppointment.cancel("PATIENT_REQUEST"))
                .withMessage("Cannot cancel an appointment with less than 3 hours in advance.");
    }

    @Test
    void shouldNotCancelAlreadyCanceledAppointment() {
        appointment.cancel("PATIENT_REQUEST");

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> appointment.cancel("PATIENT_REQUEST"))
                .withMessage("Cannot cancel an already canceled appointment.");
    }

    @Test
    @DisplayName("#6 – restore deve retornar instância com todos os valores corretos")
    void shouldReturnInstanceWithAllCorrectValues() {
        UUID id = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        Patient patient = Patient.of("João", "111", InsuranceType.BASIC);
        Doctor doctor   = Doctor.of("Dr. Ana", "Ortopedia", "CRM-11");
        LocalDateTime scheduled = LocalDateTime.of(2026, 5, 1, 10, 0);
        LocalDateTime created   = LocalDateTime.of(2026, 4, 1, 8, 0);

        MedicalAppointment appointment = MedicalAppointment.restore(
                id, patient, doctor, scheduled, created,
                AppointmentStatus.OPEN, 0, List.of()
        );

        assertThat(appointment).isNotNull();
        assertThat(appointment.getId()).isEqualTo(id);
        assertThat(appointment.getPatient()).isEqualTo(patient);
        assertThat(appointment.getDoctor()).isEqualTo(doctor);
        assertThat(appointment.getScheduledAt()).isEqualTo(scheduled);
        assertThat(appointment.getCreatedAt()).isEqualTo(created);
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.OPEN);
        assertThat(appointment.getRescheduleCount()).isEqualTo(0);
        assertThat(appointment.getProcedures()).isEmpty();
    }

    @Test
    @DisplayName("#26 – calculateGrossTotal deve somar o custo de todos os procedimentos")
    void calculateGrossTotalMaySumCostsOfAllProcedures() {
        Patient patient = Patient.of("João", "111", InsuranceType.BASIC);
        Doctor doctor  = Doctor.of("Dr. Ana", "Ortopedia", "CRM-11");
        MedicalAppointment appointment = MedicalAppointment.of(patient, doctor,
                LocalDateTime.now().plusDays(1));

        Procedure p1 = Procedure.of("Consulta", new Money(new BigDecimal("200.00")));
        Procedure p2 = Procedure.of("RX",       new Money(new BigDecimal("100.00")));
        appointment.addProcedure(AppointmentProcedure.of(p1, 1));
        appointment.addProcedure(AppointmentProcedure.of(p2, 2));

        Money total = appointment.calculateGrossTotal();

        assertThat(total.getAmount()).isEqualByComparingTo("400.00");
    }

    @Test
    @DisplayName("Deve atualizar data e hora quando reagendado para horário vago (#51)")
    void shouldUpdateDateAndTimeWhenRescheduledToAvailableSlot() {
        LocalDateTime newScheduledAt = LocalDateTime.now().plusDays(2);
        DoctorScheduleValidator validator = Mockito.mock(DoctorScheduleValidator.class);

        when(validator.isAvailable(dummyDoctor, newScheduledAt)).thenReturn(true);

        appointment.reschedule(newScheduledAt, validator);

        assertThat(appointment.getScheduledAt()).isEqualTo(newScheduledAt);
    }

    @Test
    @DisplayName("Deve manter o estado original (exceto o horário) após reagendamento (#52)")
    void shouldMaintainOriginalStateExceptTimeWhenRescheduled() {
        AppointmentStatus initialStatus = appointment.getStatus();
        Patient initialPatient = appointment.getPatient();
        Doctor initialDoctor = appointment.getDoctor();

        LocalDateTime newScheduledAt = LocalDateTime.now().plusDays(2);
        DoctorScheduleValidator validator = Mockito.mock(DoctorScheduleValidator.class);
        when(validator.isAvailable(dummyDoctor, newScheduledAt)).thenReturn(true);

        appointment.reschedule(newScheduledAt, validator);

        assertThat(appointment.getScheduledAt()).isEqualTo(newScheduledAt);
        assertThat(appointment.getStatus()).isEqualTo(initialStatus);
        assertThat(appointment.getPatient()).isEqualTo(initialPatient);
        assertThat(appointment.getDoctor()).isEqualTo(initialDoctor);
    }
}

