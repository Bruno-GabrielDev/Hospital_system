package br.ifsp.hospital.domain.model;

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
        return null;
    }

    public static Patient restore(UUID id, String name, String document, InsuranceType insuranceType) {
        return null;
    }

    public UUID getId()                     { return null; }
    public String getName()                 { return null; }
    public String getDocument()             { return null; }
    public InsuranceType getInsuranceType() { return null; }

    @Override public boolean equals(Object o) { return false; }
    @Override public int hashCode()            { return 0; }
}
