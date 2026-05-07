package br.ifsp.hospital.structural;

import br.ifsp.hospital.annotation.Structural;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import br.ifsp.hospital.domain.repository.*;
import br.ifsp.hospital.domain.service.AppointmentService;
import br.ifsp.hospital.domain.service.DoctorScheduleValidator;
import br.ifsp.hospital.domain.service.InsuranceCoverageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@UnitTest
@Structural
@ExtendWith(MockitoExtension.class)
@DisplayName("Structural – AppointmentService")
class AppointmentServiceStructuralTest {

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private ProcedureRepository procedureRepository;
    @Mock private DoctorScheduleValidator doctorScheduleValidator;

    private AppointmentService sut;

    private Patient patient;
    private Doctor doctor;
    private MedicalAppointment appointment;
    private UUID appointmentId;

    @BeforeEach
    void setUp() {
        sut = new AppointmentService(
                appointmentRepository, patientRepository, doctorRepository,
                procedureRepository, new InsuranceCoverageService(), doctorScheduleValidator);

        patient = Patient.of("Bruno", "111", InsuranceType.BASIC);
        doctor = Doctor.of("Dr. Silva", "Cardio", "CRM-001");
        appointment = MedicalAppointment.of(patient, doctor, LocalDateTime.now().plusDays(1));
        appointmentId = UUID.randomUUID();
    }

    @Test
    @DisplayName("findById – atendimento existe → retorna appointment")
    void findById_existe_retornaAppointment() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        assertThat(sut.findById(appointmentId)).isSameAs(appointment);
    }

    @Test
    @DisplayName("findAll – retorna lista de atendimentos")
    void findAll_retornaLista() {
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));
        assertThat(sut.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("findByPatient – retorna lista por paciente")
    void findByPatient_retornaLista() {
        UUID patientId = UUID.randomUUID();
        when(appointmentRepository.findByPatientId(patientId)).thenReturn(List.of(appointment));
        assertThat(sut.findByPatient(patientId)).hasSize(1);
    }

    @Test
    @DisplayName("findByDoctor – retorna lista por médico")
    void findByDoctor_retornaLista() {
        UUID doctorId = UUID.randomUUID();
        when(appointmentRepository.findByDoctorId(doctorId)).thenReturn(List.of(appointment));
        assertThat(sut.findByDoctor(doctorId)).hasSize(1);
    }

    @Test
    @DisplayName("markAsBilled – atendimento existe → muda para BILLED")
    void markAsBilled_existe_mudaParaBilled() {
        Procedure proc = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        appointment.addProcedure(AppointmentProcedure.of(proc, 1));
        proc.complete();
        appointment.close();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        MedicalAppointment result = sut.markAsBilled(appointmentId);

        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.BILLED);
    }

    @Test
    @DisplayName("markAsBilled – atendimento não existe → EntityNotFoundException")
    void markAsBilled_naoExiste_lancaExcecao() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> sut.markAsBilled(appointmentId))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class);
    }

    @Test
    @DisplayName("addProcedure – dados válidos → procedimento adicionado")
    void addProcedure_dadosValidos_adicionaProcedimento() {
        UUID procedureId = UUID.randomUUID();
        Procedure proc = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));

        when(procedureRepository.findById(procedureId)).thenReturn(Optional.of(proc));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        MedicalAppointment result = sut.addProcedure(appointmentId, procedureId, 2);

        assertThat(result).isNotNull();
        assertThat(result.getProcedures()).hasSize(1);
    }

    @Test
    @DisplayName("generateInvoices – paciente particular (NONE) → apenas fatura do paciente")
    void generateInvoices_pacienteParticular_apenasFaturaPaciente() {
        Patient particular = Patient.of("Carlos", "999", InsuranceType.NONE);
        MedicalAppointment apptParticular = MedicalAppointment.of(particular, doctor,
                LocalDateTime.now().plusDays(1));

        Procedure proc = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        apptParticular.addProcedure(AppointmentProcedure.of(proc, 1));
        proc.complete();
        apptParticular.close();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(apptParticular));

        List<Invoice> invoices = sut.generateInvoices(appointmentId);

        assertThat(invoices).hasSize(1);
        assertThat(invoices.get(0).type()).isEqualTo(InvoiceType.PATIENT);
    }
}