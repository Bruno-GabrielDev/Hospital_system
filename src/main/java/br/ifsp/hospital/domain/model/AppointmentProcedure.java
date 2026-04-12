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
        return new AppointmentProcedure(UUID.randomUUID(), procedure, quantity);
    }

    public static AppointmentProcedure restore(UUID id, Procedure procedure, int quantity) {
        return null;
    }

    public void cancel() {
        this.procedure.cancel();
    }

    public UUID getId()            { return null; }
    public Procedure getProcedure(){
        return this.procedure;
    }
    public int getQuantity()       { return 0; }
    public Money getTotalCost()    { return null; }

    public ProcedureStatus getStatus(){
        return this.getProcedure().getStatus();
    }

    @Override public boolean equals(Object o) { return false; }
    @Override public int hashCode()            { return 0; }
}
