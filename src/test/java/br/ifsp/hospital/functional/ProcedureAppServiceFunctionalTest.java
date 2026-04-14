package br.ifsp.hospital.functional;

import br.ifsp.hospital.annotation.Functional;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Money;
import br.ifsp.hospital.domain.model.Procedure;
import br.ifsp.hospital.domain.repository.ProcedureRepository;
import br.ifsp.hospital.domain.service.ProcedureAppService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@UnitTest
@Functional
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Funcionais - ProcedureAppService")
class ProcedureAppServiceFunctionalTest {

    @Mock
    private ProcedureRepository procedureRepository;

    @InjectMocks
    private ProcedureAppService sut;

    @Test
    @DisplayName("Deve rejeitar a criação de procedimento com nome vazio ou nulo")
    void shouldRejectProcedureCreationWithEmptyName() {
        Money validPrice = new Money(new BigDecimal("150.00"));

        assertThatThrownBy(() -> sut.create(null, validPrice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nome do procedimento é obrigatório");

        assertThatThrownBy(() -> sut.create("   ", validPrice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nome do procedimento é obrigatório");

        verify(procedureRepository, never()).save(any(Procedure.class));
    }

    @Test
    @DisplayName("Deve rejeitar a criação de procedimento com preço nulo")
    void shouldRejectProcedureCreationWithNullPrice() {
        assertThatThrownBy(() -> sut.create("Raio-X", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Preço é obrigatório");

        verify(procedureRepository, never()).save(any(Procedure.class));
    }
}