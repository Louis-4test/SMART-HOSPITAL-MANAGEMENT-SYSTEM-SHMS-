package com.hms.strategy;

import org.springframework.stereotype.Component;

@Component
public class PharmacyBilling implements BillingStrategy {

    @Override
    public double calculateAmount(double baseAmount) {
        return baseAmount * 1.15;
    }

    @Override
    public String getBillingType() {
        return "PHARMACY";
    }
}
