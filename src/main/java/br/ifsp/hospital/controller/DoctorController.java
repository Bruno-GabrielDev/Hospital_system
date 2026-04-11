package br.ifsp.hospital.controller;

import br.ifsp.hospital.controller.dto.HospitalDTOs.*;
import br.ifsp.hospital.domain.service.DoctorAppService;
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
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@Tag(name = "Médicos")
@SecurityRequirement(name = "bearerAuth")
public class DoctorController {

    private final DoctorAppService doctorAppService;

    @Operation(summary = "Cadastrar médico")
    @PostMapping
    public ResponseEntity<DoctorResponse> create(@RequestBody DoctorRequest request) {
        var doctor = doctorAppService.create(request.name(), request.specialty(), request.license());
        return ResponseEntity.status(HttpStatus.CREATED).body(DoctorResponse.from(doctor));
    }

    @Operation(summary = "Buscar médico por ID")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(DoctorResponse.from(doctorAppService.findById(id)));
    }

    @Operation(summary = "Listar todos os médicos")
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> findAll() {
        return ResponseEntity.ok(
                doctorAppService.findAll().stream().map(DoctorResponse::from).toList());
    }
}
