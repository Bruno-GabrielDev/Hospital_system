package br.ifsp.hospital.security.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record RegisterUserResponse(
        @Schema(description = "ID do usuário registrado")
        UUID id
) {}
