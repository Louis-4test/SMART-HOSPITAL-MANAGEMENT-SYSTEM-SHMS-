package com.hms.adapter;

public class ExternalPaymentService {

    public String sendPayment(String transactionId, double value, String currencyCode) {
        return "SUCCESS:" + transactionId;
    }
}
