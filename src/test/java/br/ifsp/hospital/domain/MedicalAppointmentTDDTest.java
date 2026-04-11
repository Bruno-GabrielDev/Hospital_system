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
    }
}
