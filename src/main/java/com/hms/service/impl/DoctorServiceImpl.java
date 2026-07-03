package com.hms.service.impl;

import com.hms.entity.Doctor;
import com.hms.repository.DoctorRepository;
import com.hms.service.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final Set<String> licenseNumbers = new HashSet<>();

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
        doctorRepository.findAll().forEach(d -> {
            if (d.getLicenseNumber() != null) {
                licenseNumbers.add(d.getLicenseNumber());
            }
        });
    }

    private static final Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Override
    public Doctor registerDoctor(Doctor doctor) {
        log.info("Entering registerDoctor({})", doctor);
        if (doctor.getLicenseNumber() != null) {
            licenseNumbers.add(doctor.getLicenseNumber());
        }
        Doctor result = doctorRepository.save(doctor);
        log.debug("Exiting registerDoctor: {}", result);
        return result;
    }

    @Override
    public Doctor findById(Long id) {
        log.info("Entering findById({})", id);
        Doctor result = doctorRepository.findById(id).orElse(null);
        log.debug("Exiting findById: {}", result);
        return result;
    }

    @Override
    public Doctor findByLicenseNumber(String licenseNumber) {
        log.info("Entering findByLicenseNumber({})", licenseNumber);
        Doctor result = doctorRepository.findByLicenseNumber(licenseNumber).orElse(null);
        log.debug("Exiting findByLicenseNumber: {}", result);
        return result;
    }

    @Override
    public List<Doctor> findBySpecialization(String specialization) {
        log.info("Entering findBySpecialization({})", specialization);
        List<Doctor> result = doctorRepository.findBySpecialization(specialization);
        log.debug("Exiting findBySpecialization: {} results", result.size());
        return result;
    }

    @Override
    public List<Doctor> findByDepartmentId(Long departmentId) {
        log.info("Entering findByDepartmentId({})", departmentId);
        List<Doctor> result = doctorRepository.findByDepartmentId(departmentId);
        log.debug("Exiting findByDepartmentId: {} results", result.size());
        return result;
    }

    @Override
    public List<Doctor> findAll() {
        log.info("Entering findAll()");
        List<Doctor> result = doctorRepository.findAll();
        log.debug("Exiting findAll: {} results", result.size());
        return result;
    }

    @Override
    public Page<Doctor> findAll(Pageable pageable) {
        log.info("Entering findAll({})", pageable);
        Page<Doctor> result = doctorRepository.findAll(pageable);
        log.debug("Exiting findAll: {} results", result.getTotalElements());
        return result;
    }

    @Override
    public Doctor update(Long id, Doctor doctor) {
        log.info("Entering update({}, {})", id, doctor);
        Doctor existing = doctorRepository.findById(id).orElse(null);
        if (existing == null) {
            log.debug("Exiting update: doctor not found");
            return null;
        }
        existing.setFirstName(doctor.getFirstName());
        existing.setLastName(doctor.getLastName());
        existing.setEmail(doctor.getEmail());
        existing.setPhone(doctor.getPhone());
        existing.setSpecialization(doctor.getSpecialization());
        existing.setYearsOfExperience(doctor.getYearsOfExperience());
        Doctor result = doctorRepository.save(existing);
        log.debug("Exiting update: {}", result);
        return result;
    }

    @Override
    public void delete(Long id) {
        log.info("Entering delete({})", id);
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor != null && doctor.getLicenseNumber() != null) {
            licenseNumbers.remove(doctor.getLicenseNumber());
        }
        doctorRepository.deleteById(id);
        log.debug("Exiting delete");
    }

    @Override
    public List<Doctor> getBusiestDoctors() {
        log.info("Entering getBusiestDoctors()");
        List<Doctor> result = doctorRepository.findBusiestDoctors();
        log.debug("Exiting getBusiestDoctors: {} results", result.size());
        return result;
    }

    @Override
    public Set<String> getAllLicenseNumbers() {
        return Set.copyOf(licenseNumbers);
    }

    @Override
    public boolean isLicenseNumberRegistered(String licenseNumber) {
        return licenseNumbers.contains(licenseNumber);
    }
}
