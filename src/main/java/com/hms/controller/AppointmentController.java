package com.hms.controller;

import com.hms.dto.AppointmentDTO;
import com.hms.entity.Appointment;
import com.hms.entity.Doctor;
import com.hms.entity.Patient;
import com.hms.service.AppointmentService;
import com.hms.service.DoctorService;
import com.hms.service.PatientService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ModelMapper modelMapper;

    public AppointmentController(AppointmentService appointmentService,
                                  PatientService patientService,
                                  DoctorService doctorService,
                                  ModelMapper modelMapper) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentDTO dto) {
        Patient patient = patientService.findById(dto.getPatientId());
        Doctor doctor = doctorService.findById(dto.getDoctorId());
        if (patient == null || doctor == null) {
            return ResponseEntity.badRequest().build();
        }
        Appointment appointment = modelMapper.map(dto, Appointment.class);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        Appointment saved = appointmentService.createAppointment(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(saved, AppointmentDTO.class));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.findAll().stream()
                .map(a -> modelMapper.map(a, AppointmentDTO.class))
                .toList());
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<Page<AppointmentDTO>> getAllAppointmentsPaged(Pageable pageable) {
        return ResponseEntity.ok(appointmentService.findAll(pageable)
                .map(a -> modelMapper.map(a, AppointmentDTO.class)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.findById(id);
        if (appointment == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(appointment, AppointmentDTO.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentDTO dto) {
        Appointment appointment = modelMapper.map(dto, Appointment.class);
        Appointment updated = appointmentService.update(id, appointment);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(updated, AppointmentDTO.class));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/undo-cancel")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> undoCancel() {
        appointmentService.undoCancelAppointment(null);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentDTO>> filterAppointments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long doctorId) {
        List<Appointment> results = appointmentService.findAll();
        if (status != null) {
            results = results.stream()
                    .filter(a -> status.equalsIgnoreCase(a.getStatus()))
                    .toList();
        }
        if (doctorId != null) {
            results = results.stream()
                    .filter(a -> a.getDoctor() != null && doctorId.equals(a.getDoctor().getId()))
                    .toList();
        }
        return ResponseEntity.ok(results.stream()
                .map(a -> modelMapper.map(a, AppointmentDTO.class))
                .toList());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
