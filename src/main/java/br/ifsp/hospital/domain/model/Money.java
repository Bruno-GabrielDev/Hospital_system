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
        return amount;
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.getAmount()));
    }

    public Money multiply(int quantity) {
        if (quantity <= 0 )
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        return new Money(amount.multiply(BigDecimal.valueOf(quantity)));
    }

    public Money applyDiscount(BigDecimal factor) {
        return new Money(this.amount.multiply(factor));
    }

    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.getAmount()));
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
