package com.hms.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DoctorNotifier {

    private static final Logger log = LoggerFactory.getLogger(DoctorNotifier.class);

    @EventListener
    public void onPatientAdmitted(PatientAdmittedEvent event) {
        log.info("NOTIFYING DOCTOR: Patient {} {} has been admitted",
                event.getPatient().getFirstName(), event.getPatient().getLastName());
    }
}
