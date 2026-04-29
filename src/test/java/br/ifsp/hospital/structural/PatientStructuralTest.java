package br.ifsp.hospital.structural;

import br.ifsp.hospital.annotation.Structural;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
@Structural
@DisplayName("Structural – Patient")
class PatientStructuralTest {

    @Test
    @DisplayName("of – name null → IllegalArgumentException")
    void of_nameNull_lancaExcecao() {
        assertThatThrownBy(() -> Patient.of(null, "111", InsuranceType.BASIC))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("of – name só com espaços → IllegalArgumentException")
    void of_nameEmBranco_lancaExcecao() {
        assertThatThrownBy(() -> Patient.of("   ", "111", InsuranceType.BASIC))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("equals – objeto nulo → false")
    void equals_objetoNulo_retornaFalse() {
        Patient p = Patient.of("Bruno", "111", InsuranceType.BASIC);
        assertThat(p.equals(null)).isFalse();
    }

    @Test
    @DisplayName("equals – classe diferente → false")
    void equals_classeDiferente_retornaFalse() {
        Patient p = Patient.of("Bruno", "111", InsuranceType.BASIC);
        assertThat(p.equals("não é Patient")).isFalse();
    }

    @Test
    @DisplayName("equals – mesmo ID → true")
    void equals_mesmoId_retornaTrue() {
        Patient p1 = Patient.of("Bruno", "111", InsuranceType.BASIC);
        Patient p2 = Patient.restore(p1.getId(), "Outro", "999", InsuranceType.PREMIUM);
        assertThat(p1.equals(p2)).isTrue();
    }

    @Test
    @DisplayName("equals – IDs diferentes → false")
    void equals_idsDiferentes_retornaFalse() {
        Patient p1 = Patient.of("Bruno", "111", InsuranceType.BASIC);
        Patient p2 = Patient.of("Bruno", "111", InsuranceType.BASIC);
        assertThat(p1.equals(p2)).isFalse();
    }
}