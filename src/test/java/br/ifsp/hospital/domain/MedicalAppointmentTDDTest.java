package br.ifsp.hospital.domain;


import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;


@UnitTest
@TDD
@DisplayName("TDD – MedicalAppointment")

class MedicalAppointmentTDDTest {

    private Patient patient;
    private Doctor  doctor;
    private LocalDateTime future;

    @BeforeEach
    void setUp() {
    }


        @Test
    @DisplayName("#33 – resultado arredondado para 2 casas decimais")
    void t33_deveArredondarParaDuasCasas() {
        Patient patient = Patient.of("Bruno", "111", InsuranceType.BASIC);
        Doctor doctor = Doctor.of("Dr. Silva", "Cardiologia", "CRM-001");
        MedicalAppointment appt = MedicalAppointment.of(patient, doctor,
                LocalDateTime.now().plusDays(1));
        appt.addProcedure(AppointmentProcedure.of(
                Procedure.of("Exame", new Money(new BigDecimal("0.005"))), 1));

        Money result = appt.calculateGrossTotal();

        assertThat(result.getAmount().scale()).isEqualTo(2);

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MedicalAppointmentTDDTest {

    @Test
    @UnitTest
    @TDD
    void shouldCancelOpenAppointment(){
        Patient dummyPatient = Patient.of("John Doe", "123456789", InsuranceType.BASIC);
        Doctor dummyDoctor = Doctor.of("John Doe", "Cardiology", "54321");
        LocalDateTime dummyScheduledAt = LocalDateTime.now().plusDays(1);

        MedicalAppointment appointment = MedicalAppointment.of(dummyPatient, dummyDoctor, dummyScheduledAt);

        appointment.cancel();
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELED);

    }
}
