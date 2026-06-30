package com.hms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "laboratory_tests")
@EntityListeners(AuditingEntityListener.class)
public class LaboratoryTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testName;
    private LocalDateTime testDate;
    private String result;
    private String status;

    private String requestedByDoctor;

    @ManyToMany(mappedBy = "laboratoryTests")
    private List<Patient> patients = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    public LaboratoryTest() {}

    public LaboratoryTest(Long id, String testName, LocalDateTime testDate, String status) {
        this.id = id;
        this.testName = testName;
        this.testDate = testDate;
        this.status = status;
    }

    public LaboratoryTest(LaboratoryTest other) {
        this.id = other.id;
        this.testName = other.testName;
        this.testDate = other.testDate;
        this.result = other.result;
        this.status = other.status;
        this.requestedByDoctor = other.requestedByDoctor;
    }

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
    public List<Patient> getPatients() { return patients; }
    public void setPatients(List<Patient> patients) { this.patients = patients; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
