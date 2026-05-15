package br.ifsp.hospital.mutation;

import br.ifsp.hospital.annotation.Mutation;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@Mutation
@DisplayName("Mutation – Patient")
public class PatientMutationTest {

    @Test
    @DisplayName("Deve gerar hashCodes diferentes para instâncias distintas (IDs diferentes)")
    void hashCodeDifferentPatientsShouldReturnDifferentHashCodes() {
        Patient patient1 = Patient.of("John Doe", "111.222.333-44", InsuranceType.BASIC);
        Patient patient2 = Patient.of("John Doe", "111.222.333-44", InsuranceType.BASIC);

        assertThat(patient1.hashCode()).isNotEqualTo(patient2.hashCode());
    }

    @Test
    @DisplayName("Deve ter data de inscrição no plano de 5 anos atrás por padrão (#95)")
    void shouldHaveDefaultPlanEnrollmentDateOfFiveYearsAgo() {
        Patient patient = Patient.of("Ana", "999.999.999-99", InsuranceType.BASIC);
        java.time.LocalDate expectedDate = java.time.LocalDate.now().minusYears(5);
        assertThat(patient.getPlanEnrollmentDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("Deve seguir o contrato do hashCode (#96)")
    void shouldFollowHashCodeContract() {
        java.util.UUID id = java.util.UUID.randomUUID();
        Patient p1 = Patient.restore(id, "Ana", "111", InsuranceType.BASIC);
        Patient p2 = Patient.restore(id, "Ana", "111", InsuranceType.BASIC);

        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
        assertThat(p1.hashCode()).isNotZero();
    }
    }