package com.hms.template;

import com.hms.entity.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutpatientAdmission extends AdmissionWorkflow {

    private static final Logger log = LoggerFactory.getLogger(OutpatientAdmission.class);

    @Override
    protected void registerPatient(Patient patient) {
        patient.setPatientStatus("OUTPATIENT");
        log.info("Outpatient registration for patient: {} {}", patient.getFirstName(), patient.getLastName());
    }

    @Override
    protected void assignRoom() {
        log.info("Assigning consultation room");
    }

    @Override
    protected void assignDoctor() {
        log.info("Assigning available specialist");
    }

    @Override
    protected void createMedicalRecord() {
        log.info("Creating outpatient medical record");
    }

    @Override
    protected void notifyDepartments() {
        log.info("Notifying pharmacy and billing");
    }
}
