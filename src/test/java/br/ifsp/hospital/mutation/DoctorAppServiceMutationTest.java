package br.ifsp.hospital.mutation;

import br.ifsp.hospital.annotation.Mutation;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@UnitTest
@Mutation
@ExtendWith(MockitoExtension.class)
@DisplayName("Mutation – DoctorAppService")
public class DoctorAppServiceMutationTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorAppService sut;

    @Test
    @DisplayName("Deve retornar o médico salvo com sucesso na criação")
    void createValidDoctorShouldReturnSavedDoctor() {
        Doctor expectedDoctor = Doctor.of("Dr. Gregory House", "Diagnóstico", "CRM-12345");

        when(doctorRepository.save(any(Doctor.class))).thenReturn(expectedDoctor);

        Doctor result = sut.create("Dr. Gregory House", "Diagnóstico", "CRM-12345");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedDoctor);
    }
}