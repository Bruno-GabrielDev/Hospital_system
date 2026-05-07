package br.ifsp.hospital.structural;

import br.ifsp.hospital.annotation.Structural;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@Structural
@ExtendWith(MockitoExtension.class)
class PatientAppServiceStructuralTest {

    @Mock PatientRepository repo;
    @InjectMocks PatientAppService sut;

    @Test
    @DisplayName("Should create patient")
    void shouldCreatePatient() {
        assertThatCode(() -> sut.create("Rhuan", "000.000.000-00", InsuranceType.BASIC))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should find a patient")
    void shouldFindAPatient() {
        UUID uuid = UUID.randomUUID();
        Patient patient = Patient.restore(uuid, "rr", "000.000.000-01", InsuranceType.BASIC);
        when(repo.findById(uuid)).thenReturn(Optional.of(patient));
        Patient result = sut.findById(uuid);

        assertThat(result).isNotNull();

        verify(repo, times(1)).findById(uuid);
    }


    @Test
    @DisplayName("Should find all patients")
    void shouldFindAllPatients() {
        assertThatCode(() -> sut.findAll())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should not create patient when name cpf is empty")
    void shouldNotCreatePatientWhenNameCpfIsEmpty() {
        assertThatThrownBy(() -> sut.create("Rhuan", "", InsuranceType.BASIC))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CPF é obrigatório");
    }


}