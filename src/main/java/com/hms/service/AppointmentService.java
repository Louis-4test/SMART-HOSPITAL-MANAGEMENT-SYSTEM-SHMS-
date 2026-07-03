package com.hms.service;

import com.hms.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NavigableSet;
import java.util.Queue;

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
    void addToConsultationQueue(Long appointmentId);
    Appointment processNextConsultation();
    Queue<Appointment> getConsultationQueue();
    NavigableSet<Appointment> getSortedAppointments();
}
