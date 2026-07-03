package com.hms.service.impl;

import com.hms.dto.CollectionComparisonDTO;
import com.hms.entity.*;
import com.hms.repository.*;
import com.hms.service.ReportService;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final LaboratoryTestRepository laboratoryTestRepository;
    private final DepartmentRepository departmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final InvoiceRepository invoiceRepository;

    public ReportServiceImpl(PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              LaboratoryTestRepository laboratoryTestRepository,
                              DepartmentRepository departmentRepository,
                              AppointmentRepository appointmentRepository,
                              InvoiceRepository invoiceRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.laboratoryTestRepository = laboratoryTestRepository;
        this.departmentRepository = departmentRepository;
        this.appointmentRepository = appointmentRepository;
        this.invoiceRepository = invoiceRepository;
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
        List<Department> departments = departmentRepository.findAll();
        Map<String, Long> result = new TreeMap<>();
        for (Department dept : departments) {
            long count = dept.getDoctors().stream()
                    .flatMap(d -> d.getAppointments().stream())
                    .map(a -> a.getPatient().getId())
                    .distinct()
                    .count();
            result.put(dept.getName(), count);
        }
        log.debug("Exiting getPatientsPerDepartment: {} departments", result.size());
        return result;
    }

    @Override
    public long getPatientsAdmittedToday() {
        log.info("Entering getPatientsAdmittedToday()");
        long result = patientRepository.findAll().stream()
                .filter(p -> p.getCreatedAt() != null
                        && p.getCreatedAt().toLocalDate().equals(java.time.LocalDate.now()))
                .count();
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
        Map<String, Long> result = appointmentRepository.findAll().stream()
                .filter(a -> a.getReason() != null && !a.getReason().isBlank())
                .collect(Collectors.groupingBy(Appointment::getReason, TreeMap::new, Collectors.counting()));
        log.debug("Exiting getMostCommonDiseases: {} diseases", result.size());
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
        Map<String, Double> result = new TreeMap<>();
        List<Invoice> invoices = invoiceRepository.findAll();
        for (Invoice inv : invoices) {
            if (inv.getAppointment() != null
                    && inv.getAppointment().getDoctor() != null
                    && inv.getAppointment().getDoctor().getDepartment() != null
                    && inv.getTotalAmount() != null) {
                String deptName = inv.getAppointment().getDoctor().getDepartment().getName();
                result.merge(deptName, inv.getTotalAmount(), Double::sum);
            }
        }
        log.debug("Exiting getRevenueByDepartment: {} departments", result.size());
        return result;
    }

    @Override
    public Map<String, Double> getMonthlyRevenue() {
        log.info("Entering getMonthlyRevenue()");
        Map<String, Double> result = new TreeMap<>();
        List<Invoice> invoices = invoiceRepository.findAll();
        for (Invoice inv : invoices) {
            if (inv.getIssueDate() != null && inv.getTotalAmount() != null) {
                String monthKey = inv.getIssueDate().getMonth().toString()
                        + " " + inv.getIssueDate().getYear();
                result.merge(monthKey, inv.getTotalAmount(), Double::sum);
            }
        }
        log.debug("Exiting getMonthlyRevenue: {} months", result.size());
        return result;
    }

    @Override
    public Map<String, Long> getPatientAgeDistribution() {
        log.info("Entering getPatientAgeDistribution()");
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("0-18", 0L);
        result.put("19-30", 0L);
        result.put("31-45", 0L);
        result.put("46-60", 0L);
        result.put("61-80", 0L);
        result.put("80+", 0L);

        java.time.LocalDate today = java.time.LocalDate.now();
        for (Patient p : patientRepository.findAll()) {
            if (p.getDateOfBirth() == null) continue;
            int age = today.getYear() - p.getDateOfBirth().getYear();
            if (today.getDayOfYear() < p.getDateOfBirth().getDayOfYear()) age--;
            if (age <= 18) result.merge("0-18", 1L, Long::sum);
            else if (age <= 30) result.merge("19-30", 1L, Long::sum);
            else if (age <= 45) result.merge("31-45", 1L, Long::sum);
            else if (age <= 60) result.merge("46-60", 1L, Long::sum);
            else if (age <= 80) result.merge("61-80", 1L, Long::sum);
            else result.merge("80+", 1L, Long::sum);
        }
        log.debug("Exiting getPatientAgeDistribution: {} buckets", result.size());
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

    @Override
    public List<CollectionComparisonDTO> getCollectionComparisonTable() {
        log.info("Entering getCollectionComparisonTable()");
        List<CollectionComparisonDTO> result = List.of(
                row("ArrayList vs LinkedList", "Internal", "Dynamic array", "Doubly-linked list"),
                row("ArrayList vs LinkedList", "Get(index)", "O(1)", "O(n)"),
                row("ArrayList vs LinkedList", "Add at end", "O(1) amortized", "O(1)"),
                row("ArrayList vs LinkedList", "Add at front", "O(n)", "O(1)"),
                row("ArrayList vs LinkedList", "Memory", "Low per element", "High (prev/next pointers)"),
                row("ArrayList vs LinkedList", "Best for", "Read-heavy, random access", "Frequent front insertions/deletions"),

                row("HashSet vs TreeSet", "Ordering", "None", "Sorted (Comparable/Comparator)"),
                row("HashSet vs TreeSet", "Duplicates", "Prevented via hash", "Prevented via compareTo"),
                row("HashSet vs TreeSet", "Null elements", "Allows one null", "Not allowed"),
                row("HashSet vs TreeSet", "Performance", "O(1) average", "O(log n)"),

                row("HashMap vs TreeMap", "Key ordering", "None", "Sorted"),
                row("HashMap vs TreeMap", "Performance", "O(1) average", "O(log n)"),
                row("HashMap vs TreeMap", "Null keys", "Allows one", "Not allowed"),
                row("HashMap vs TreeMap", "Best for", "Fast key lookup", "Sorted reports, range queries"),

                row("Queue vs PriorityQueue", "Ordering", "FIFO (arrival)", "Priority (comparator)"),
                row("Queue vs PriorityQueue", "Head element", "First inserted", "Highest priority"),
                row("Queue vs PriorityQueue", "Performance", "O(1) add/remove", "O(log n) add/remove"),
                row("Queue vs PriorityQueue", "Best for", "Fair scheduling", "Emergency triage"),

                row("Stack vs Deque", "Principle", "LIFO", "LIFO + FIFO"),
                row("Stack vs Deque", "Performance", "O(1) push/pop", "O(1) add/remove both ends"),
                row("Stack vs Deque", "Legacy", "Yes (extends Vector)", "No (modern collection)"),
                row("Stack vs Deque", "Best for", "Undo operations", "Navigation history")
        );
        log.debug("Exiting getCollectionComparisonTable: {} rows", result.size());
        return result;
    }

    private CollectionComparisonDTO row(String comparisonGroup, String feature, String firstCollection, String secondCollection) {
        return new CollectionComparisonDTO(comparisonGroup, feature, firstCollection, secondCollection);
    }
}
