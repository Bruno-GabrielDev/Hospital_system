package br.ifsp.hospital.domain.model;

import java.util.UUID;

public record BillDetail(
        UUID appointmentId,
        Money grossTotal,
        Money patientAmount,
        Money insuranceAmount,
        InsuranceType insuranceType
) {}