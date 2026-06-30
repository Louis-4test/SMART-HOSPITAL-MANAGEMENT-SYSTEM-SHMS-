package com.hms.observer;

import com.hms.entity.Patient;
import org.springframework.context.ApplicationEvent;

public class PatientAdmittedEvent extends ApplicationEvent {

    private final Patient patient;

    public PatientAdmittedEvent(Object source, Patient patient) {
        super(source);
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }
}
