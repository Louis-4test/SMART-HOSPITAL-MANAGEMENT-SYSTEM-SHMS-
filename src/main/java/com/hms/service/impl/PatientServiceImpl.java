package com.hms.service.impl;

import com.hms.entity.Patient;
import com.hms.repository.PatientRepository;
import com.hms.service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final Queue<Patient> erWaitingQueue = new LinkedList<>();
    private final Queue<Patient> emergencyTriageQueue = new PriorityQueue<>(
            (a, b) -> Integer.compare(b.getSeverityLevel(), a.getSeverityLevel())
    );

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Override
    public Patient registerPatient(Patient patient) {
        log.info("Entering registerPatient({})", patient);
        Patient result = patientRepository.save(patient);
        log.debug("Exiting registerPatient: {}", result);
        return result;
    }

    @Override
    public Patient findById(Long id) {
        log.info("Entering findById({})", id);
        Patient result = patientRepository.findById(id).orElse(null);
        log.debug("Exiting findById: {}", result);
        return result;
    }

    @Override
    public Patient findByEmail(String email) {
        log.info("Entering findByEmail({})", email);
        Patient result = patientRepository.findByEmail(email).orElse(null);
        log.debug("Exiting findByEmail: {}", result);
        return result;
    }

    @Override
    public List<Patient> searchPatient() {
        log.info("Entering searchPatient()");
        List<Patient> result = patientRepository.findAll();
        log.debug("Exiting searchPatient: {} results", result.size());
        return result;
    }

    @Override
    public List<Patient> searchPatient(Long id) {
        log.info("Entering searchPatient({})", id);
        Patient patient = patientRepository.findById(id).orElse(null);
        List<Patient> result = patient != null ? List.of(patient) : Collections.emptyList();
        log.debug("Exiting searchPatient: {} results", result.size());
        return result;
    }

    @Override
    public List<Patient> searchPatient(String name) {
        log.info("Entering searchPatient({})", name);
        List<Patient> byFirstName = patientRepository.findByFirstNameContainingIgnoreCase(name);
        List<Patient> byLastName = patientRepository.findByLastNameContainingIgnoreCase(name);
        List<Patient> merged = new java.util.ArrayList<>(byFirstName);
        merged.addAll(byLastName);
        List<Patient> result = merged.stream().distinct().toList();
        log.debug("Exiting searchPatient: {} results", result.size());
        return result;
    }

    @Override
    public List<Patient> searchPatient(String name, String phone) {
        log.info("Entering searchPatient({}, {})", name, phone);
        List<Patient> result = patientRepository.searchByName(name).stream()
                .filter(p -> p.getPhone().equals(phone))
                .toList();
        log.debug("Exiting searchPatient: {} results", result.size());
        return result;
    }

    @Override
    public List<Patient> findAll() {
        log.info("Entering findAll()");
        List<Patient> result = patientRepository.findAll();
        log.debug("Exiting findAll: {} results", result.size());
        return result;
    }

    @Override
    public Page<Patient> findAll(Pageable pageable) {
        log.info("Entering findAll({})", pageable);
        Page<Patient> result = patientRepository.findAll(pageable);
        log.debug("Exiting findAll: {} results", result.getTotalElements());
        return result;
    }

    @Override
    public Patient update(Long id, Patient patient) {
        log.info("Entering update({}, {})", id, patient);
        Patient existing = patientRepository.findById(id).orElse(null);
        if (existing == null) {
            log.debug("Exiting update: patient not found");
            return null;
        }
        existing.setFirstName(patient.getFirstName());
        existing.setLastName(patient.getLastName());
        existing.setEmail(patient.getEmail());
        existing.setPhone(patient.getPhone());
        existing.setDateOfBirth(patient.getDateOfBirth());
        existing.setGender(patient.getGender());
        existing.setBloodType(patient.getBloodType());
        existing.setPatientStatus(patient.getPatientStatus());
        Patient result = patientRepository.save(existing);
        log.debug("Exiting update: {}", result);
        return result;
    }

    @Override
    public void delete(Long id) {
        log.info("Entering delete({})", id);
        patientRepository.deleteById(id);
        log.debug("Exiting delete");
    }

    @Override
    public void addToERQueue(Patient patient) {
        log.info("Entering addToERQueue({})", patient.getId());
        erWaitingQueue.add(patient);
        log.debug("Exiting addToERQueue: queue size {}", erWaitingQueue.size());
    }

    @Override
    public Patient processNextERPatient() {
        log.info("Entering processNextERPatient()");
        Patient patient = erWaitingQueue.poll();
        log.debug("Exiting processNextERPatient: {}", patient);
        return patient;
    }

    @Override
    public Queue<Patient> getERQueue() {
        return new LinkedList<>(erWaitingQueue);
    }

    @Override
    public void addToEmergencyTriage(Patient patient, int severityLevel) {
        log.info("Entering addToEmergencyTriage({}, severity={})", patient.getId(), severityLevel);
        patient.setSeverityLevel(severityLevel);
        emergencyTriageQueue.add(patient);
        log.debug("Exiting addToEmergencyTriage: triage queue size {}", emergencyTriageQueue.size());
    }

    @Override
    public Patient processNextEmergencyPatient() {
        log.info("Entering processNextEmergencyPatient()");
        Patient patient = emergencyTriageQueue.poll();
        log.debug("Exiting processNextEmergencyPatient: {}", patient);
        return patient;
    }

    @Override
    public Queue<Patient> getEmergencyTriageQueue() {
        return new PriorityQueue<>(emergencyTriageQueue);
    }
}
