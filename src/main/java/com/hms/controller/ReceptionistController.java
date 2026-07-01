package com.hms.controller;

import com.hms.dto.ReceptionistDTO;
import com.hms.entity.Receptionist;
import com.hms.service.ReceptionistService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receptionists")
public class ReceptionistController {

    private final ReceptionistService receptionistService;
    private final ModelMapper modelMapper;

    public ReceptionistController(ReceptionistService receptionistService, ModelMapper modelMapper) {
        this.receptionistService = receptionistService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReceptionistDTO> createReceptionist(@Valid @RequestBody ReceptionistDTO dto) {
        Receptionist receptionist = modelMapper.map(dto, Receptionist.class);
        Receptionist saved = receptionistService.registerReceptionist(receptionist);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(saved, ReceptionistDTO.class));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<ReceptionistDTO>> getAllReceptionists() {
        return ResponseEntity.ok(receptionistService.findAll().stream()
                .map(r -> modelMapper.map(r, ReceptionistDTO.class))
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ReceptionistDTO> getReceptionist(@PathVariable Long id) {
        Receptionist receptionist = receptionistService.findById(id);
        if (receptionist == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(receptionist, ReceptionistDTO.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReceptionistDTO> updateReceptionist(@PathVariable Long id, @Valid @RequestBody ReceptionistDTO dto) {
        Receptionist receptionist = modelMapper.map(dto, Receptionist.class);
        Receptionist updated = receptionistService.update(id, receptionist);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(updated, ReceptionistDTO.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReceptionist(@PathVariable Long id) {
        receptionistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
