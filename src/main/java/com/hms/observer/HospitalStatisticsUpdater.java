package com.hms.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class HospitalStatisticsUpdater {

    private static final Logger log = LoggerFactory.getLogger(HospitalStatisticsUpdater.class);
    private final AtomicLong admissionCount = new AtomicLong(0);

    @EventListener
    public void onPatientAdmitted(PatientAdmittedEvent event) {
        long count = admissionCount.incrementAndGet();
        log.info("UPDATING STATISTICS: Total admissions so far: {}", count);
    }

    public long getTotalAdmissions() {
        return admissionCount.get();
    }
}
