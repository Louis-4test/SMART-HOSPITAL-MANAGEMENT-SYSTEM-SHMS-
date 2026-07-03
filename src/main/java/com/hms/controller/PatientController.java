package com.hms.controller;

import com.hms.dto.PatientDTO;
import com.hms.entity.Patient;
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
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;
    private final ModelMapper modelMapper;

    public PatientController(PatientService patientService, ModelMapper modelMapper) {
        this.patientService = patientService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO dto) {
        Patient patient = modelMapper.map(dto, Patient.class);
        Patient saved = patientService.registerPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(saved, PatientDTO.class));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        return ResponseEntity.ok(patients.stream()
                .map(p -> modelMapper.map(p, PatientDTO.class))
                .toList());
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<Page<PatientDTO>> getAllPatientsPaged(Pageable pageable) {
        Page<Patient> page = patientService.findAll(pageable);
        return ResponseEntity.ok(page.map(p -> modelMapper.map(p, PatientDTO.class)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable Long id) {
        Patient patient = patientService.findById(id);
        if (patient == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(patient, PatientDTO.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDTO dto) {
        Patient patient = modelMapper.map(dto, Patient.class);
        Patient updated = patientService.update(id, patient);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(updated, PatientDTO.class));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<PatientDTO> partialUpdate(@PathVariable Long id, @RequestBody PatientDTO dto) {
        Patient existing = patientService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) existing.setLastName(dto.getLastName());
        if (dto.getEmail() != null) existing.setEmail(dto.getEmail());
        if (dto.getPhone() != null) existing.setPhone(dto.getPhone());
        if (dto.getPatientStatus() != null) existing.setPatientStatus(dto.getPatientStatus());
        Patient saved = patientService.registerPatient(existing);
        return ResponseEntity.ok(modelMapper.map(saved, PatientDTO.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<PatientDTO>> searchPatients(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone) {
        List<Patient> results;
        if (id != null) {
            results = patientService.searchPatient(id);
        } else if (name != null && phone != null) {
            results = patientService.searchPatient(name, phone);
        } else if (name != null) {
            results = patientService.searchPatient(name);
        } else if (email != null) {
            Patient patient = patientService.findByEmail(email);
            results = patient != null ? List.of(patient) : List.of();
        } else {
            results = patientService.searchPatient();
        }
        return ResponseEntity.ok(results.stream()
                .map(p -> modelMapper.map(p, PatientDTO.class))
                .toList());
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<PatientDTO>> filterPatients(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String status) {
        List<Patient> results = patientService.findAll();
        if (gender != null) {
            results = results.stream()
                    .filter(p -> gender.equalsIgnoreCase(p.getGender()))
                    .toList();
        }
        if (status != null) {
            results = results.stream()
                    .filter(p -> status.equalsIgnoreCase(p.getPatientStatus()))
                    .toList();
        }
        return ResponseEntity.ok(results.stream()
                .map(p -> modelMapper.map(p, PatientDTO.class))
                .toList());
    }
}
