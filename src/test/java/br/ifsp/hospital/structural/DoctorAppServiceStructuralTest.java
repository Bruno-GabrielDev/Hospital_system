package br.ifsp.hospital.structural;

import br.ifsp.hospital.annotation.Structural;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Doctor;
import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Patient;
import br.ifsp.hospital.domain.repository.DoctorRepository;
import br.ifsp.hospital.domain.repository.PatientRepository;
import br.ifsp.hospital.domain.service.DoctorAppService;
import br.ifsp.hospital.domain.service.PatientAppService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@Structural
@ExtendWith(MockitoExtension.class)
@DisplayName("Doctor Service - Structural")
class DoctorAppServiceStructuralTest {

    @Mock DoctorRepository repo;
    @InjectMocks DoctorAppService sut;

    @Test
    @DisplayName("Should create doctor")
    void shouldCreateDoctor() {
        assertThatCode(() -> sut.create("Rhuan", "Sexology", "023"))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("invalidCreateScenarios")
    @DisplayName("Should throw error when create is invalid")
    void shouldThrowErrorWhenCreateIsInvalid(String specialty, String crm, String message ) {
        assertThatThrownBy(() -> sut.create("Rhuan", specialty, crm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(message);
    }

    private static Stream<Arguments> invalidCreateScenarios() {
        return Stream.of(
                Arguments.of(null, "00000", "Especialidade é obrigatória"),
                Arguments.of("doctor", null, "CRM é obrigatório"),
                Arguments.of("doctor", "", "CRM é obrigatório")
        );
    }

    @Test
    @DisplayName("Should find all doctors")
    void shouldFindAllDoctors() {
        assertThatCode(() -> sut.findAll()).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should find a doctor")
    void shouldFindADoctor() {
        UUID uuid = UUID.randomUUID();
        Doctor patient = Doctor.restore(uuid, "dd", "teste", "test");
        when(repo.findById(uuid)).thenReturn(Optional.of(patient));
        Doctor result = sut.findById(uuid);

        assertThat(result).isNotNull();

        verify(repo, times(1)).findById(uuid);
    }

}