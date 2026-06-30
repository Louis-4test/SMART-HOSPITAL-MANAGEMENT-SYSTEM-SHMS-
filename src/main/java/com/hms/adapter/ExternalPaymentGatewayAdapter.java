package com.hms.adapter;

import org.springframework.stereotype.Component;

@Component
public class ExternalPaymentGatewayAdapter implements PaymentGateway {

    private final ExternalPaymentService externalService = new ExternalPaymentService();

    @Override
    public boolean processPayment(String invoiceNumber, double amount, String currency) {
        String response = externalService.sendPayment(invoiceNumber, amount, currency);
        return response.startsWith("SUCCESS");
    }
}
