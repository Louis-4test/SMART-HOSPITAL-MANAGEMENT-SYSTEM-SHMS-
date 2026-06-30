package com.hms.service;

import com.hms.entity.Prescription;
import java.util.List;

public interface PrescriptionService {
    Prescription createPrescription(Prescription prescription);
    Prescription findById(Long id);
    List<Prescription> findByPatientId(Long patientId);
    List<Prescription> findAll();
}
