package com.hms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "emergency_contacts")
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String relationship;
    private String phone;
    private String alternatePhone;

    public EmergencyContact() {}

    public EmergencyContact(Long id, String fullName, String relationship, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.relationship = relationship;
        this.phone = phone;
    }

    public EmergencyContact(EmergencyContact other) {
        this.id = other.id;
        this.fullName = other.fullName;
        this.relationship = other.relationship;
        this.phone = other.phone;
        this.alternatePhone = other.alternatePhone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAlternatePhone() { return alternatePhone; }
    public void setAlternatePhone(String alternatePhone) { this.alternatePhone = alternatePhone; }
}
