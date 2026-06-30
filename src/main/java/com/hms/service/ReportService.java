package com.hms.service;

import java.util.Map;

public interface ReportService {
    long getTotalPatients();
    Map<String, Long> getPatientsPerDepartment();
    long getPatientsAdmittedToday();
    Map<String, Long> getDoctorWorkload();
    Map<String, Long> getMostCommonDiseases();
    Map<String, Long> getLabTestStatistics();
    Map<String, Double> getRevenueByDepartment();
    Map<String, Double> getMonthlyRevenue();
    Map<String, Long> getPatientAgeDistribution();
    Map<String, Long> getTopBusiestDoctors();
}
