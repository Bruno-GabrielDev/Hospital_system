package br.ifsp.hospital.domain.model;

import java.util.UUID;

public class Doctor {

    private final UUID id;
    private final String name;
    private final String specialty;
    private final String license;

    private Doctor(UUID id, String name, String specialty, String license) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.license = license;
    }

    public static Doctor of(String name, String specialty, String license) {
        return null;
    }

    public static Doctor restore(UUID id, String name, String specialty, String license) {
        return null;
    }

    public UUID getId()         { return null; }
    public String getName()     { return null; }
    public String getSpecialty(){ return null; }
    public String getLicense()  { return null; }

    @Override public boolean equals(Object o) { return false; }
    @Override public int hashCode()            { return 0; }
}
