package com.hms.controller;

import com.hms.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/total-patients")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Long> getTotalPatients() {
        return ResponseEntity.ok(reportService.getTotalPatients());
    }

    @GetMapping("/patients-per-department")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Map<String, Long>> getPatientsPerDepartment() {
        return ResponseEntity.ok(reportService.getPatientsPerDepartment());
    }

    @GetMapping("/admitted-today")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Long> getAdmittedToday() {
        return ResponseEntity.ok(reportService.getPatientsAdmittedToday());
    }

    @GetMapping("/doctor-workload")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Map<String, Long>> getDoctorWorkload() {
        return ResponseEntity.ok(reportService.getDoctorWorkload());
    }

    @GetMapping("/common-diseases")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Map<String, Long>> getCommonDiseases() {
        return ResponseEntity.ok(reportService.getMostCommonDiseases());
    }

    @GetMapping("/lab-statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Map<String, Long>> getLabStatistics() {
        return ResponseEntity.ok(reportService.getLabTestStatistics());
    }

    @GetMapping("/revenue-by-department")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Map<String, Double>> getRevenueByDepartment() {
        return ResponseEntity.ok(reportService.getRevenueByDepartment());
    }

    @GetMapping("/monthly-revenue")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Map<String, Double>> getMonthlyRevenue() {
        return ResponseEntity.ok(reportService.getMonthlyRevenue());
    }

    @GetMapping("/age-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Map<String, Long>> getAgeDistribution() {
        return ResponseEntity.ok(reportService.getPatientAgeDistribution());
    }

    @GetMapping("/busiest-doctors")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Map<String, Long>> getBusiestDoctors() {
        return ResponseEntity.ok(reportService.getTopBusiestDoctors());
    }
}
