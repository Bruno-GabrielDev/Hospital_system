package br.ifsp.hospital.domain.model;

import java.util.UUID;

public record Invoice(
        UUID appointmentId,
        InvoiceType type,
        Money amount
) {}