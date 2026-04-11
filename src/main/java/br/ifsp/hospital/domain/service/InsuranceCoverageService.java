package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.InsuranceType;
import br.ifsp.hospital.domain.model.Money;
import org.springframework.stereotype.Service;

@Service
public class InsuranceCoverageService {

    public Money applyCoverage(Money total, InsuranceType insuranceType) {
        return null;
    }

    public Money getCoveredAmount(Money total, InsuranceType insuranceType) {
        return null;
    }
}
