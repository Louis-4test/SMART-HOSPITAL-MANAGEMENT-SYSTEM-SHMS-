package com.hms.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("DOCTOR")
public class Doctor extends Person {

    @Column(unique = true)
    private String licenseNumber;

    private String specialization;
    private Integer yearsOfExperience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    public Doctor() {}

    public Doctor(Long id, String firstName, String lastName, String email, String phone, String address) {
        super(id, firstName, lastName, email, phone, address);
    }

    public Doctor(Doctor other) {
        super(other);
        this.licenseNumber = other.licenseNumber;
        this.specialization = other.specialization;
        this.yearsOfExperience = other.yearsOfExperience;
    }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    @Override
    public void displayProfile() {
        System.out.println("Dr. " + getFirstName() + " " + getLastName() + " | " + specialization);
    }

    public double calculateCharges() {
        return 0;
    }
}
