package br.ifsp.hospital.functional;

import br.ifsp.hospital.annotation.Functional;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Patient;
import br.ifsp.hospital.domain.repository.PatientRepository;
import br.ifsp.hospital.domain.service.PatientAppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@UnitTest
@Functional
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Funcionais - PatientAppService")
class PatientAppServiceFunctionalTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientAppService sut;

    @Test
    @DisplayName("Deve rejeitar a criação de paciente com nome vazio ou nulo")
    void shouldRejectPatientCreationWithEmptyName() {
        assertThatThrownBy(() -> sut.create(null, "111.222.333-44", InsuranceType.BASIC))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nome não pode ser vazio");

        assertThatThrownBy(() -> sut.create("   ", "111.222.333-44", InsuranceType.BASIC))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nome não pode ser vazio");

        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    @DisplayName("Deve rejeitar a criação de paciente com CPF nulo")
    void shouldRejectPatientCreationWithNullCpf() {
        assertThatThrownBy(() -> sut.create("Carlos", null, InsuranceType.BASIC))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CPF é obrigatório");

        verify(patientRepository, never()).save(any(Patient.class));
    }
}