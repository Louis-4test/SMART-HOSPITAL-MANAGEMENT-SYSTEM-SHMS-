package com.hms.builder;

import com.hms.entity.*;
import java.time.LocalDate;

public class PatientBuilder {

    private final Patient patient;

    public PatientBuilder() {
        this.patient = new Patient();
    }

    public PatientBuilder withFirstName(String firstName) {
        patient.setFirstName(firstName);
        return this;
    }

    public PatientBuilder withLastName(String lastName) {
        patient.setLastName(lastName);
        return this;
    }

    public PatientBuilder withEmail(String email) {
        patient.setEmail(email);
        return this;
    }

    public PatientBuilder withPhone(String phone) {
        patient.setPhone(phone);
        return this;
    }

    public PatientBuilder withDateOfBirth(LocalDate dateOfBirth) {
        patient.setDateOfBirth(dateOfBirth);
        return this;
    }

    public PatientBuilder withGender(String gender) {
        patient.setGender(gender);
        return this;
    }

    public PatientBuilder withBloodType(String bloodType) {
        patient.setBloodType(bloodType);
        return this;
    }

    public PatientBuilder withNationalId(String nationalId) {
        patient.setNationalId(nationalId);
        return this;
    }

    public PatientBuilder withStatus(String status) {
        patient.setPatientStatus(status);
        return this;
    }

    public PatientBuilder withMedicalRecord(MedicalRecord medicalRecord) {
        patient.setMedicalRecord(medicalRecord);
        return this;
    }

    public PatientBuilder withEmergencyContact(EmergencyContact emergencyContact) {
        patient.setEmergencyContact(emergencyContact);
        return this;
    }

    public PatientBuilder withAddress(Address address) {
        patient.setPatientAddress(address);
        return this;
    }

    public Patient build() {
        return patient;
    }
}
