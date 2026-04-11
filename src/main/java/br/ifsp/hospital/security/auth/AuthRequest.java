package br.ifsp.hospital.security.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthRequest(
        @Schema(description = "Email de login", example = "dr.silva@hospital.com")
        String username,
        @Schema(description = "Senha", example = "Senha@123")
        String password
) {}
