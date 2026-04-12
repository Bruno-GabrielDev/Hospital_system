package br.ifsp.hospital.domain;

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.AppointmentStatus;
import br.ifsp.hospital.domain.model.Doctor;
import br.ifsp.hospital.domain.model.MedicalAppointment;
import br.ifsp.hospital.domain.model.Patient;
import br.ifsp.hospital.domain.repository.AppointmentRepository;
import br.ifsp.hospital.domain.repository.DoctorRepository;
import br.ifsp.hospital.domain.repository.PatientRepository;
import br.ifsp.hospital.domain.repository.ProcedureRepository;
import br.ifsp.hospital.domain.service.AppointmentService;
import br.ifsp.hospital.domain.service.InsuranceCoverageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@UnitTest
@TDD
@ExtendWith(MockitoExtension.class)
@DisplayName("TDD – InsuranceCoverageService (#x-#y)")
public class AppointmentServiceTDDTest {

    @Mock AppointmentRepository appointmentRepository;
    @Mock PatientRepository patientRepository;
    @Mock DoctorRepository doctorRepository;
    @Mock ProcedureRepository procedureRepository;
    AppointmentService sut;
    @Mock Patient patient;
    @Mock Doctor doctor ;
    UUID appointmentId;

    @BeforeEach
    void setup() {
        InsuranceCoverageService service = new InsuranceCoverageService();
        sut = new AppointmentService(
                appointmentRepository,
                patientRepository,
                doctorRepository,
                procedureRepository,
                service
        );
        appointmentId = UUID.fromString("568374d2-990c-4395-9988-82e666a4f208");
    }

    @Test
    @DisplayName("#26 - It should allow payment and show costs when the appointment is closed.")
    void itShouldAllowPaymentAndShowCostsWhenTheAppointmentIsClosed() {

        MedicalAppointment appointment = MedicalAppointment.of(patient, doctor, LocalDateTime.of(
                2026,
                4,
                11,
                10, 0
        ));

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        MedicalAppointment result = sut.close(appointmentId);

        assertNotNull(result, "O atendimento retornado não deve ser nulo");
        assertEquals(AppointmentStatus.CLOSED, result.getStatus(), "O status deve ser CLOSED após o fechamento");

        verify(appointmentRepository).save(appointment);
    }
    
    
}
