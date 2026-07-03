package com.hms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("PATIENT")
public class Patient extends Person {

    private LocalDate dateOfBirth;
    private String gender;
    private String bloodType;

    @Column(unique = true)
    private String nationalId;

    private String patientStatus;

    private int severityLevel;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "emergency_contact_id")
    private EmergencyContact emergencyContact;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_address_id")
    private Address patientAddress;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "patient_lab_tests",
        joinColumns = @JoinColumn(name = "patient_id"),
        inverseJoinColumns = @JoinColumn(name = "lab_test_id")
    )
    private List<LaboratoryTest> laboratoryTests = new ArrayList<>();

    public Patient() {}

    public Patient(Long id, String firstName, String lastName, String email, String phone, String address) {
        super(id, firstName, lastName, email, phone, address);
    }

    public Patient(Patient other) {
        super(other);
        this.dateOfBirth = other.dateOfBirth;
        this.gender = other.gender;
        this.bloodType = other.bloodType;
        this.nationalId = other.nationalId;
        this.patientStatus = other.patientStatus;
        this.severityLevel = other.severityLevel;
    }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    public String getPatientStatus() { return patientStatus; }
    public void setPatientStatus(String patientStatus) { this.patientStatus = patientStatus; }
    public int getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(int severityLevel) { this.severityLevel = severityLevel; }
    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; }
    public EmergencyContact getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(EmergencyContact emergencyContact) { this.emergencyContact = emergencyContact; }
    public Address getPatientAddress() { return patientAddress; }
    public void setPatientAddress(Address patientAddress) { this.patientAddress = patientAddress; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
    public List<Prescription> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<Prescription> prescriptions) { this.prescriptions = prescriptions; }
    public List<LaboratoryTest> getLaboratoryTests() { return laboratoryTests; }
    public void setLaboratoryTests(List<LaboratoryTest> laboratoryTests) { this.laboratoryTests = laboratoryTests; }

    @Override
    public void displayProfile() {
        System.out.println("Patient: " + getFirstName() + " " + getLastName() + " | Status: " + patientStatus);
    }

    public double calculateCharges() {
        return 0;
    }

    @Override
    public String generateReport() {
        return "Patient Report: " + getFirstName() + " " + getLastName()
                + " | Status: " + patientStatus
                + " | DOB: " + dateOfBirth
                + " | Gender: " + gender;
    }
}
