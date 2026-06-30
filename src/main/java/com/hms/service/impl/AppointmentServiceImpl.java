package com.hms.service.impl;

import com.hms.entity.Appointment;
import com.hms.repository.AppointmentRepository;
import com.hms.service.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Stack;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final Stack<Appointment> cancellationUndoStack = new Stack<>();

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    @Override
    public Appointment createAppointment(Appointment appointment) {
        log.info("Entering createAppointment({})", appointment);
        LocalDateTime start = appointment.getAppointmentDate();
        LocalDateTime end = start.plusHours(1);

        List<Appointment> doctorApps = appointmentRepository.findDoctorAppointmentsInRange(
                appointment.getDoctor().getId(), start, end);
        if (!doctorApps.isEmpty()) {
            throw new com.hms.exception.AppointmentConflictException(
                    "Doctor has an overlapping appointment at this time");
        }

        long patientCount = appointmentRepository.countPatientAppointmentsOnDate(
                appointment.getPatient().getId(), start);
        if (patientCount > 0) {
            throw new com.hms.exception.AppointmentConflictException(
                    "Patient already has an appointment at this time");
        }

        Appointment result = appointmentRepository.save(appointment);
        log.debug("Exiting createAppointment: {}", result);
        return result;
    }

    @Override
    public Appointment findById(Long id) {
        log.info("Entering findById({})", id);
        Appointment result = appointmentRepository.findById(id).orElse(null);
        log.debug("Exiting findById: {}", result);
        return result;
    }

    @Override
    public List<Appointment> findByPatientId(Long patientId) {
        log.info("Entering findByPatientId({})", patientId);
        List<Appointment> result = appointmentRepository.findByPatientId(patientId);
        log.debug("Exiting findByPatientId: {} results", result.size());
        return result;
    }

    @Override
    public List<Appointment> findByDoctorId(Long doctorId) {
        log.info("Entering findByDoctorId({})", doctorId);
        List<Appointment> result = appointmentRepository.findByDoctorId(doctorId);
        log.debug("Exiting findByDoctorId: {} results", result.size());
        return result;
    }

    @Override
    public List<Appointment> findByStatus(String status) {
        log.info("Entering findByStatus({})", status);
        List<Appointment> result = appointmentRepository.findByStatus(status);
        log.debug("Exiting findByStatus: {} results", result.size());
        return result;
    }

    @Override
    public List<Appointment> findAll() {
        log.info("Entering findAll()");
        List<Appointment> result = appointmentRepository.findAll();
        log.debug("Exiting findAll: {} results", result.size());
        return result;
    }

    @Override
    public Page<Appointment> findAll(Pageable pageable) {
        log.info("Entering findAll({})", pageable);
        Page<Appointment> result = appointmentRepository.findAll(pageable);
        log.debug("Exiting findAll: {} results", result.getTotalElements());
        return result;
    }

    @Override
    public Appointment update(Long id, Appointment appointment) {
        log.info("Entering update({}, {})", id, appointment);
        Appointment existing = appointmentRepository.findById(id).orElse(null);
        if (existing == null) {
            log.debug("Exiting update: appointment not found");
            return null;
        }
        existing.setAppointmentDate(appointment.getAppointmentDate());
        existing.setStatus(appointment.getStatus());
        existing.setReason(appointment.getReason());
        existing.setNotes(appointment.getNotes());
        Appointment result = appointmentRepository.save(existing);
        log.debug("Exiting update: {}", result);
        return result;
    }

    @Override
    public void cancelAppointment(Long id) {
        log.info("Entering cancelAppointment({})", id);
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment != null) {
            cancellationUndoStack.push(appointment);
            appointment.setStatus("CANCELLED");
            appointmentRepository.save(appointment);
        }
        log.debug("Exiting cancelAppointment");
    }

    @Override
    public void undoCancelAppointment(Long id) {
        log.info("Entering undoCancelAppointment({})", id);
        if (!cancellationUndoStack.isEmpty()) {
            Appointment cancelled = cancellationUndoStack.pop();
            cancelled.setStatus("SCHEDULED");
            appointmentRepository.save(cancelled);
        }
        log.debug("Exiting undoCancelAppointment");
    }
}
