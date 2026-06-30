package com.hms.service.impl;

import com.hms.entity.Appointment;
import com.hms.entity.Invoice;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.InvoiceRepository;
import com.hms.service.BillingService;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {

    private final InvoiceRepository invoiceRepository;
    private final AppointmentRepository appointmentRepository;

    public BillingServiceImpl(InvoiceRepository invoiceRepository,
                              AppointmentRepository appointmentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.appointmentRepository = appointmentRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(BillingServiceImpl.class);

    @Override
    public Invoice generateInvoice(Long appointmentId) {
        log.info("Entering generateInvoice({})", appointmentId);
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null || !"COMPLETED".equals(appointment.getStatus())) {
            log.debug("Exiting generateInvoice: appointment invalid");
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setAppointment(appointment);
        invoice.setIssueDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(30));
        invoice.setStatus("PENDING");
        Invoice result = invoiceRepository.save(invoice);
        log.debug("Exiting generateInvoice: {}", result);
        return result;
    }

    @Override
    public Invoice findById(Long id) {
        log.info("Entering findById({})", id);
        Invoice result = invoiceRepository.findById(id).orElse(null);
        log.debug("Exiting findById: {}", result);
        return result;
    }

    @Override
    public Invoice findByAppointmentId(Long appointmentId) {
        log.info("Entering findByAppointmentId({})", appointmentId);
        Invoice result = invoiceRepository.findByAppointmentId(appointmentId).orElse(null);
        log.debug("Exiting findByAppointmentId: {}", result);
        return result;
    }

    @Override
    public Invoice processPayment(Long invoiceId, String paymentMethod) {
        log.info("Entering processPayment({}, {})", invoiceId, paymentMethod);
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null) {
            log.debug("Exiting processPayment: invoice not found");
            return null;
        }
        invoice.setStatus("PAID");
        Invoice result = invoiceRepository.save(invoice);
        log.debug("Exiting processPayment: {}", result);
        return result;
    }
}
