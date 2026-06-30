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
    private final ApplicationEventPublisher eventPublisher;

    public HospitalFacade(PatientService patientService,
                          DoctorService doctorService,
                          AppointmentService appointmentService,
                          BillingService billingService,
                          LaboratoryTestService laboratoryTestService,
                          ApplicationEventPublisher eventPublisher) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.billingService = billingService;
        this.laboratoryTestService = laboratoryTestService;
        this.eventPublisher = eventPublisher;
    }

    public Patient admitPatient(Patient patient, Long doctorId) {
        Patient saved = patientService.registerPatient(patient);
        eventPublisher.publishEvent(new PatientAdmittedEvent(this, saved));
        return saved;
    }

    public Appointment requestLabTest(Long patientId, Long appointmentId, LaboratoryTest test) {
        Appointment appointment = appointmentService.findById(appointmentId);
        if (appointment != null) {
            laboratoryTestService.requestTest(test);
        }
        return appointment;
    }

    public Invoice generateBill(Long appointmentId) {
        return billingService.generateInvoice(appointmentId);
    }

    public void dischargePatient(Long patientId) {
        Patient patient = patientService.findById(patientId);
        if (patient != null) {
            patient.setPatientStatus("DISCHARGED");
            patientService.registerPatient(patient);
        }
    }
}
