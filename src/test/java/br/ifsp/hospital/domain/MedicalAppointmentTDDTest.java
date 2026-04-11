package br.ifsp.hospital.domain;

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MedicalAppointmentTDDTest {

    @Test
    @UnitTest
    @TDD
    void shouldCancelOpenAppointment(){
        Patient dummyPatient = Patient.of("John Doe", "123456789", InsuranceType.BASIC);
        Doctor dummyDoctor = Doctor.of("John Doe", "Cardiology", "54321");
        LocalDateTime dummyScheduledAt = LocalDateTime.now().plusDays(1);

        MedicalAppointment appointment = MedicalAppointment.of(dummyPatient, dummyDoctor, dummyScheduledAt);

        appointment.cancel();
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELED);
    }
}
