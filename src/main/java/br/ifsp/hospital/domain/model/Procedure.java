package br.ifsp.hospital.domain.model;

import java.util.UUID;

public class Procedure {

    private final UUID id;
    private final String name;
    private final Money cost;

    private Procedure(UUID id, String name, Money cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public static Procedure of(String name, Money cost) {
        return null;
    }

    public static Procedure restore(UUID id, String name, Money cost) {
        return null;
    }

    public UUID getId()    { return null; }
    public String getName(){ return null; }
    public Money getCost() { return null; }

    @Override public boolean equals(Object o) { return false; }
    @Override public int hashCode()            { return 0; }
}
