package br.ifsp.hospital.domain.model;

import java.math.BigDecimal;

public final class Money {

    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        if(amount == null)
            throw new IllegalArgumentException("Amount cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Valor monetário não pode ser negativo.");
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return null;
    }

    public Money add(Money other) {
        return null;
    }

    public Money multiply(int quantity) {
        return null;
    }

    public Money applyDiscount(BigDecimal factor) {
        return null;
    }

    public Money subtract(Money other) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }
}
