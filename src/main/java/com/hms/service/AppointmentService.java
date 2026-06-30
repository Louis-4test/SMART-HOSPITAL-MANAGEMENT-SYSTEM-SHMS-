package com.hms.service;

import com.hms.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(Appointment appointment);
    Appointment findById(Long id);
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByStatus(String status);
    List<Appointment> findAll();
    Page<Appointment> findAll(Pageable pageable);
    Appointment update(Long id, Appointment appointment);
    void cancelAppointment(Long id);
    void undoCancelAppointment(Long id);
}
