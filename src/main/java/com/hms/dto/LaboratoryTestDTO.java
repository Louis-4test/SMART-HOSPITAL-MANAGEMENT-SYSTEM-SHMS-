package com.hms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class LaboratoryTestDTO {

    private Long id;

    @NotBlank
    private String testName;

    @NotNull
    private LocalDateTime testDate;

    private String result;
    private String status;

    @NotBlank
    private String requestedByDoctor;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public LocalDateTime getTestDate() { return testDate; }
    public void setTestDate(LocalDateTime testDate) { this.testDate = testDate; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRequestedByDoctor() { return requestedByDoctor; }
    public void setRequestedByDoctor(String requestedByDoctor) { this.requestedByDoctor = requestedByDoctor; }
}
