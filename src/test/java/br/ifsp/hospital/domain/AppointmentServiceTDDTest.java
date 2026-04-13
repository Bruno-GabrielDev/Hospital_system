package br.ifsp.hospital.domain;

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import br.ifsp.hospital.domain.repository.*;
import br.ifsp.hospital.domain.service.AppointmentService;
import br.ifsp.hospital.domain.service.DoctorScheduleValidator;
import br.ifsp.hospital.domain.service.InsuranceCoverageService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
    @Mock
    DoctorScheduleValidator doctorScheduleValidator;

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
                new InsuranceCoverageService(),
                doctorScheduleValidator
        );

        patient = Patient.of("Carlos", "111.222.333-44", InsuranceType.BASIC);
        doctor  = Doctor.of("Dr. Silva", "Cardiologia", "CRM-SP 123456");
        appointment = MedicalAppointment.of(patient, doctor,
                LocalDateTime.now().plusDays(1));
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

    @DisplayName("#26/31/23 Should calculate correct amounts based on insurance plan")
    @ParameterizedTest(name = " {0}: paciente paga {2}, convênio cobre {3}")
    @CsvSource({
            "BASIC,   Carlos, 140.00, 60.00",
            "PREMIUM, Ana,     60.00, 140.00",
            "NONE,    Pedro,  200.00,   0.00"
    })
    void shouldCalculateCorrectAmountsBasedOnInsurancePlan(
            InsuranceType type,
            String name,
            String expectedPatient,
            String expectedInsurance)
    {
        Patient patient = Patient.of(name, "000.000.000-00", type);
        Doctor doctor = Doctor.of("Dr. Silva", "Cardiologia", "CRM-SP 123456");
        MedicalAppointment appointment = MedicalAppointment.of(patient, doctor,
                LocalDateTime.of(2026, 5, 1, 10, 0));

        Procedure consulta = Procedure.of("Consulta", new Money(new BigDecimal("200.00")));
        appointment.addProcedure(AppointmentProcedure.of(consulta, 1));
        consulta.complete();

        appointment.close();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        BillDetail bill = sut.calculateBill(appointmentId);

        assertThat(bill.grossTotal().getAmount()).isEqualByComparingTo("200.00");
        assertThat(bill.patientAmount().getAmount()).isEqualByComparingTo(expectedPatient);
        assertThat(bill.insuranceAmount().getAmount()).isEqualByComparingTo(expectedInsurance);
        assertThat(bill.insuranceType()).isEqualTo(type);
    }

    @Test
    @DisplayName("#68/69 – Deve lançar exceção se o procedimento não existir ao adicionar")
    void shouldThrowExceptionWhenAddingProcedureThatDoesNotExist() {
        UUID invalidProcedureId = UUID.randomUUID();

        when(procedureRepository.findById(invalidProcedureId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.addProcedure(appointmentId, invalidProcedureId, 1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("#73 – Deve lançar exceção se o ID do procedimento for nulo")
    void shouldThrowExceptionWhenProcedureIdIsNull() {
        assertThatThrownBy(() -> sut.addProcedure(appointmentId, null, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("#74/75 – Deve lançar exceção se o atendimento não existir ao adicionar procedimento")
    void shouldThrowExceptionWhenAddingProcedureToNonExistentAppointment() {
        UUID validProcedureId = UUID.randomUUID();
        Procedure dummyProc = Procedure.of("Raio-X", new Money(new BigDecimal("150.00")));

        when(procedureRepository.findById(validProcedureId)).thenReturn(Optional.of(dummyProc));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.addProcedure(appointmentId, validProcedureId, 1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Deve cancelar o atendimento, salvar no repositório e liberar a agenda (#49 / #43)")
    void shouldCancelAppointmentAndSave() {
        String reason = "Paciente teve um imprevisto";
        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment))
                .thenReturn(appointment);

        MedicalAppointment result = sut.cancel(appointmentId, reason);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.CANCELED);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    @DisplayName("Deve reagendar o atendimento, salvar no repositório e liberar a agenda antiga (#57 / #50)")
    void shouldRescheduleAppointmentAndSave() {
        LocalDateTime newScheduledAt = LocalDateTime.now().plusDays(5);

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment))
                .thenReturn(appointment);

        when(doctorScheduleValidator.isWithinWorkingHours(doctor, newScheduledAt)).thenReturn(true);
        when(doctorScheduleValidator.isAvailable(doctor, newScheduledAt)).thenReturn(true);

        MedicalAppointment result = sut.reschedule(appointmentId, newScheduledAt);

        assertThat(result).isNotNull();
        assertThat(result.getScheduledAt()).isEqualTo(newScheduledAt);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cancelar atendimento inexistente na base")
    void shouldThrowExceptionWhenCancelingNonExistentAppointment() {
        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.cancel(appointmentId, "Motivo válido"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Atendimento não encontrado");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar reagendar atendimento inexistente na base")
    void shouldThrowExceptionWhenReschedulingNonExistentAppointment() {
        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.reschedule(appointmentId, LocalDateTime.now().plusDays(2)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Atendimento não encontrado");
    }
}