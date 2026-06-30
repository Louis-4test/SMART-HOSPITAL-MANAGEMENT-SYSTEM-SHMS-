package com.hms.strategy;

import org.springframework.stereotype.Component;

@Component
public class LaboratoryBilling implements BillingStrategy {

    @Override
    public double calculateAmount(double baseAmount) {
        return baseAmount * 1.2;
    }

    @Override
    public String getBillingType() {
        return "LABORATORY";
    }
}
