package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class InsuranceCoverageService {

    public Money applyCoverage(Money grossAmount, InsuranceType type) {
        BigDecimal amount = grossAmount.getAmount();

        if (type == null) {
            return new Money(amount.setScale(2, RoundingMode.HALF_UP));
        }

        BigDecimal multiplier = switch (type) {
            case PREMIUM -> new BigDecimal("0.30");
            case BASIC -> new BigDecimal("0.70");
            default -> BigDecimal.ONE;
        };

        BigDecimal result = amount.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
        return new Money(result);
    }

    public Money getCoveredAmount(Money grossAmount, InsuranceType type) {

        if (type == null) {
            return new Money(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }

        Money patientAmount = applyCoverage(grossAmount, type);
        BigDecimal covered = grossAmount.getAmount().subtract(patientAmount.getAmount());

        return new Money(covered);
    }
}