package com.hms.strategy;

public interface BillingStrategy {
    double calculateAmount(double baseAmount);
    String getBillingType();
}
