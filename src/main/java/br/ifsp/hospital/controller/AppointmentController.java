package br.ifsp.hospital.controller;

import br.ifsp.hospital.controller.dto.HospitalDTOs.*;
import br.ifsp.hospital.domain.model.BillDetail;
import br.ifsp.hospital.domain.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@Tag(name = "Atendimentos")
@SecurityRequirement(name = "bearerAuth")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Criar atendimento", description = "Cria novo atendimento com status OPEN.")
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody AppointmentCreateRequest request) {
        var appt = appointmentService.create(
                request.patientId(), request.doctorId(), request.scheduledAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(AppointmentResponse.from(appt));
    }

    @Operation(summary = "Adicionar procedimento ao atendimento")
    @PostMapping("/{id}/procedures")
    public ResponseEntity<AppointmentResponse> addProcedure(
            @PathVariable UUID id,
            @RequestBody AddProcedureRequest request) {
        var appt = appointmentService.addProcedure(id, request.procedureId(), request.quantity());
        return ResponseEntity.ok(AppointmentResponse.from(appt));
    }

    @Operation(summary = "Fechar atendimento", description = "Status muda para CLOSED.")
    @PatchMapping("/{id}/close")
    public ResponseEntity<AppointmentResponse> close(@PathVariable UUID id) {
        return ResponseEntity.ok(AppointmentResponse.from(appointmentService.close(id)));
    }

    @Operation(summary = "Calcular conta com cobertura do plano")
    @GetMapping("/{id}/bill")
    public ResponseEntity<BillResponse> calculateBill(@PathVariable UUID id) {
        BillDetail detail = appointmentService.calculateBill(id);
        return ResponseEntity.ok(new BillResponse(id, detail.patientAmount().getAmount()));
    }

    @Operation(summary = "Faturar atendimento", description = "Status muda para BILLED.")
    @PatchMapping("/{id}/bill")
    public ResponseEntity<AppointmentResponse> markAsBilled(@PathVariable UUID id) {
        return ResponseEntity.ok(AppointmentResponse.from(appointmentService.markAsBilled(id)));
    }

    @Operation(summary = "Reagendar atendimento")
    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentResponse> reschedule(
            @PathVariable UUID id,
            @RequestBody RescheduleRequest request) {
        var appt = appointmentService.reschedule(id, request.newScheduledAt());
        return ResponseEntity.ok(AppointmentResponse.from(appt));
    }

    @Operation(summary = "Buscar atendimento por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(AppointmentResponse.from(appointmentService.findById(id)));
    }

    @Operation(summary = "Listar todos os atendimentos")
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> findAll() {
        return ResponseEntity.ok(
                appointmentService.findAll().stream().map(AppointmentResponse::from).toList());
    }

    @Operation(summary = "Listar atendimentos por paciente")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> findByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(
                appointmentService.findByPatient(patientId).stream()
                        .map(AppointmentResponse::from).toList());
    }

    @Operation(summary = "Listar atendimentos por médico")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> findByDoctor(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(
                appointmentService.findByDoctor(doctorId).stream()
                        .map(AppointmentResponse::from).toList());
    }

    @Operation(summary = "Cancelar atendimento", description = "Cancela o atendimento e libera a agenda do médico.")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancel(
            @PathVariable UUID id,
            @RequestBody CancelRequest request) {

        var appt = appointmentService.cancel(id, request.reason());

        return ResponseEntity.ok(AppointmentResponse.from(appt));
    }
}
