package com.hms.controller;

import com.hms.dto.InvoiceDTO;
import com.hms.entity.Invoice;
import com.hms.service.BillingService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/billing")
public class BillingController {

    private final BillingService billingService;
    private final ModelMapper modelMapper;

    public BillingController(BillingService billingService, ModelMapper modelMapper) {
        this.billingService = billingService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/invoices/generate/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<InvoiceDTO> generateInvoice(@PathVariable Long appointmentId) {
        Invoice invoice = billingService.generateInvoice(appointmentId);
        if (invoice == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(modelMapper.map(invoice, InvoiceDTO.class));
    }

    @GetMapping("/invoices/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<InvoiceDTO> getInvoice(@PathVariable Long id) {
        Invoice invoice = billingService.findById(id);
        if (invoice == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(invoice, InvoiceDTO.class));
    }

    @PostMapping("/invoices/{id}/pay")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<InvoiceDTO> payInvoice(@PathVariable Long id, @RequestParam String method) {
        Invoice invoice = billingService.processPayment(id, method);
        if (invoice == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(invoice, InvoiceDTO.class));
    }
}
