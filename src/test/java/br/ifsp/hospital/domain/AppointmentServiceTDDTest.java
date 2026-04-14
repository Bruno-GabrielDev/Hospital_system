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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
    @DisplayName("#10/67 – Deve lançar exceção ao tentar criar atendimento para paciente inexistente")
    void shouldThrowExceptionWhenCreatingAppointmentForNonExistentPatient() {
        UUID invalidPatientId = UUID.randomUUID();
        UUID validDoctorId = UUID.randomUUID();

        LocalDateTime validDate = LocalDateTime.of(2030, 5, 1, 10, 0);

        when(patientRepository.findById(invalidPatientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.create(invalidPatientId, validDoctorId, validDate))
                .isInstanceOf(EntityNotFoundException.class);
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
    @DisplayName("#32 – Deve impedir criação se o paciente já possui atendimento OPEN")
    void shouldBlockCreationIfPatientAlreadyHasOpenAppointment() {
        UUID validPatientId = UUID.randomUUID();
        UUID validDoctorId = UUID.randomUUID();
        LocalDateTime scheduledAt = LocalDateTime.of(2030, 5, 1, 10, 0);

        Patient patient = Patient.of("Ana", "123", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. Silva", "Cardiologia", "CRM-123");

        when(patientRepository.findById(validPatientId)).thenReturn(Optional.of(patient));

        MedicalAppointment openAppointment = MedicalAppointment.of(patient, doctor,
                LocalDateTime.of(2030, 4, 1, 10, 0));

        when(appointmentRepository.findByPatientIdAndStatus(validPatientId, AppointmentStatus.OPEN))
                .thenReturn(List.of(openAppointment));

        assertThatThrownBy(() -> sut.create(validPatientId, validDoctorId, scheduledAt))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("#34 – Procedimento em período de carência temporal não deve receber desconto de seguro")
    void shouldNotApplyDiscountIfProcedureIsInGracePeriodTemporal() {
        Patient patientBasic = Patient.of("Carlos", "222", InsuranceType.BASIC);
        patientBasic.setPlanEnrollmentDate(LocalDate.of(2030, 4, 20));

        LocalDateTime appointmentDate = LocalDateTime.of(2030, 4, 30, 10, 0);
        MedicalAppointment appt = MedicalAppointment.of(patientBasic, doctor, appointmentDate);

        Procedure p1 = Procedure.of("Exame Complexo", new Money(new BigDecimal("100.00")));
        p1.setGracePeriodDays(180);
        appt.addProcedure(AppointmentProcedure.of(p1, 1));
        p1.complete();

        Procedure p2 = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        p2.setGracePeriodDays(0);
        appt.addProcedure(AppointmentProcedure.of(p2, 1));
        p2.complete();

        appt.close();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appt));

        BillDetail bill = sut.calculateBill(appointmentId);

        assertThat(bill.grossTotal().getAmount()).isEqualByComparingTo("200.00");
        assertThat(bill.patientAmount().getAmount()).isEqualByComparingTo("170.00");
        assertThat(bill.insuranceAmount().getAmount()).isEqualByComparingTo("30.00");
    }

    @Test
    @DisplayName("#35 – Deve gerar duas faturas distintas (Paciente e Convênio) se houver seguro")
    void shouldGenerateTwoDistinctInvoicesIfInsuranceExists() {
        Patient patientBasic = Patient.of("Carlos", "222", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. Ana", "Ortopedia", "CRM-22");
        MedicalAppointment appointment = MedicalAppointment.of(patientBasic, doctor,
                LocalDateTime.of(2026, 5, 1, 10, 0));

        Procedure proc = Procedure.of("Consulta", new Money(new BigDecimal("200.00")));
        appointment.addProcedure(AppointmentProcedure.of(proc, 1));
        proc.complete();
        appointment.close();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        List<Invoice> invoices = sut.generateInvoices(appointmentId);
        assertThat(invoices).hasSize(2);

        Invoice patientInvoice = invoices.stream()
                .filter(i -> i.type() == InvoiceType.PATIENT)
                .findFirst()
                .orElseThrow();
        assertThat(patientInvoice.amount().getAmount()).isEqualByComparingTo("140.00");

        Invoice insuranceInvoice = invoices.stream()
                .filter(i -> i.type() == InvoiceType.INSURANCE)
                .findFirst()
                .orElseThrow();
        assertThat(insuranceInvoice.amount().getAmount()).isEqualByComparingTo("60.00");
    }

    @Test
    @DisplayName("#62 / #63 – Deve impedir criação se o médico já possuir atendimento no mesmo horário")
    void shouldBlockCreationIfDoctorIsUnavailable() {
        UUID validPatientId = UUID.randomUUID();
        UUID validDoctorId = UUID.randomUUID();

        LocalDateTime validDate = LocalDateTime.of(2030, 5, 1, 10, 0);

        when(patientRepository.findById(validPatientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientIdAndStatus(validPatientId, AppointmentStatus.OPEN))
                .thenReturn(Collections.emptyList());
        when(doctorRepository.findById(validDoctorId)).thenReturn(Optional.of(doctor));

        when(doctorScheduleValidator.isAvailable(doctor, validDate)).thenReturn(false);

        assertThatThrownBy(() -> sut.create(validPatientId, validDoctorId, validDate))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("#65 / #66 – Deve impedir criação se a data do atendimento estiver no passado")
    void shouldBlockCreationIfDateIsInThePast() {
        UUID validPatientId = UUID.randomUUID();
        UUID validDoctorId = UUID.randomUUID();
        LocalDateTime pastDate = LocalDateTime.of(2020, 1, 1, 10, 0);

        assertThatThrownBy(() -> sut.create(validPatientId, validDoctorId, pastDate))
                .isInstanceOf(IllegalArgumentException.class);
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

    @Test
    @DisplayName("Happy Path – Deve criar um atendimento com sucesso quando todos os dados são válidos")
    void shouldCreateAppointmentSuccessfully() {
        UUID validPatientId = UUID.randomUUID();
        UUID validDoctorId = UUID.randomUUID();
        LocalDateTime validDate = LocalDateTime.of(2030, 5, 1, 10, 0);

        when(patientRepository.findById(validPatientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientIdAndStatus(validPatientId, AppointmentStatus.OPEN))
                .thenReturn(Collections.emptyList());
        when(doctorRepository.findById(validDoctorId)).thenReturn(Optional.of(doctor));
        when(doctorScheduleValidator.isAvailable(doctor, validDate)).thenReturn(true);
        when(appointmentRepository.save(any(MedicalAppointment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MedicalAppointment result = sut.create(validPatientId, validDoctorId, validDate);

        assertThat(result).isNotNull();
        assertThat(result.getPatient()).isEqualTo(patient);
        assertThat(result.getDoctor()).isEqualTo(doctor);
        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.OPEN);
        assertThat(result.getScheduledAt()).isEqualTo(validDate);

        verify(appointmentRepository).save(any(MedicalAppointment.class));
    }
}