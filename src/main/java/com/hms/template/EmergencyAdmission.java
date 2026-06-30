package com.hms.template;

import com.hms.entity.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmergencyAdmission extends AdmissionWorkflow {

    private static final Logger log = LoggerFactory.getLogger(EmergencyAdmission.class);

    @Override
    protected void registerPatient(Patient patient) {
        patient.setPatientStatus("EMERGENCY_ADMITTED");
        log.info("Emergency registration for patient: {} {}", patient.getFirstName(), patient.getLastName());
    }

    @Override
    protected void assignRoom() {
        log.info("Assigning emergency room immediately");
    }

    @Override
    protected void assignDoctor() {
        log.info("Assigning on-call emergency doctor");
    }

    @Override
    protected void createMedicalRecord() {
        log.info("Creating emergency medical record");
    }

    @Override
    protected void notifyDepartments() {
        log.info("Notifying emergency team, lab, and billing");
    }
}
