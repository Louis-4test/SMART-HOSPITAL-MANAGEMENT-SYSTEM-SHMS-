package com.hms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bloodType;
    private String allergies;
    private String chronicConditions;
    private String pastSurgeries;
    private String familyHistory;
    private String insuranceInfo;

    public MedicalRecord() {}

    public MedicalRecord(Long id, String bloodType, String allergies, String chronicConditions) {
        this.id = id;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.chronicConditions = chronicConditions;
    }

    public MedicalRecord(MedicalRecord other) {
        this.id = other.id;
        this.bloodType = other.bloodType;
        this.allergies = other.allergies;
        this.chronicConditions = other.chronicConditions;
        this.pastSurgeries = other.pastSurgeries;
        this.familyHistory = other.familyHistory;
        this.insuranceInfo = other.insuranceInfo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public String getChronicConditions() { return chronicConditions; }
    public void setChronicConditions(String chronicConditions) { this.chronicConditions = chronicConditions; }
    public String getPastSurgeries() { return pastSurgeries; }
    public void setPastSurgeries(String pastSurgeries) { this.pastSurgeries = pastSurgeries; }
    public String getFamilyHistory() { return familyHistory; }
    public void setFamilyHistory(String familyHistory) { this.familyHistory = familyHistory; }
    public String getInsuranceInfo() { return insuranceInfo; }
    public void setInsuranceInfo(String insuranceInfo) { this.insuranceInfo = insuranceInfo; }
}
