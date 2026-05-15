package br.ifsp.hospital.mutation;

import br.ifsp.hospital.annotation.Mutation;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Patient;
import br.ifsp.hospital.domain.repository.AppointmentRepository;
import br.ifsp.hospital.domain.repository.DoctorRepository;
import br.ifsp.hospital.domain.repository.PatientRepository;
import br.ifsp.hospital.domain.service.AppointmentService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
@Mutation
@ExtendWith(MockitoExtension.class)
@DisplayName("Mutation – AppointmentService")
public class AppointmentServiceMutationTest {

    @Mock private PatientRepository patientRepository;
    @Mock private DoctorRepository doctorRepository;

    @Mock private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService sut;

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando o médico não for encontrado na criação")
    void createDoctorNotFoundShouldThrowException() {
        UUID patientId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        LocalDateTime validDate = LocalDateTime.now().plusDays(1);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(mock(Patient.class)));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.create(patientId, doctorId, validDate))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Médico não encontrado");
    }
}