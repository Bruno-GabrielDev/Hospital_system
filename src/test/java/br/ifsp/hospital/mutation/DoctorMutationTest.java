package br.ifsp.hospital.mutation;

import br.ifsp.hospital.annotation.Mutation;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Doctor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@Mutation
@DisplayName("Mutation – Doctor")
public class DoctorMutationTest {

    @Test
    @DisplayName("Deve gerar hashCodes diferentes para instâncias distintas (IDs diferentes)")
    void hashCodeDifferentDoctorsShouldReturnDifferentHashCodes() {
        Doctor doctor1 = Doctor.of("Dr. House", "Diagnóstico", "CRM-111");
        Doctor doctor2 = Doctor.of("Dr. House", "Diagnóstico", "CRM-111");

        assertThat(doctor1.hashCode()).isNotEqualTo(doctor2.hashCode());
    }
}