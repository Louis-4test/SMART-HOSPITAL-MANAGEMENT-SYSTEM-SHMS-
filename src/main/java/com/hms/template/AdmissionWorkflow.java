package com.hms.template;

import com.hms.entity.Patient;

public abstract class AdmissionWorkflow {

    public final void processAdmission(Patient patient) {
        registerPatient(patient);
        assignRoom();
        assignDoctor();
        createMedicalRecord();
        notifyDepartments();
    }

    protected abstract void registerPatient(Patient patient);
    protected abstract void assignRoom();
    protected abstract void assignDoctor();
    protected abstract void createMedicalRecord();
    protected abstract void notifyDepartments();
}
