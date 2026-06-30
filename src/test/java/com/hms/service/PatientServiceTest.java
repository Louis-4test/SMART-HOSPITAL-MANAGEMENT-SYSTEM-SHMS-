package com.hms.service;

import com.hms.entity.Patient;
import com.hms.repository.PatientRepository;
import com.hms.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    private PatientService patientService;

    @BeforeEach
    void setUp() {
        patientService = new PatientServiceImpl(patientRepository);
    }

    @Test
    void registerPatient_ShouldSaveAndReturn() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setEmail("john@test.com");

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient result = patientService.registerPatient(patient);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(patientRepository).save(patient);
    }

    @Test
    void findById_WhenExists_ShouldReturnPatient() {
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient result = patientService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_WhenNotExists_ShouldReturnNull() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        Patient result = patientService.findById(99L);

        assertNull(result);
    }

    @Test
    void searchPatient_WithId_ShouldReturnList() {
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        List<Patient> results = patientService.searchPatient(1L);

        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId());
    }

    @Test
    void searchPatient_WithName_ShouldReturnMatching() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        when(patientRepository.findByFirstNameContainingIgnoreCase("John")).thenReturn(List.of(patient));
        when(patientRepository.findByLastNameContainingIgnoreCase("John")).thenReturn(List.of());

        List<Patient> results = patientService.searchPatient("John");

        assertEquals(1, results.size());
    }

    @Test
    void delete_ShouldCallRepository() {
        doNothing().when(patientRepository).deleteById(1L);

        patientService.delete(1L);

        verify(patientRepository).deleteById(1L);
    }

    @Test
    void findAll_ShouldReturnAll() {
        when(patientRepository.findAll()).thenReturn(List.of(new Patient(), new Patient()));

        List<Patient> results = patientService.findAll();

        assertEquals(2, results.size());
    }
}
