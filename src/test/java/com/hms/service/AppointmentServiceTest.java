package com.hms.service;

import com.hms.entity.Appointment;
import com.hms.entity.Doctor;
import com.hms.entity.Patient;
import com.hms.exception.AppointmentConflictException;
import com.hms.repository.AppointmentRepository;
import com.hms.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentServiceImpl(appointmentRepository);
    }

    @Test
    void createAppointment_WhenNoConflict_ShouldSucceed() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(1L);

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(1));

        when(appointmentRepository.findDoctorAppointmentsInRange(any(), any(), any()))
                .thenReturn(List.of());
        when(appointmentRepository.countPatientAppointmentsOnDate(any(), any()))
                .thenReturn(0L);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment result = appointmentService.createAppointment(appointment);

        assertNotNull(result);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void createAppointment_WhenDoctorConflict_ShouldThrow() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(1L);

        Appointment existing = new Appointment();
        existing.setId(2L);

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(1));

        when(appointmentRepository.findDoctorAppointmentsInRange(any(), any(), any()))
                .thenReturn(List.of(existing));

        assertThrows(AppointmentConflictException.class,
                () -> appointmentService.createAppointment(appointment));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void createAppointment_WhenPatientConflict_ShouldThrow() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        Patient patient = new Patient();
        patient.setId(1L);

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(1));

        when(appointmentRepository.findDoctorAppointmentsInRange(any(), any(), any()))
                .thenReturn(List.of());
        when(appointmentRepository.countPatientAppointmentsOnDate(any(), any()))
                .thenReturn(1L);

        assertThrows(AppointmentConflictException.class,
                () -> appointmentService.createAppointment(appointment));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void cancelAndUndo_ShouldWork() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus("SCHEDULED");

        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        appointmentService.cancelAppointment(1L);

        assertEquals("CANCELLED", appointment.getStatus());

        appointmentService.undoCancelAppointment(null);

        assertEquals("SCHEDULED", appointment.getStatus());
    }
}
