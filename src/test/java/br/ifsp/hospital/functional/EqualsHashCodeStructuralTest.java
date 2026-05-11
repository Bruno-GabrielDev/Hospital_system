package br.ifsp.hospital.functional;

import br.ifsp.hospital.annotation.Functional;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import br.ifsp.hospital.functional.utils.EqualsHashCodeScenario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@Functional
@DisplayName("Functional – Equals and HashCode")
class EqualsHashCodeStructuralTest {

    static Stream<EqualsHashCodeScenario> hashCodeOnlyScenarios() {
        UUID id1 = UUID.randomUUID();
        return Stream.of(
                new EqualsHashCodeScenario(
                        Patient.restore(id1, "Ana", "123", InsuranceType.PREMIUM),
                        Patient.restore(id1, "Ana", "123", InsuranceType.PREMIUM),
                        null, null
                ),
                new EqualsHashCodeScenario(
                        Procedure.restore(id1, "Consulta", new Money(BigDecimal.TEN)),
                        Procedure.restore(id1, "Consulta", new Money(BigDecimal.TEN)),
                        null, null
                ),
                new EqualsHashCodeScenario(
                        Doctor.restore(id1, "Dr. House", "Diagnóstico", "CRM123"),
                        Doctor.restore(id1, "Dr. House", "Diagnóstico", "CRM123"),
                        null, null
                )
        );
    }

    @ParameterizedTest
    @MethodSource("hashCodeOnlyScenarios")
    void shouldVerifyHashCodeContract(EqualsHashCodeScenario scenario) {
        assertThat(scenario.base().hashCode()).isEqualTo(scenario.matching().hashCode());
        assertThat(scenario.base().hashCode()).isEqualTo(scenario.base().hashCode());
    }


    static Stream<EqualsHashCodeScenario> fullContractScenarios() {
        UUID idApp = UUID.randomUUID();
        Procedure proc = Procedure.restore(UUID.randomUUID(), "Teste", new Money(BigDecimal.TEN));
        UUID idMedApp = UUID.randomUUID();

        Patient dummyPatient = Patient.restore(UUID.randomUUID(), "João", "111", InsuranceType.PREMIUM);
        Doctor dummyDoctor = Doctor.restore(UUID.randomUUID(), "Dr. House", "Clínico", "CRM111");
        LocalDateTime now = LocalDateTime.now();

        MedicalAppointment medAppBase = MedicalAppointment.restore(idMedApp, dummyPatient, dummyDoctor, now, now, AppointmentStatus.OPEN, 0, new ArrayList<>());
        MedicalAppointment medAppMatching = MedicalAppointment.restore(idMedApp, dummyPatient, dummyDoctor, now, now, AppointmentStatus.OPEN, 0, new ArrayList<>());
        MedicalAppointment medAppDifferent = MedicalAppointment.restore(UUID.randomUUID(), dummyPatient, dummyDoctor, now, now, AppointmentStatus.OPEN, 0, new ArrayList<>());

        return Stream.of(
                new EqualsHashCodeScenario(
                        AppointmentProcedure.restore(idApp, proc, 1),
                        AppointmentProcedure.restore(idApp, proc, 1),
                        AppointmentProcedure.restore(UUID.randomUUID(), proc, 1),
                        "Uma String Qualquer"
                ),
                new EqualsHashCodeScenario(
                        new Money(new BigDecimal("10.00")),
                        new Money(new BigDecimal("10.00")),
                        new Money(new BigDecimal("20.00")),
                        10.00
                ),
                new EqualsHashCodeScenario(
                        medAppBase,
                        medAppMatching,
                        medAppDifferent,
                        "Outro Tipo Aleatório"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("fullContractScenarios")
    void shouldVerifyFullEqualsAndHashCodeContract(EqualsHashCodeScenario scenario) {
        Object base = scenario.base();
        Object matching = scenario.matching();
        Object different = scenario.different();
        Object otherType = scenario.otherType();

        assertThat(base).isNotEqualTo(null);
        assertThat(base).isNotEqualTo(otherType);
        assertThat(base).isEqualTo(matching);
        assertThat(base).isNotEqualTo(different);
        assertThat(base.hashCode()).isEqualTo(matching.hashCode());
    }
}