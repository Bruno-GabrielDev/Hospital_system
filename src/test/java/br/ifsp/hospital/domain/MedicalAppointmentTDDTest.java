package br.ifsp.hospital.domain;

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.AppointmentStatus;
import br.ifsp.hospital.domain.model.MedicalAppointment;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MedicalAppointmentTDDTest {

    @Test
    @UnitTest
    @TDD
    void shouldCancelOpenAppointment(){
        MedicalAppointment appointment = MedicalAppointment.of(null, null, null);

        appointment.cancel();
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELED);
    }
}
