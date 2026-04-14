package br.ifsp.hospital.functional;

import br.ifsp.hospital.annotation.Functional;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Doctor;
import br.ifsp.hospital.domain.repository.AppointmentRepository;
import br.ifsp.hospital.domain.service.DefaultDoctorScheduleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@UnitTest
@Functional
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Funcionais - Análise de Valor Limite (Agenda)")
class DefaultDoctorScheduleValidatorFunctionalTest {

    @Mock AppointmentRepository appointmentRepository;
    @InjectMocks private DefaultDoctorScheduleValidator sut;
    private Doctor dummyDoctor;

    @BeforeEach
    void setUp() {
        dummyDoctor = mock(Doctor.class);
    }

    @ParameterizedTest(name = "Horário {0}:{1} deve retornar {2}")
    @CsvSource({
            "7, 59, false",
            "8, 0, true",
            "8, 1, true",
            "17, 59, true",
            "18, 0, false",
            "18, 1, false"
    })
    @DisplayName("Deve validar limites do horário comercial (08:00 às 17:59)")
    void shouldValidateWorkingHoursBoundaries(int hour, int minute, boolean expected) {
        LocalDateTime testTime = LocalDateTime.of(2030, 5, 6, hour, minute);

        boolean result = sut.isWithinWorkingHours(dummyDoctor, testTime);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest(name = "Data {0}-{1}-{2} (Ano-Mês-Dia) deve retornar {3}")
    @CsvSource({
            "2030, 5, 10, true",
            "2030, 5, 11, false",
            "2030, 5, 12, false",
            "2030, 5, 13, true"
    })
    @DisplayName("Deve validar limites dos dias da semana (Segunda a Sexta)")
    void shouldValidateDaysOfWeekBoundaries(int year, int month, int day, boolean expected) {
        LocalDateTime testTime = LocalDateTime.of(year, month, day, 10, 0);

        boolean result = sut.isWithinWorkingHours(dummyDoctor, testTime);

        assertThat(result).isEqualTo(expected);
    }
}