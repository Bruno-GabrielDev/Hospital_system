package br.ifsp.hospital.structural;

import br.ifsp.hospital.annotation.Structural;
import br.ifsp.hospital.annotation.UnitTest;
import br.ifsp.hospital.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
@Structural
@DisplayName("Structural – MedicalAppointment")
class MedicalAppointmentStructuralTest {

    private MedicalAppointment criar() {
        Patient p = Patient.of("Bruno", "111", InsuranceType.BASIC);
        Doctor d = Doctor.of("Dr. Silva", "Cardio", "CRM-001");
        return MedicalAppointment.of(p, d, LocalDateTime.now().plusDays(1));
    }

    @Test
    @DisplayName("generateMedicalReport – status não BILLED → IllegalStateException")
    void report_statusNaoBilled_lancaExcecao() {
        MedicalAppointment a = criar();
        assertThatThrownBy(() -> a.generateMedicalReport())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("generateMedicalReport – status BILLED → retorna relatório")
    void report_statusBilled_retornaRelatorio() {
        MedicalAppointment a = criar();
        Procedure proc = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        a.addProcedure(AppointmentProcedure.of(proc, 1));
        proc.complete();
        a.close();
        a.markAsBilled();

        String relatorio = a.generateMedicalReport();

        assertThat(relatorio).contains("RELATÓRIO MÉDICO");
        assertThat(relatorio).contains("Bruno");
        assertThat(relatorio).contains("Consulta");
    }

    @Test
    @DisplayName("cancel – em status BILLED dispara refund")
    void cancel_emBilled_disparaRefund() {
        MedicalAppointment a = criar();
        Procedure proc = Procedure.of("Consulta", new Money(new BigDecimal("100.00")));
        a.addProcedure(AppointmentProcedure.of(proc, 1));
        proc.complete();
        a.close();
        a.markAsBilled();

        a.cancel("Imprevisto");

        assertThat(a.getStatus()).isEqualTo(AppointmentStatus.CANCELED);
        assertThat(a.isRefunded()).isTrue();
    }

    @Test
    @DisplayName("equals e hashCode – invocação para cobertura")
    void equalsEHashCode_invocacao() {
        MedicalAppointment a = criar();
        assertThat(a.equals(null)).isFalse();
        assertThat(a.hashCode()).isEqualTo(0);
    }

    @Test
    @DisplayName("markAsBilled – status OPEN → IllegalStateException")
    void markAsBilled_statusOpen_lancaExcecao() {
        MedicalAppointment a = criar();
        assertThatThrownBy(() -> a.markAsBilled())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("fechado");
    }

    @Test
    @DisplayName("cancel – reason null → IllegalArgumentException")
    void cancel_reasonNull_lancaExcecao() {
        MedicalAppointment a = criar();
        assertThatThrownBy(() -> a.cancel(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}