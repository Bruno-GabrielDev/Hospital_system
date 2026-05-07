package br.ifsp.hospital.structural;

import br.ifsp.hospital.annotation.Structural;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import br.ifsp.hospital.domain.repository.AppointmentRepository;
import br.ifsp.hospital.domain.service.DefaultDoctorScheduleValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@UnitTest
@Structural
@ExtendWith(MockitoExtension.class)
@DisplayName("Structural – DefaultDoctorScheduleValidator")
class DefaultDoctorScheduleValidatorStructuralTest {

    @Mock private AppointmentRepository appointmentRepository;
    @InjectMocks private DefaultDoctorScheduleValidator sut;

    @Test
    @DisplayName("isAvailable – sem atendimentos no horário → true")
    void isAvailable_semAtendimentos_retornaTrue() {
        Doctor d = Doctor.of("Dr. Silva", "Cardio", "CRM-001");
        LocalDateTime horario = LocalDateTime.now().plusDays(1);
        when(appointmentRepository.findByDoctorIdAndScheduledAt(any(), any()))
                .thenReturn(Collections.emptyList());

        assertThat(sut.isAvailable(d, horario)).isTrue();
    }

    @Test
    @DisplayName("isAvailable – atendimento ativo no horário → false")
    void isAvailable_atendimentoAtivo_retornaFalse() {
        Doctor d = Doctor.of("Dr. Silva", "Cardio", "CRM-001");
        Patient p = Patient.of("Bruno", "111", InsuranceType.BASIC);
        LocalDateTime horario = LocalDateTime.now().plusDays(1);

        MedicalAppointment ativo = MedicalAppointment.of(p, d, horario);

        when(appointmentRepository.findByDoctorIdAndScheduledAt(any(), any()))
                .thenReturn(List.of(ativo));

        assertThat(sut.isAvailable(d, horario)).isFalse();
    }

    @Test
    @DisplayName("isAvailable – atendimento cancelado no horário → true")
    void isAvailable_atendimentoCancelado_retornaTrue() {
        Doctor d = Doctor.of("Dr. Silva", "Cardio", "CRM-001");
        Patient p = Patient.of("Bruno", "111", InsuranceType.BASIC);
        LocalDateTime horario = LocalDateTime.now().plusDays(1);

        MedicalAppointment cancelado = MedicalAppointment.of(p, d, horario);
        cancelado.cancel("Imprevisto");

        when(appointmentRepository.findByDoctorIdAndScheduledAt(any(), any()))
                .thenReturn(List.of(cancelado));

        assertThat(sut.isAvailable(d, horario)).isTrue();
    }
}