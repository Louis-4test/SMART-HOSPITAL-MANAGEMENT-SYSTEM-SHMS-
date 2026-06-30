package com.hms.service;

import com.hms.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientService {
    Patient registerPatient(Patient patient);
    Patient findById(Long id);
    Patient findByEmail(String email);
    List<Patient> searchPatient();
    List<Patient> searchPatient(Long id);
    List<Patient> searchPatient(String name);
    List<Patient> searchPatient(String name, String phone);
    List<Patient> findAll();
    Page<Patient> findAll(Pageable pageable);
    Patient update(Long id, Patient patient);
    void delete(Long id);
}
