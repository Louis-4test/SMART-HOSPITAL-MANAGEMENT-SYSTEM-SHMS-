package com.hms.controller;

import com.hms.config.SecurityConfig;
import com.hms.dto.CollectionComparisonDTO;
import com.hms.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
@Import(SecurityConfig.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCollectionComparison_WithAdminRole_ShouldReturnRows() throws Exception {
        when(reportService.getCollectionComparisonTable()).thenReturn(List.of(
                new CollectionComparisonDTO("ArrayList vs LinkedList", "Get(index)", "O(1)", "O(n)")
        ));

        mockMvc.perform(get("/api/v1/reports/collection-comparison"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comparisonGroup").value("ArrayList vs LinkedList"))
                .andExpect(jsonPath("$[0].feature").value("Get(index)"))
                .andExpect(jsonPath("$[0].firstCollection").value("O(1)"))
                .andExpect(jsonPath("$[0].secondCollection").value("O(n)"));
    }

    @Test
    @WithMockUser(roles = "NURSE")
    void getCollectionComparison_WithNurseRole_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/api/v1/reports/collection-comparison"))
                .andExpect(status().isForbidden());
    }
}
