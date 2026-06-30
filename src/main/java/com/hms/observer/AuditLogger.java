package com.hms.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditLogger {

    private static final Logger log = LoggerFactory.getLogger(AuditLogger.class);

    @EventListener
    public void onPatientAdmitted(PatientAdmittedEvent event) {
        log.info("AUDIT: Patient {} {} admitted at {}",
                event.getPatient().getFirstName(),
                event.getPatient().getLastName(),
                LocalDateTime.now());
    }
}
