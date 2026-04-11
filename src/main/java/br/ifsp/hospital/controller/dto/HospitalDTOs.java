package br.ifsp.hospital.controller.dto;

import br.ifsp.hospital.domain.model.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/** Contêiner de todos os DTOs REST. */
public final class HospitalDTOs {

    private HospitalDTOs() {}

    // -----------------------------------------------------------------------
    // Patient
    // -----------------------------------------------------------------------

    public record PatientRequest(
            @Schema(example = "Bruno Lima")    String name,
            @Schema(example = "111.222.333-44") String document,
            @Schema(example = "BASIC")         InsuranceType insuranceType
    ) {}

    public record PatientResponse(UUID id, String name, String document, InsuranceType insuranceType) {
        public static PatientResponse from(Patient p) {
            return new PatientResponse(p.getId(), p.getName(), p.getDocument(), p.getInsuranceType());
        }
    }

    // -----------------------------------------------------------------------
    // Doctor
    // -----------------------------------------------------------------------

    public record DoctorRequest(
            @Schema(example = "Dr. Silva")      String name,
            @Schema(example = "Cardiologia")    String specialty,
            @Schema(example = "CRM-SP 123456")  String license
    ) {}

    public record DoctorResponse(UUID id, String name, String specialty, String license) {
        public static DoctorResponse from(Doctor d) {
            return new DoctorResponse(d.getId(), d.getName(), d.getSpecialty(), d.getLicense());
        }
    }

    // -----------------------------------------------------------------------
    // Procedure
    // -----------------------------------------------------------------------

    public record ProcedureRequest(
            @Schema(example = "Consulta") String name,
            @Schema(example = "200.00")   BigDecimal cost
    ) {}

    public record ProcedureResponse(UUID id, String name, BigDecimal cost) {
        public static ProcedureResponse from(Procedure p) {
            return new ProcedureResponse(p.getId(), p.getName(), p.getCost().getAmount());
        }
    }

    // -----------------------------------------------------------------------
    // Appointment — criação e adição de procedimento
    // -----------------------------------------------------------------------

    public record AppointmentCreateRequest(
            @Schema(description = "ID do paciente")            UUID patientId,
            @Schema(description = "ID do médico")              UUID doctorId,
            @Schema(description = "Data e hora do atendimento") LocalDateTime scheduledAt
    ) {}

    public record AddProcedureRequest(
            @Schema(description = "ID do procedimento") UUID procedureId,
            @Schema(description = "Quantidade", example = "1") int quantity
    ) {}

    public record RescheduleRequest(
            @Schema(description = "Novo horário do atendimento") LocalDateTime newScheduledAt
    ) {}

    // -----------------------------------------------------------------------
    // Appointment — resposta
    // -----------------------------------------------------------------------

    public record AppointmentResponse(
            UUID id,
            PatientResponse patient,
            DoctorResponse  doctor,
            AppointmentStatus status,
            LocalDateTime createdAt,
            LocalDateTime scheduledAt,
            int rescheduleCount,
            List<AppointmentProcedureResponse> procedures
    ) {
        public static AppointmentResponse from(MedicalAppointment a) {
            return new AppointmentResponse(
                    a.getId(),
                    PatientResponse.from(a.getPatient()),
                    DoctorResponse.from(a.getDoctor()),
                    a.getStatus(),
                    a.getCreatedAt(),
                    a.getScheduledAt(),
                    a.getRescheduleCount(),
                    a.getProcedures().stream().map(AppointmentProcedureResponse::from).toList()
            );
        }
    }

    public record AppointmentProcedureResponse(UUID id, String procedureName, int quantity, BigDecimal totalCost) {
        public static AppointmentProcedureResponse from(AppointmentProcedure ap) {
            return new AppointmentProcedureResponse(
                    ap.getId(),
                    ap.getProcedure().getName(),
                    ap.getQuantity(),
                    ap.getTotalCost().getAmount()
            );
        }
    }

    public record BillResponse(UUID appointmentId, BigDecimal totalWithCoverage) {}
}
