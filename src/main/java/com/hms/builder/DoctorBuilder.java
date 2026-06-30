package com.hms.builder;

import com.hms.entity.Department;
import com.hms.entity.Doctor;

public class DoctorBuilder {

    private final Doctor doctor;

    public DoctorBuilder() {
        this.doctor = new Doctor();
    }

    public DoctorBuilder withFirstName(String firstName) {
        doctor.setFirstName(firstName);
        return this;
    }

    public DoctorBuilder withLastName(String lastName) {
        doctor.setLastName(lastName);
        return this;
    }

    public DoctorBuilder withEmail(String email) {
        doctor.setEmail(email);
        return this;
    }

    public DoctorBuilder withPhone(String phone) {
        doctor.setPhone(phone);
        return this;
    }

    public DoctorBuilder withLicenseNumber(String licenseNumber) {
        doctor.setLicenseNumber(licenseNumber);
        return this;
    }

    public DoctorBuilder withSpecialization(String specialization) {
        doctor.setSpecialization(specialization);
        return this;
    }

    public DoctorBuilder withYearsOfExperience(int years) {
        doctor.setYearsOfExperience(years);
        return this;
    }

    public DoctorBuilder withDepartment(Department department) {
        doctor.setDepartment(department);
        return this;
    }

    public Doctor build() {
        return doctor;
    }
}
