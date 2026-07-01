package com.hms.facade;

import com.hms.entity.*;
import com.hms.observer.PatientAdmittedEvent;
import com.hms.service.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class HospitalFacade {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final BillingService billingService;
    private final LaboratoryTestService laboratoryTestService;
    private final AuditService auditService;
    private final ApplicationEventPublisher eventPublisher;

    public HospitalFacade(PatientService patientService,
                          DoctorService doctorService,
                          AppointmentService appointmentService,
                          BillingService billingService,
                          LaboratoryTestService laboratoryTestService,
                          AuditService auditService,
                          ApplicationEventPublisher eventPublisher) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.billingService = billingService;
        this.laboratoryTestService = laboratoryTestService;
        this.auditService = auditService;
        this.eventPublisher = eventPublisher;
    }

    public Patient admitPatient(Patient patient, Long doctorId) {
        Patient saved = patientService.registerPatient(patient);
        eventPublisher.publishEvent(new PatientAdmittedEvent(this, saved));
        auditService.log("PATIENT_ADMITTED", "Patient", saved.getId(),
                "Patient " + saved.getFirstName() + " " + saved.getLastName() + " admitted to doctor " + doctorId);
        return saved;
    }

    public Appointment requestLabTest(Long patientId, Long appointmentId, LaboratoryTest test) {
        Appointment appointment = appointmentService.findById(appointmentId);
        if (appointment != null) {
            laboratoryTestService.requestTest(test);
            auditService.log("LAB_TEST_REQUESTED", "LaboratoryTest", test.getId(),
                    "Lab test requested for patient " + patientId + " on appointment " + appointmentId);
        }
        return appointment;
    }

    public Invoice generateBill(Long appointmentId) {
        Invoice invoice = billingService.generateInvoice(appointmentId);
        auditService.log("BILL_GENERATED", "Invoice", invoice.getId(),
                "Invoice generated for appointment " + appointmentId);
        return invoice;
    }

    public void dischargePatient(Long patientId) {
        Patient patient = patientService.findById(patientId);
        if (patient != null) {
            patient.setPatientStatus("DISCHARGED");
            patientService.registerPatient(patient);
            auditService.log("PATIENT_DISCHARGED", "Patient", patientId,
                    "Patient " + patient.getFirstName() + " " + patient.getLastName() + " discharged");
        }
    }
}
