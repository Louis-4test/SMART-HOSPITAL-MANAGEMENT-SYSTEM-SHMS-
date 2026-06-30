package com.hms.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("RECEPTIONIST")
public class Receptionist extends Person {

    private String employeeCode;
    private String shift;

    public Receptionist() {}

    public Receptionist(Long id, String firstName, String lastName, String email, String phone, String address) {
        super(id, firstName, lastName, email, phone, address);
    }

    public Receptionist(Receptionist other) {
        super(other);
        this.employeeCode = other.employeeCode;
        this.shift = other.shift;
    }

    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    @Override
    public void displayProfile() {
        System.out.println("Receptionist: " + getFirstName() + " " + getLastName() + " | Code: " + employeeCode);
    }
}
