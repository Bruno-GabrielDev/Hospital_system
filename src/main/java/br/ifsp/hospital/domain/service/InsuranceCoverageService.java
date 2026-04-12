package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InsuranceCoverageService {

    public Money applyCoverage(Money total, InsuranceType insuranceType) {
        if (insuranceType == InsuranceType.BASIC)
            return total.applyDiscount(new BigDecimal("0.70"));
        if (insuranceType == InsuranceType.PREMIUM)
            return total.applyDiscount(new BigDecimal("0.30"));
        return total;
    }

    public Money getCoveredAmount(Money total, InsuranceType insuranceType) {
        Money patientPays = applyCoverage(total, insuranceType);
        return total.subtract(patientPays);
    }
}
