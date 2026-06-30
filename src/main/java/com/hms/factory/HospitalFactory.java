package com.hms.factory;

import com.hms.entity.*;

public class HospitalFactory {

    public static Patient createPatient(String firstName, String lastName, String email, String phone) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setEmail(email);
        patient.setPhone(phone);
        patient.setPatientStatus("REGISTERED");
        return patient;
    }

    public static Doctor createDoctor(String firstName, String lastName, String email, String phone,
                                       String licenseNumber, String specialization) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setEmail(email);
        doctor.setPhone(phone);
        doctor.setLicenseNumber(licenseNumber);
        doctor.setSpecialization(specialization);
        return doctor;
    }

    public static Nurse createNurse(String firstName, String lastName, String email, String shift) {
        Nurse nurse = new Nurse();
        nurse.setFirstName(firstName);
        nurse.setLastName(lastName);
        nurse.setEmail(email);
        nurse.setShift(shift);
        return nurse;
    }

    public static Department createDepartment(String name, String description) {
        Department department = new Department();
        department.setName(name);
        department.setDescription(description);
        return department;
    }

    public static Appointment createAppointment(Patient patient, Doctor doctor, String reason) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setReason(reason);
        appointment.setStatus("SCHEDULED");
        return appointment;
    }
}
