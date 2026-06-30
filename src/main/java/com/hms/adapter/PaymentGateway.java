package com.hms.adapter;

public interface PaymentGateway {
    boolean processPayment(String invoiceNumber, double amount, String currency);
}
