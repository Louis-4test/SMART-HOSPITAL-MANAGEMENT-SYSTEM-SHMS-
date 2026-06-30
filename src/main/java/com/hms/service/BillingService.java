package com.hms.service;

import com.hms.entity.Invoice;

public interface BillingService {
    Invoice generateInvoice(Long appointmentId);
    Invoice findById(Long id);
    Invoice findByAppointmentId(Long appointmentId);
    Invoice processPayment(Long invoiceId, String paymentMethod);
}
