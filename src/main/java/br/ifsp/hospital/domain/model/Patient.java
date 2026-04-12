package br.ifsp.hospital.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Patient {

    private final UUID id;
    private final String name;
    private final String document;
    private final InsuranceType insuranceType;

    private Patient(UUID id, String name, String document, InsuranceType insuranceType) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.insuranceType = insuranceType;
    }

    public static Patient of(String name, String document, InsuranceType insuranceType) {
        Objects.requireNonNull(insuranceType, "Tipo de seguro é obrigatório.");
        Objects.requireNonNull(name, "Nome é obrigatório.");
        if (name.isBlank())
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        return new Patient(UUID.randomUUID(), name, document, insuranceType);
    }

    public static Patient restore(UUID id, String name, String document, InsuranceType insuranceType) {
        return new Patient(id, name, document, insuranceType);
    }

    public UUID getId()                     { return id; }
    public String getName()                 { return name; }
    public String getDocument()             { return document; }
    public InsuranceType getInsuranceType() { return insuranceType; }

    @Override public boolean equals(Object o) { return false; }
    @Override public int hashCode()            { return 0; }
}
