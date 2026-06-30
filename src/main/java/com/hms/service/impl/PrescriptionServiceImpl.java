package com.hms.service.impl;

import com.hms.entity.Prescription;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.PrescriptionRepository;
import com.hms.service.PrescriptionService;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository,
                                    AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(PrescriptionServiceImpl.class);

    @Override
    public Prescription createPrescription(Prescription prescription) {
        log.info("Entering createPrescription({})", prescription);
        List<com.hms.entity.Appointment> completed = appointmentRepository.findByPatientId(
                prescription.getPatient().getId()).stream()
                .filter(a -> "COMPLETED".equals(a.getStatus()) || "DIAGNOSED".equals(a.getStatus()))
                .toList();
        if (completed.isEmpty()) {
            throw new IllegalStateException("Cannot issue prescription: no diagnosis recorded for patient");
        }
        Prescription result = prescriptionRepository.save(prescription);
        log.debug("Exiting createPrescription: {}", result);
        return result;
    }

    @Override
    public Prescription findById(Long id) {
        log.info("Entering findById({})", id);
        Prescription result = prescriptionRepository.findById(id).orElse(null);
        log.debug("Exiting findById: {}", result);
        return result;
    }

    @Override
    public List<Prescription> findByPatientId(Long patientId) {
        log.info("Entering findByPatientId({})", patientId);
        List<Prescription> result = prescriptionRepository.findByPatientId(patientId);
        log.debug("Exiting findByPatientId: {} results", result.size());
        return result;
    }

    @Override
    public List<Prescription> findAll() {
        log.info("Entering findAll()");
        List<Prescription> result = prescriptionRepository.findAll();
        log.debug("Exiting findAll: {} results", result.size());
        return result;
    }
}
