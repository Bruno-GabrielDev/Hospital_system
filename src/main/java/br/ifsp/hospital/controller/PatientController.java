package br.ifsp.hospital.controller;

import br.ifsp.hospital.controller.dto.HospitalDTOs.*;
import br.ifsp.hospital.domain.service.PatientAppService;
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
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Pacientes")
@SecurityRequirement(name = "bearerAuth")
public class PatientController {

    private final PatientAppService patientAppService;

    @Operation(summary = "Cadastrar paciente")
    @PostMapping
    public ResponseEntity<PatientResponse> create(@RequestBody PatientRequest request) {
        var patient = patientAppService.create(
                request.name(), request.document(), request.insuranceType());
        return ResponseEntity.status(HttpStatus.CREATED).body(PatientResponse.from(patient));
    }

    @Operation(summary = "Buscar paciente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(PatientResponse.from(patientAppService.findById(id)));
    }

    @Operation(summary = "Listar todos os pacientes")
    @GetMapping
    public ResponseEntity<List<PatientResponse>> findAll() {
        return ResponseEntity.ok(
                patientAppService.findAll().stream().map(PatientResponse::from).toList());
    }
}
