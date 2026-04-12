package br.ifsp.hospital.domain;

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@TDD
@DisplayName("TDD – Patient")
class PatientTDDTest {

    @Test
    @DisplayName("#6 – restore deve retornar instância com todos os valores corretos")
    void shouldReturnPatientInstanceWithAllValuesCorrect() {
        UUID id = UUID.randomUUID();
        Patient restored = Patient.restore(id, "Ana", "999.999.999-99", InsuranceType.PREMIUM);

        assertThat(restored).isNotNull();
        assertThat(restored.getId()).isEqualTo(id);
        assertThat(restored.getName()).isEqualTo("Ana");
        assertThat(restored.getDocument()).isEqualTo("999.999.999-99");
        assertThat(restored.getInsuranceType()).isEqualTo(InsuranceType.PREMIUM);
    }
}