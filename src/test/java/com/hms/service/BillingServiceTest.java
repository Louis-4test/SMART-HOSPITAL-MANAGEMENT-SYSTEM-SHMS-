package com.hms.service;

import com.hms.entity.Appointment;
import com.hms.entity.Invoice;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.InvoiceRepository;
import com.hms.service.impl.BillingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    private BillingService billingService;

    @BeforeEach
    void setUp() {
        billingService = new BillingServiceImpl(invoiceRepository, appointmentRepository);
    }

    @Test
    void generateInvoice_WhenAppointmentCompleted_ShouldCreatePendingInvoice() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus("COMPLETED");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Invoice result = billingService.generateInvoice(1L);

        assertNotNull(result);
        assertSame(appointment, result.getAppointment());
        assertEquals("PENDING", result.getStatus());
        assertEquals(LocalDate.now(), result.getIssueDate());
        assertEquals(LocalDate.now().plusDays(30), result.getDueDate());
        assertTrue(result.getInvoiceNumber().startsWith("INV-"));
        verify(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    void generateInvoice_WhenAppointmentMissing_ShouldReturnNullAndNotSave() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        Invoice result = billingService.generateInvoice(99L);

        assertNull(result);
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void generateInvoice_WhenAppointmentNotCompleted_ShouldReturnNullAndNotSave() {
        Appointment appointment = new Appointment();
        appointment.setStatus("SCHEDULED");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Invoice result = billingService.generateInvoice(1L);

        assertNull(result);
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void processPayment_WhenInvoiceExists_ShouldMarkPaid() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setStatus("PENDING");

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(invoice)).thenReturn(invoice);

        Invoice result = billingService.processPayment(1L, "CARD");

        assertNotNull(result);
        assertEquals("PAID", result.getStatus());
        verify(invoiceRepository).save(invoice);
    }

    @Test
    void processPayment_WhenInvoiceMissing_ShouldReturnNull() {
        when(invoiceRepository.findById(44L)).thenReturn(Optional.empty());

        Invoice result = billingService.processPayment(44L, "CASH");

        assertNull(result);
        verify(invoiceRepository, never()).save(any());
    }
}
