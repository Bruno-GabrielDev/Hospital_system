package br.ifsp.hospital.domain;

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MedicalAppointmentTDDTest {

    private MedicalAppointment appointment;

    @BeforeEach
    void setUp() {
        Patient dummyPatient = Patient.of("John Doe", "123456789", InsuranceType.BASIC);
        Doctor dummyDoctor = Doctor.of("Dr. House", "Cardiology", "54321");
        LocalDateTime dummyScheduledAt = LocalDateTime.now().plusDays(1);

        appointment = MedicalAppointment.of(dummyPatient, dummyDoctor, dummyScheduledAt);
    }

    @Test
    @UnitTest
    @TDD
    void shouldCancelOpenAppointment() {
        appointment.cancel();

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELED);
    }

    @Test
    @UnitTest
    @TDD
    void shouldNotCancelClosedAppointment() {
        appointment.close();

        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(appointment::cancel)
            .withMessage("Closed appointment cannot be canceled.");
    }

    @Test
    @UnitTest
    @TDD
    void shouldCancelBilledAppointmentAndTriggerRefund() {
        appointment.markAsBilled();

        appointment.cancel();

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELED);
        assertThat(appointment.isRefunded()).isTrue();
    }

    @Test
    @UnitTest
    @TDD
    void shouldCancelProceduresWhenCancelingAppointment() {
        Procedure dummyProcedure = Procedure.of("Dummy Procedure", new Money(BigDecimal.valueOf(100)));
        AppointmentProcedure dummyAppointmentProcedure = AppointmentProcedure.of(dummyProcedure, 2);

        appointment.addProcedure(dummyAppointmentProcedure);
        appointment.cancel();

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELED);

        assertThat(appointment.getProcedures())
                .isNotEmpty()
                .allSatisfy(proc -> assertThat(proc.getStatus()).isEqualTo(ProcedureStatus.CANCELED));
    }

    @Test
    @UnitTest
    @TDD
    void shouldRefundProceduresWhenCancelingBilledAppointment() {
        Procedure dummyProcedure = Procedure.of("Dummy Procedure", new Money(BigDecimal.valueOf(100)));
        AppointmentProcedure dummyAppointmentProcedure = AppointmentProcedure.of(dummyProcedure, 2);

        appointment.addProcedure(dummyAppointmentProcedure);
        appointment.markAsBilled();

        appointment.cancel();

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
    @UnitTest
    @TDD
    void shouldNotCancelAppointmentWithoutReason() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> appointment.cancel(""))
                .withMessage("Cancellation reason is required.");
    }
}
