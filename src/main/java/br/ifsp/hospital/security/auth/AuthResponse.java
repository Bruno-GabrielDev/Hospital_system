package br.ifsp.hospital.security.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
        @Schema(description = "Token JWT para usar nas requisições autenticadas")
        String token
) {}
