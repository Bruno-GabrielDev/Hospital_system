package br.ifsp.hospital.mutation;

import br.ifsp.hospital.annotation.Mutation;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Doctor;
import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.MedicalAppointment;
import br.ifsp.hospital.domain.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@Mutation
@DisplayName("Mutation – MedicalAppointment")
public class MedicalAppointmentMutationTest {

    @Test
    @DisplayName("Deve retornar false para isReceiptIssued em um agendamento recém-criado")
    void isReceiptIssuedNewAppointmentShouldReturnFalse() {
        Patient patient = Patient.of("John", "123", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. House", "Cardio", "CRM123");
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);

        MedicalAppointment appointment = MedicalAppointment.of(patient, doctor, scheduledAt);

        assertThat(appointment.isReceiptIssued()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false para isRefunded em um agendamento recém-criado")
    void isRefundedNewAppointmentShouldReturnFalse() {
        Patient patient = Patient.of("John", "123", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. House", "Cardio", "CRM123");
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);

        MedicalAppointment appointment = MedicalAppointment.of(patient, doctor, scheduledAt);

        assertThat(appointment.isRefunded()).isFalse();
    }

    @Test
    @DisplayName("Deve gerar hashCodes diferentes para instâncias distintas (IDs diferentes)")
    void hashCodeDifferentAppointmentsShouldReturnDifferentHashCodes() {
        Patient patient = Patient.of("John", "123", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. House", "Cardio", "CRM123");
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);

        MedicalAppointment appointment1 = MedicalAppointment.of(patient, doctor, scheduledAt);
        MedicalAppointment appointment2 = MedicalAppointment.of(patient, doctor, scheduledAt);

        assertThat(appointment1.hashCode()).isNotEqualTo(appointment2.hashCode());
    }

    @Test
    @DisplayName("Deve atribuir ID e data de criação ao criar novo atendimento (#80)")
    void shouldAssignIdAndCreatedAtWhenCreatedUsingOf() {
        Patient patient = Patient.of("John", "123", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. House", "Cardio", "CRM123");
        MedicalAppointment appointment = MedicalAppointment.of(patient, doctor, LocalDateTime.now().plusDays(1));
        assertThat(appointment.getId()).isNotNull();
        assertThat(appointment.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Não deve processar estorno ao cancelar atendimento não faturado (#83)")
    void shouldNotProcessRefundsWhenCancelingNonBilledAppointment() {
        Patient patient = Patient.of("John", "123", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. House", "Cardio", "CRM123");
        MedicalAppointment appointment = MedicalAppointment.of(patient, doctor, LocalDateTime.now().plusDays(1));
        appointment.cancel("PATIENT_REQUEST");

        assertThat(appointment.isRefunded()).isFalse();
    }

    @Test
    @DisplayName("Deve permitir cancelamento com exatamente 3 horas de antecedência (#84)")
    void shouldAllowCancellationWithExactlyThreeHoursNotice() {
        Patient patient = Patient.of("John", "123", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. House", "Cardio", "CRM123");
        LocalDateTime scheduledAt = LocalDateTime.now().plusHours(3).plusMinutes(1);
        MedicalAppointment exactAppointment = MedicalAppointment.of(patient, doctor, scheduledAt);

        assertThat(exactAppointment.getStatus()).isEqualTo(br.ifsp.hospital.domain.model.AppointmentStatus.OPEN);
        exactAppointment.cancel("PATIENT_REQUEST");
        assertThat(exactAppointment.getStatus()).isEqualTo(br.ifsp.hospital.domain.model.AppointmentStatus.CANCELED);
    }

    @Test
    @DisplayName("Deve bloquear cancelamento se o limite de 3 horas for alterado para 4 (#85)")
    void shouldBlockCancellationIfLimitWasFourHoursInsteadOfThree() {
        Patient patient = Patient.of("John", "123", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. House", "Cardio", "CRM123");
        LocalDateTime scheduledAt = LocalDateTime.now().plusHours(3).plusMinutes(30);
        MedicalAppointment marginAppointment = MedicalAppointment.of(patient, doctor, scheduledAt);

        marginAppointment.cancel("PATIENT_REQUEST");
        assertThat(marginAppointment.getStatus()).isEqualTo(br.ifsp.hospital.domain.model.AppointmentStatus.CANCELED);
    }

    @Test
    @DisplayName("Deve gerar relatório com conteúdo completo e formatado (#86)")
    void shouldGenerateReportWithCompleteInformation() {
        Patient patient = Patient.of("John Doe", "123", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. House", "Cardiology", "54321");
        MedicalAppointment appointment = MedicalAppointment.of(patient, doctor, LocalDateTime.now().plusDays(1));

        br.ifsp.hospital.domain.model.Procedure proc = br.ifsp.hospital.domain.model.Procedure.of("Hemograma", new br.ifsp.hospital.domain.model.Money(new java.math.BigDecimal("50.00")));
        appointment.addProcedure(br.ifsp.hospital.domain.model.AppointmentProcedure.of(proc, 2));
        proc.complete();
        appointment.close();
        appointment.markAsBilled();

        String report = appointment.generateMedicalReport();

        String expectedDate = appointment.getScheduledAt().toString();
        String expectedReport = "=== RELATÓRIO MÉDICO ===\n" +
                "Paciente: John Doe\n" +
                "Médico: Dr. House - Cardiology\n" +
                "Data: " + expectedDate + "\n" +
                "Procedimentos realizados:\n" +
                "- Hemograma (Qtd: 2)\n";

        assertThat(report).isEqualTo(expectedReport);
    }

    @Test
    @DisplayName("Deve seguir o contrato do hashCode (#87)")
    void shouldFollowHashCodeContract() {
        Patient patient = Patient.of("John", "123", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. House", "Cardio", "CRM123");
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);
        MedicalAppointment appointment = MedicalAppointment.of(patient, doctor, scheduledAt);

        MedicalAppointment sameAppointment = MedicalAppointment.restore(
                appointment.getId(), appointment.getPatient(), appointment.getDoctor(),
                appointment.getScheduledAt(), appointment.getCreatedAt(),
                appointment.getStatus(), appointment.getRescheduleCount(),
                java.util.List.of()
        );

        assertThat(appointment.hashCode()).isEqualTo(sameAppointment.hashCode());
        assertThat(appointment.hashCode()).isNotZero();
    }

    @Test
    @DisplayName("#6 – restore deve restaurar lista de procedimentos corretamente (#100)")
    void shouldRestoreProceduresListCorrectly() {
        Patient patient = Patient.of("John", "123", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. House", "Cardio", "CRM123");
        br.ifsp.hospital.domain.model.Procedure p = br.ifsp.hospital.domain.model.Procedure.of("RX", new br.ifsp.hospital.domain.model.Money(new java.math.BigDecimal("100.00")));
        br.ifsp.hospital.domain.model.AppointmentProcedure ap = br.ifsp.hospital.domain.model.AppointmentProcedure.of(p, 1);
        java.util.List<br.ifsp.hospital.domain.model.AppointmentProcedure> procedures = java.util.List.of(ap);

        MedicalAppointment restored = MedicalAppointment.restore(
                java.util.UUID.randomUUID(), patient, doctor, LocalDateTime.now(), LocalDateTime.now(),
                br.ifsp.hospital.domain.model.AppointmentStatus.OPEN, 0, procedures
        );

        assertThat(restored.getProcedures()).hasSize(1);
        assertThat(restored.getProcedures().get(0)).isEqualTo(ap);
    }
    }