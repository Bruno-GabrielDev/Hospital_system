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
}