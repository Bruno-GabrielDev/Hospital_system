package br.ifsp.hospital.structural;

import br.ifsp.hospital.annotation.Structural;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Doctor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
@Structural
@DisplayName("Structural – Doctor")
class DoctorStructuralTest {

    @Test
    @DisplayName("of – license null → IllegalArgumentException")
    void of_licenseNull_lancaExcecao() {
        assertThatThrownBy(() -> Doctor.of("Dr. Silva", "Cardio", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("of – license só com espaços → IllegalArgumentException")
    void of_licenseEmBranco_lancaExcecao() {
        assertThatThrownBy(() -> Doctor.of("Dr. Silva", "Cardio", "   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("equals – objeto nulo → false")
    void equals_objetoNulo_retornaFalse() {
        Doctor d = Doctor.of("Dr. Silva", "Cardio", "CRM-001");
        assertThat(d.equals(null)).isFalse();
    }

    @Test
    @DisplayName("equals – classe diferente → false")
    void equals_classeDiferente_retornaFalse() {
        Doctor d = Doctor.of("Dr. Silva", "Cardio", "CRM-001");
        assertThat(d.equals("não é Doctor")).isFalse();
    }

    @Test
    @DisplayName("equals – mesmo ID → true")
    void equals_mesmoId_retornaTrue() {
        Doctor d1 = Doctor.of("Dr. Silva", "Cardio", "CRM-001");
        Doctor d2 = Doctor.restore(d1.getId(), "Outro Nome", "Outra", "CRM-999");
        assertThat(d1.equals(d2)).isTrue();
    }

    @Test
    @DisplayName("equals – IDs diferentes → false")
    void equals_idsDiferentes_retornaFalse() {
        Doctor d1 = Doctor.of("Dr. Silva", "Cardio", "CRM-001");
        Doctor d2 = Doctor.of("Dr. Silva", "Cardio", "CRM-002");
        assertThat(d1.equals(d2)).isFalse();
    }
}