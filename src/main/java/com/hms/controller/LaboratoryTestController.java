package com.hms.controller;

import com.hms.dto.LaboratoryTestDTO;
import com.hms.entity.LaboratoryTest;
import com.hms.service.LaboratoryTestService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lab-tests")
public class LaboratoryTestController {

    private final LaboratoryTestService laboratoryTestService;
    private final ModelMapper modelMapper;

    public LaboratoryTestController(LaboratoryTestService laboratoryTestService, ModelMapper modelMapper) {
        this.laboratoryTestService = laboratoryTestService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<LaboratoryTestDTO> requestTest(@Valid @RequestBody LaboratoryTestDTO dto) {
        LaboratoryTest test = modelMapper.map(dto, LaboratoryTest.class);
        LaboratoryTest saved = laboratoryTestService.requestTest(test);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(saved, LaboratoryTestDTO.class));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<List<LaboratoryTestDTO>> getAllTests() {
        return ResponseEntity.ok(laboratoryTestService.findAll().stream()
                .map(t -> modelMapper.map(t, LaboratoryTestDTO.class))
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<LaboratoryTestDTO> getTest(@PathVariable Long id) {
        LaboratoryTest test = laboratoryTestService.findById(id);
        if (test == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(test, LaboratoryTestDTO.class));
    }

    @PatchMapping("/{id}/result")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<LaboratoryTestDTO> updateResult(@PathVariable Long id, @RequestParam String result) {
        LaboratoryTest test = laboratoryTestService.updateResult(id, result);
        if (test == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(test, LaboratoryTestDTO.class));
    }
}
