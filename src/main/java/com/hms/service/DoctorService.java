package com.hms.service;

import com.hms.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface DoctorService {
    Doctor registerDoctor(Doctor doctor);
    Doctor findById(Long id);
    Doctor findByLicenseNumber(String licenseNumber);
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findByDepartmentId(Long departmentId);
    List<Doctor> findAll();
    Page<Doctor> findAll(Pageable pageable);
    Doctor update(Long id, Doctor doctor);
    void delete(Long id);
    List<Doctor> getBusiestDoctors();
    Set<String> getAllLicenseNumbers();
    boolean isLicenseNumberRegistered(String licenseNumber);
}
