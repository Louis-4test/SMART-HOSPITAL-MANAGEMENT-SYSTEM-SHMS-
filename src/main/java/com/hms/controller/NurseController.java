package com.hms.controller;

import com.hms.dto.NurseDTO;
import com.hms.entity.Nurse;
import com.hms.service.NurseService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/nurses")
public class NurseController {

    private final NurseService nurseService;
    private final ModelMapper modelMapper;

    public NurseController(NurseService nurseService, ModelMapper modelMapper) {
        this.nurseService = nurseService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NurseDTO> createNurse(@Valid @RequestBody NurseDTO dto) {
        Nurse nurse = modelMapper.map(dto, Nurse.class);
        Nurse saved = nurseService.registerNurse(nurse);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(saved, NurseDTO.class));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<List<NurseDTO>> getAllNurses() {
        return ResponseEntity.ok(nurseService.findAll().stream()
                .map(n -> modelMapper.map(n, NurseDTO.class))
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<NurseDTO> getNurse(@PathVariable Long id) {
        Nurse nurse = nurseService.findById(id);
        if (nurse == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(nurse, NurseDTO.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NurseDTO> updateNurse(@PathVariable Long id, @Valid @RequestBody NurseDTO dto) {
        Nurse nurse = modelMapper.map(dto, Nurse.class);
        Nurse updated = nurseService.update(id, nurse);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(updated, NurseDTO.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNurse(@PathVariable Long id) {
        nurseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
