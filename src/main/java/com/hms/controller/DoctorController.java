package com.hms.controller;

import com.hms.dto.DoctorDTO;
import com.hms.entity.Doctor;
import com.hms.service.DoctorService;
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
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final ModelMapper modelMapper;

    public DoctorController(DoctorService doctorService, ModelMapper modelMapper) {
        this.doctorService = doctorService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO dto) {
        Doctor doctor = modelMapper.map(dto, Doctor.class);
        Doctor saved = doctorService.registerDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(saved, DoctorDTO.class));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.findAll().stream()
                .map(d -> modelMapper.map(d, DoctorDTO.class))
                .toList());
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<Page<DoctorDTO>> getAllDoctorsPaged(Pageable pageable) {
        return ResponseEntity.ok(doctorService.findAll(pageable)
                .map(d -> modelMapper.map(d, DoctorDTO.class)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<DoctorDTO> getDoctor(@PathVariable Long id) {
        Doctor doctor = doctorService.findById(id);
        if (doctor == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(doctor, DoctorDTO.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorDTO dto) {
        Doctor doctor = modelMapper.map(dto, Doctor.class);
        Doctor updated = doctorService.update(id, doctor);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(updated, DoctorDTO.class));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDTO> partialUpdate(@PathVariable Long id, @RequestBody DoctorDTO dto) {
        Doctor existing = doctorService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) existing.setLastName(dto.getLastName());
        if (dto.getPhone() != null) existing.setPhone(dto.getPhone());
        if (dto.getSpecialization() != null) existing.setSpecialization(dto.getSpecialization());
        if (dto.getYearsOfExperience() != null) existing.setYearsOfExperience(dto.getYearsOfExperience());
        return ResponseEntity.ok(modelMapper.map(doctorService.registerDoctor(existing), DoctorDTO.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/specialization/{specialization}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<DoctorDTO>> getBySpecialization(@PathVariable String specialization) {
        return ResponseEntity.ok(doctorService.findBySpecialization(specialization).stream()
                .map(d -> modelMapper.map(d, DoctorDTO.class))
                .toList());
    }

    @GetMapping("/busiest")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<DoctorDTO>> getBusiestDoctors() {
        return ResponseEntity.ok(doctorService.getBusiestDoctors().stream()
                .map(d -> modelMapper.map(d, DoctorDTO.class))
                .toList());
    }
}
