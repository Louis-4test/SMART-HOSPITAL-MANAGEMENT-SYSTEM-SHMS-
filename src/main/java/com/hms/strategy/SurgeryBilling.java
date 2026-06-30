package com.hms.strategy;

import org.springframework.stereotype.Component;

@Component
public class SurgeryBilling implements BillingStrategy {

    @Override
    public double calculateAmount(double baseAmount) {
        return baseAmount * 3.5;
    }

    @Override
    public String getBillingType() {
        return "SURGERY";
    }
}
