package br.ifsp.hospital.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Patient {

    private final UUID id;
    private final String name;
    private final String document;
    private final InsuranceType insuranceType;
    private LocalDate planEnrollmentDate = LocalDate.now().minusYears(5);


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

    public void setPlanEnrollmentDate(LocalDate date) { this.planEnrollmentDate = date; }

    public LocalDate getPlanEnrollmentDate() { return planEnrollmentDate; }
    public UUID getId()                     { return id; }
    public String getName()                 { return name; }
    public String getDocument()             { return document; }
    public InsuranceType getInsuranceType() { return insuranceType; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
