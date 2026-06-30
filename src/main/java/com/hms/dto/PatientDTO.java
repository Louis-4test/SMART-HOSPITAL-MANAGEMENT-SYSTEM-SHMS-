package com.hms.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class PatientDTO {

    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\-\\s]{10,15}$", message = "Invalid phone number")
    private String phone;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank
    private String gender;

    private String bloodType;

    @NotBlank
    private String nationalId;

    private String patientStatus;

    private MedicalRecordDTO medicalRecord;
    private EmergencyContactDTO emergencyContact;
    private AddressDTO patientAddress;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
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
    public MedicalRecordDTO getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecordDTO medicalRecord) { this.medicalRecord = medicalRecord; }
    public EmergencyContactDTO getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(EmergencyContactDTO emergencyContact) { this.emergencyContact = emergencyContact; }
    public AddressDTO getPatientAddress() { return patientAddress; }
    public void setPatientAddress(AddressDTO patientAddress) { this.patientAddress = patientAddress; }
}
