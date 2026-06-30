package com.hms.controller;

import com.hms.dto.PrescriptionDTO;
import com.hms.entity.Patient;
import com.hms.entity.Prescription;
import com.hms.service.PatientService;
import com.hms.service.PrescriptionService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final PatientService patientService;
    private final ModelMapper modelMapper;

    public PrescriptionController(PrescriptionService prescriptionService,
                                   PatientService patientService,
                                   ModelMapper modelMapper) {
        this.prescriptionService = prescriptionService;
        this.patientService = patientService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionDTO> createPrescription(@Valid @RequestBody PrescriptionDTO dto) {
        Patient patient = patientService.findById(dto.getPatientId());
        if (patient == null) return ResponseEntity.badRequest().build();
        Prescription prescription = modelMapper.map(dto, Prescription.class);
        prescription.setPatient(patient);
        Prescription saved = prescriptionService.createPrescription(prescription);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(saved, PrescriptionDTO.class));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PrescriptionDTO>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionService.findAll().stream()
                .map(p -> modelMapper.map(p, PrescriptionDTO.class))
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionDTO> getPrescription(@PathVariable Long id) {
        Prescription prescription = prescriptionService.findById(id);
        if (prescription == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(prescription, PrescriptionDTO.class));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PrescriptionDTO>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.findByPatientId(patientId).stream()
                .map(p -> modelMapper.map(p, PrescriptionDTO.class))
                .toList());
    }
}
