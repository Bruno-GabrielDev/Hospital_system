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
}