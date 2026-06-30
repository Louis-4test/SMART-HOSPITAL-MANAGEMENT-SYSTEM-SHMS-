package com.hms.service.impl;

import com.hms.entity.*;
import com.hms.repository.*;
import com.hms.service.ReportService;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final InvoiceRepository invoiceRepository;
    private final DepartmentRepository departmentRepository;
    private final LaboratoryTestRepository laboratoryTestRepository;

    public ReportServiceImpl(PatientRepository patientRepository,
                             DoctorRepository doctorRepository,
                             AppointmentRepository appointmentRepository,
                             InvoiceRepository invoiceRepository,
                             DepartmentRepository departmentRepository,
                             LaboratoryTestRepository laboratoryTestRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.invoiceRepository = invoiceRepository;
        this.departmentRepository = departmentRepository;
        this.laboratoryTestRepository = laboratoryTestRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Override
    public long getTotalPatients() {
        log.info("Entering getTotalPatients()");
        long result = patientRepository.count();
        log.debug("Exiting getTotalPatients: {}", result);
        return result;
    }

    @Override
    public Map<String, Long> getPatientsPerDepartment() {
        log.info("Entering getPatientsPerDepartment()");
        Map<String, Long> result = Map.of();
        log.debug("Exiting getPatientsPerDepartment: {}", result);
        return result;
    }

    @Override
    public long getPatientsAdmittedToday() {
        log.info("Entering getPatientsAdmittedToday()");
        long result = 0;
        log.debug("Exiting getPatientsAdmittedToday: {}", result);
        return result;
    }

    @Override
    public Map<String, Long> getDoctorWorkload() {
        log.info("Entering getDoctorWorkload()");
        List<Doctor> doctors = doctorRepository.findAll();
        Map<String, Long> workload = new HashMap<>();
        for (Doctor doctor : doctors) {
            workload.put(doctor.getFirstName() + " " + doctor.getLastName(),
                    (long) doctor.getAppointments().size());
        }
        log.debug("Exiting getDoctorWorkload: {} doctors", workload.size());
        return workload;
    }

    @Override
    public Map<String, Long> getMostCommonDiseases() {
        log.info("Entering getMostCommonDiseases()");
        Map<String, Long> result = Map.of();
        log.debug("Exiting getMostCommonDiseases: {}", result);
        return result;
    }

    @Override
    public Map<String, Long> getLabTestStatistics() {
        log.info("Entering getLabTestStatistics()");
        List<LaboratoryTest> tests = laboratoryTestRepository.findAll();
        Map<String, Long> stats = new HashMap<>();
        for (LaboratoryTest test : tests) {
            stats.merge(test.getTestName(), 1L, Long::sum);
        }
        log.debug("Exiting getLabTestStatistics: {} tests", stats.size());
        return stats;
    }

    @Override
    public Map<String, Double> getRevenueByDepartment() {
        log.info("Entering getRevenueByDepartment()");
        Map<String, Double> result = Map.of();
        log.debug("Exiting getRevenueByDepartment: {}", result);
        return result;
    }

    @Override
    public Map<String, Double> getMonthlyRevenue() {
        log.info("Entering getMonthlyRevenue()");
        Map<String, Double> result = Map.of();
        log.debug("Exiting getMonthlyRevenue: {}", result);
        return result;
    }

    @Override
    public Map<String, Long> getPatientAgeDistribution() {
        log.info("Entering getPatientAgeDistribution()");
        Map<String, Long> result = Map.of();
        log.debug("Exiting getPatientAgeDistribution: {}", result);
        return result;
    }

    @Override
    public Map<String, Long> getTopBusiestDoctors() {
        log.info("Entering getTopBusiestDoctors()");
        List<Doctor> busiest = doctorRepository.findBusiestDoctors();
        Map<String, Long> result = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(10, busiest.size()); i++) {
            Doctor doctor = busiest.get(i);
            result.put(doctor.getFirstName() + " " + doctor.getLastName(),
                    (long) doctor.getAppointments().size());
        }
        log.debug("Exiting getTopBusiestDoctors: {} doctors", result.size());
        return result;
    }
}
