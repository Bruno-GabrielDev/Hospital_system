package br.ifsp.hospital.functional;

import br.ifsp.hospital.annotation.Functional;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@UnitTest
@Functional
@DisplayName("Functional – Invalidity")

 class InvalidityTest {

    @Nested
    @DisplayName("Patient – campos inválidos")
    class PatientInvalidity {

        @Test
        @DisplayName("PE – insuranceType nulo → NullPointerException")
        void pe_insuranceTypeNulo() {
            assertThatThrownBy(() -> Patient.of("Bruno", "111", null))
                    .isInstanceOf(NullPointerException.class);
        }


    }
    }
