package br.ifsp.hospital.structural;

import br.ifsp.hospital.annotation.Structural;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.Money;
import br.ifsp.hospital.domain.model.Procedure;
import br.ifsp.hospital.domain.repository.ProcedureRepository;
import br.ifsp.hospital.domain.service.ProcedureAppService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@UnitTest
@Structural
@ExtendWith(MockitoExtension.class)
@DisplayName("Structural – ProcedureAppService")
class ProcedureAppServiceStructuralTest {

    @Mock private ProcedureRepository procedureRepository;
    @InjectMocks private ProcedureAppService sut;

    @Test
    @DisplayName("findById – procedimento existe → retorna procedure")
    void findById_existe_retornaProcedure() {
        UUID id = UUID.randomUUID();
        Procedure proc = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        when(procedureRepository.findById(id)).thenReturn(Optional.of(proc));

        Procedure result = sut.findById(id);

        assertThat(result).isEqualTo(proc);
    }

    @Test
    @DisplayName("findById – procedimento não existe → EntityNotFoundException")
    void findById_naoExiste_lancaExcecao() {
        UUID id = UUID.randomUUID();
        when(procedureRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("findAll – retorna lista de procedimentos")
    void findAll_retornaLista() {
        Procedure proc = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        when(procedureRepository.findAll()).thenReturn(List.of(proc));

        assertThat(sut.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("create – dados válidos → procedure salvo")
    void create_dadosValidos_salvaProcedure() {
        Money price = new Money(new BigDecimal("100.00"));
        Procedure proc = Procedure.of("Consulta", price);
        when(procedureRepository.save(org.mockito.ArgumentMatchers.any(Procedure.class)))
                .thenReturn(proc);

        Procedure result = sut.create("Consulta", price);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Consulta");
    }
}