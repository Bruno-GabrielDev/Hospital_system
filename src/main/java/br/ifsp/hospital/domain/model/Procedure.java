package br.ifsp.hospital.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Procedure {

    private final UUID id;
    private final String name;
    private final Money cost;
    private ProcedureStatus status;
    private int gracePeriodDays = 0;

    private Procedure(UUID id, String name, Money cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.status = ProcedureStatus.OPEN;
    }

    public static Procedure of(String name, Money cost) {
        return new Procedure(UUID.randomUUID(), name, cost);
    }

    public static Procedure restore(UUID id, String name, Money cost) {
        return new Procedure(id, name, cost);
    }

    public void complete() {
        this.status = ProcedureStatus.CLOSED;
    }
    public void cancel() {
        this.status = ProcedureStatus.CANCELED;
    }

    public void setGracePeriodDays(int days) { this.gracePeriodDays = days; }

    public int getGracePeriodDays() { return gracePeriodDays; }
    public UUID getId()    { return id; }
    public String getName(){ return name; }
    public Money getCost() { return cost; }
    public ProcedureStatus getStatus() {
        return  status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Procedure procedure = (Procedure) o;
        return Objects.equals(id, procedure.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
