package br.ifsp.hospital.security.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterUserRequest(
        @Schema(description = "Nome", example = "Carlos")
        String name,
        @Schema(description = "Sobrenome", example = "Souza")
        String lastname,
        @Schema(description = "Email de login", example = "carlos.souza@hospital.com")
        String email,
        @Schema(description = "Senha", example = "Senha@123")
        String password
) {}
