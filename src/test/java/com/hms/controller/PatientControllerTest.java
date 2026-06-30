package com.hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.dto.PatientDTO;
import com.hms.entity.Patient;
import com.hms.service.PatientService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    @WithMockUser(roles = "RECEPTIONIST")
    void createPatient_ShouldReturn201() throws Exception {
        PatientDTO dto = new PatientDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@test.com");
        dto.setPhone("+1234567890");
        dto.setGender("Male");
        dto.setNationalId("NAT-12345");

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");

        when(modelMapper.map(any(PatientDTO.class), eq(Patient.class))).thenReturn(patient);
        when(patientService.registerPatient(any(Patient.class))).thenReturn(patient);
        when(modelMapper.map(any(Patient.class), eq(PatientDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/patients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllPatients_ShouldReturn200() throws Exception {
        when(patientService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/patients"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPatient_WhenFound_ShouldReturn200() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);

        when(patientService.findById(1L)).thenReturn(patient);
        when(modelMapper.map(any(Patient.class), eq(PatientDTO.class))).thenReturn(new PatientDTO());

        mockMvc.perform(get("/api/v1/patients/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPatient_WhenNotFound_ShouldReturn404() throws Exception {
        when(patientService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/patients/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePatient_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/patients/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void createPatient_WithoutAuth_ShouldReturn401() throws Exception {
        PatientDTO dto = new PatientDTO();
        mockMvc.perform(post("/api/v1/patients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }
}
