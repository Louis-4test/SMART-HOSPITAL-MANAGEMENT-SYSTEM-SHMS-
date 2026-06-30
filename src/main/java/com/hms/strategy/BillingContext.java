package com.hms.strategy;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BillingContext {

    private final Map<String, BillingStrategy> strategies = new ConcurrentHashMap<>();

    public BillingContext(Map<String, BillingStrategy> strategyBeans) {
        strategyBeans.values().forEach(s -> strategies.put(s.getBillingType(), s));
    }

    public double calculate(String billingType, double baseAmount) {
        BillingStrategy strategy = strategies.get(billingType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown billing type: " + billingType);
        }
        return strategy.calculateAmount(baseAmount);
    }
}
