package br.ifsp.hospital.domain.model;

import br.ifsp.hospital.annotation.TDD;
import br.ifsp.hospital.annotation.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@TDD
@DisplayName("TDD – Doctor")
class DoctorTDDTest {

    @Test
    @DisplayName("#6 – restore deve retornar instância com todos os valores corretos")
    void shouldReturnDoctorInstanceWithCorrectValues() {
        UUID id = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        Doctor restored = Doctor.restore(id, "Dr. House", "Cardiologia", "CRM-SP 99999");

        assertThat(restored).isNotNull();
        assertThat(restored.getId()).isEqualTo(id);
        assertThat(restored.getName()).isEqualTo("Dr. House");
        assertThat(restored.getSpecialty()).isEqualTo("Cardiologia");
        assertThat(restored.getLicense()).isEqualTo("CRM-SP 99999");
    }
}