package br.ifsp.hospital.functional;

import br.ifsp.hospital.annotation.Functional;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Doctor;
import br.ifsp.hospital.domain.repository.DoctorRepository;
import br.ifsp.hospital.domain.service.DoctorAppService;
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
@DisplayName("Testes Funcionais - DoctorAppService")
class DoctorAppServiceFunctionalTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorAppService sut;

    @Test
    @DisplayName("Deve rejeitar a criação de médico com nome vazio ou nulo")
    void shouldRejectDoctorCreationWithEmptyName() {
        assertThatThrownBy(() -> sut.create(null, "Cardiologia", "CRM-12345"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nome do médico é obrigatório");

        assertThatThrownBy(() -> sut.create("   ", "Cardiologia", "CRM-12345"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nome do médico é obrigatório");

        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Deve rejeitar a criação de médico com especialidade vazia ou nula")
    void shouldRejectDoctorCreationWithEmptySpecialty() {
        assertThatThrownBy(() -> sut.create("Dr. Silva", "", "CRM-12345"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Especialidade é obrigatória");

        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Deve rejeitar a criação de médico com CRM vazio ou nulo")
    void shouldRejectDoctorCreationWithEmptyCrm() {
        assertThatThrownBy(() -> sut.create("Dr. Silva", "Cardiologia", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CRM é obrigatório");

        verify(doctorRepository, never()).save(any(Doctor.class));
    }
}