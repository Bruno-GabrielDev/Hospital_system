package br.ifsp.hospital.controller;

import br.ifsp.hospital.controller.dto.HospitalDTOs.*;
import br.ifsp.hospital.domain.model.Money; // Adicione este import
import br.ifsp.hospital.domain.service.ProcedureAppService;
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
@RequestMapping("/api/v1/procedures")
@RequiredArgsConstructor
@Tag(name = "Procedimentos")
@SecurityRequirement(name = "bearerAuth")
public class ProcedureController {

    private final ProcedureAppService procedureAppService;

    @Operation(summary = "Cadastrar procedimento")
    @PostMapping
    public ResponseEntity<ProcedureResponse> create(@RequestBody ProcedureRequest request) {
        var proc = procedureAppService.create(request.name(), new Money(request.cost()));

        return ResponseEntity.status(HttpStatus.CREATED).body(ProcedureResponse.from(proc));
    }

    @Operation(summary = "Buscar procedimento por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProcedureResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ProcedureResponse.from(procedureAppService.findById(id)));
    }

    @Operation(summary = "Listar todos os procedimentos")
    @GetMapping
    public ResponseEntity<List<ProcedureResponse>> findAll() {
        return ResponseEntity.ok(
                procedureAppService.findAll().stream().map(ProcedureResponse::from).toList());
    }
}