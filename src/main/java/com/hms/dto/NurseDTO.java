package com.hms.dto;

import jakarta.validation.constraints.*;

public class NurseDTO {

    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\-\\s]{10,15}$")
    private String phone;

    private String licenseNumber;

    @NotBlank
    private String shift;

    private String department;

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
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
