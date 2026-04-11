package br.ifsp.hospital.domain.model;

import java.util.UUID;

public class AppointmentProcedure {

    private final UUID id;
    private final Procedure procedure;
    private final int quantity;

    private AppointmentProcedure(UUID id, Procedure procedure, int quantity) {
        this.id = id;
        this.procedure = procedure;
        this.quantity = quantity;
    }

    public static AppointmentProcedure of(Procedure procedure, int quantity) {
        return null;
    }

    public static AppointmentProcedure restore(UUID id, Procedure procedure, int quantity) {
        return null;
    }

    public UUID getId()            { return null; }
    public Procedure getProcedure(){ return null; }
    public int getQuantity()       { return 0; }
    public Money getTotalCost()    { return null; }

    @Override public boolean equals(Object o) { return false; }
    @Override public int hashCode()            { return 0; }
}
