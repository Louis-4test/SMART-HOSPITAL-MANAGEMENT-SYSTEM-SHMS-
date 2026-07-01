package com.hms.observer;

import com.hms.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AuditLogger {

    private static final Logger log = LoggerFactory.getLogger(AuditLogger.class);
    private final AuditService auditService;

    public AuditLogger(AuditService auditService) {
        this.auditService = auditService;
    }

    @EventListener
    public void onPatientAdmitted(PatientAdmittedEvent event) {
        String details = "Patient " + event.getPatient().getFirstName() + " " + event.getPatient().getLastName() + " admitted";
        log.info("AUDIT: {}", details);
        auditService.log("PATIENT_ADMITTED", "Patient", event.getPatient().getId(), details, "SYSTEM");
        log.debug("Audit log persisted to database");
    }
}
