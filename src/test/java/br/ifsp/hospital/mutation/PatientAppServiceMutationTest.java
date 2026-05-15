package br.ifsp.hospital.mutation;

import br.ifsp.hospital.annotation.Mutation;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Patient;
import br.ifsp.hospital.domain.repository.PatientRepository;
import br.ifsp.hospital.domain.service.PatientAppService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@UnitTest
@Mutation
@ExtendWith(MockitoExtension.class)
@DisplayName("Mutation – PatientAppService")
public class PatientAppServiceMutationTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientAppService sut;

    @Test
    @DisplayName("Deve retornar o paciente salvo com sucesso na criação")
    void createValidPatientShouldReturnSavedPatient() {
        Patient expectedPatient = Patient.of("John Doe", "111.222.333-44", InsuranceType.BASIC);

        when(patientRepository.save(any(Patient.class))).thenReturn(expectedPatient);

        Patient result = sut.create("John Doe", "111.222.333-44", InsuranceType.BASIC);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedPatient);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao buscar paciente inexistente por ID")
    void findByIdPatientNotFoundShouldThrowException() {
        java.util.UUID patientId = java.util.UUID.randomUUID();

        // Força o repositório a retornar um Optional vazio
        when(patientRepository.findById(patientId)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> sut.findById(patientId))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class)
                .hasMessageContaining("Paciente não encontrado");
    }
}