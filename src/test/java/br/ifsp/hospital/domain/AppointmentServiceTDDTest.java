package br.ifsp.hospital.domain;

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import br.ifsp.hospital.domain.repository.*;
import br.ifsp.hospital.domain.service.AppointmentService;
import br.ifsp.hospital.domain.service.InsuranceCoverageService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@UnitTest
@TDD
@ExtendWith(MockitoExtension.class)
@DisplayName("TDD – AppointmentService")
class AppointmentServiceTDDTest {

    @Mock AppointmentRepository appointmentRepository;
    @Mock PatientRepository patientRepository;
    @Mock DoctorRepository doctorRepository;
    @Mock ProcedureRepository procedureRepository;

    AppointmentService sut;

    Patient patient;
    Doctor doctor;
    MedicalAppointment appointment;
    UUID appointmentId;

    @BeforeEach
    void setup() {
        sut = new AppointmentService(
                appointmentRepository,
                patientRepository,
                doctorRepository,
                procedureRepository,
                new InsuranceCoverageService()
        );

        patient = Patient.of("Carlos", "111.222.333-44", InsuranceType.BASIC);
        doctor  = Doctor.of("Dr. Silva", "Cardiologia", "CRM-SP 123456");
        appointment = MedicalAppointment.of(patient, doctor,
                LocalDateTime.of(2026, 4, 11, 10, 0));
        appointmentId = UUID.fromString("568374d2-990c-4395-9988-82e666a4f208");
    }

    @Test
    @DisplayName("#26 – deve fechar o atendimento e retornar com status CLOSED")
    void shouldCloseAndReturnStatusClosed() {
        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment))
                .thenReturn(appointment);

        MedicalAppointment result = sut.close(appointmentId);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.CLOSED);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    @DisplayName("#26 – deve lançar exceção ao tentar fechar atendimento inexistente")
    void shouldReturnExceptionWhenAppointmentDoesNotExistsInRepository() {
        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.close(appointmentId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("#26/#31 – calculateBill deve aplicar cobertura do convênio ao total bruto")
    void calculateBillShouldApplyInsuranceType() {
        Patient patientBasic = Patient.of("Carlos", "222", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. Ana", "Ortopedia", "CRM-22");
        MedicalAppointment appointment = MedicalAppointment.of(patientBasic, doctor,
                LocalDateTime.of(2026, 5, 1, 10, 0));

        Procedure proc = Procedure.of("Consulta", new Money(new BigDecimal("200.00")));
        appointment.addProcedure(AppointmentProcedure.of(proc, 1));
        appointment.close();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        Money bill = sut.calculateBill(appointmentId);
        assertThat(bill.getAmount()).isEqualByComparingTo("140.00");
    }
}