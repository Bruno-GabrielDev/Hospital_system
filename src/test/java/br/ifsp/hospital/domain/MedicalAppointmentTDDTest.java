package br.ifsp.hospital.domain;

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
}
