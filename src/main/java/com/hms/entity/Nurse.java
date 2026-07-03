package com.hms.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("NURSE")
public class Nurse extends Person {

    private String licenseNumber;
    private String shift;
    private String department;

    public Nurse() {}

    public Nurse(Long id, String firstName, String lastName, String email, String phone, String address) {
        super(id, firstName, lastName, email, phone, address);
    }

    public Nurse(Nurse other) {
        super(other);
        this.licenseNumber = other.licenseNumber;
        this.shift = other.shift;
        this.department = other.department;
    }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public void displayProfile() {
        System.out.println("Nurse: " + getFirstName() + " " + getLastName() + " | Shift: " + shift);
    }

    public double calculateCharges() {
        return 0;
    }

    @Override
    public String generateReport() {
        return "Nurse Report: " + getFirstName() + " " + getLastName()
                + " | Shift: " + shift
                + " | Department: " + department;
    }
}
