package com.hms.strategy;

import org.springframework.stereotype.Component;

@Component
public class GeneralConsultationBilling implements BillingStrategy {

    @Override
    public double calculateAmount(double baseAmount) {
        return baseAmount * 1.0;
    }

    @Override
    public String getBillingType() {
        return "GENERAL_CONSULTATION";
    }
}
