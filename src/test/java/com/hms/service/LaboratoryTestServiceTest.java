package com.hms.service;

import com.hms.entity.LaboratoryTest;
import com.hms.repository.LaboratoryTestRepository;
import com.hms.service.impl.LaboratoryTestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LaboratoryTestServiceTest {

    @Mock
    private LaboratoryTestRepository laboratoryTestRepository;

    private LaboratoryTestService laboratoryTestService;

    @BeforeEach
    void setUp() {
        laboratoryTestService = new LaboratoryTestServiceImpl(laboratoryTestRepository);
    }

    @Test
    void requestTest_WhenDoctorProvided_ShouldForceRequestedStatusAndSave() {
        LaboratoryTest test = new LaboratoryTest();
        test.setTestName("Complete Blood Count");
        test.setTestDate(LocalDateTime.now());
        test.setRequestedByDoctor("Dr. Alice");
        test.setStatus("DRAFT");

        when(laboratoryTestRepository.save(test)).thenReturn(test);

        LaboratoryTest result = laboratoryTestService.requestTest(test);

        assertNotNull(result);
        assertEquals("REQUESTED", result.getStatus());
        verify(laboratoryTestRepository).save(test);
    }

    @Test
    void requestTest_WhenDoctorMissing_ShouldThrowAndNotSave() {
        LaboratoryTest test = new LaboratoryTest();
        test.setRequestedByDoctor(" ");

        assertThrows(IllegalArgumentException.class, () -> laboratoryTestService.requestTest(test));
        verify(laboratoryTestRepository, never()).save(any());
    }

    @Test
    void updateResult_WhenTestExists_ShouldSetResultAndCompleteStatus() {
        LaboratoryTest test = new LaboratoryTest();
        test.setId(1L);
        test.setStatus("REQUESTED");

        when(laboratoryTestRepository.findById(1L)).thenReturn(Optional.of(test));
        when(laboratoryTestRepository.save(test)).thenReturn(test);

        LaboratoryTest result = laboratoryTestService.updateResult(1L, "Normal");

        assertNotNull(result);
        assertEquals("Normal", result.getResult());
        assertEquals("COMPLETED", result.getStatus());
        verify(laboratoryTestRepository).save(test);
    }

    @Test
    void updateResult_WhenTestMissing_ShouldReturnNullAndNotSave() {
        when(laboratoryTestRepository.findById(99L)).thenReturn(Optional.empty());

        LaboratoryTest result = laboratoryTestService.updateResult(99L, "Normal");

        assertNull(result);
        verify(laboratoryTestRepository, never()).save(any());
    }

    @Test
    void findByStatus_ShouldDelegateToRepository() {
        LaboratoryTest requested = new LaboratoryTest();
        requested.setStatus("REQUESTED");

        when(laboratoryTestRepository.findByStatus("REQUESTED")).thenReturn(List.of(requested));

        List<LaboratoryTest> result = laboratoryTestService.findByStatus("REQUESTED");

        assertEquals(1, result.size());
        assertEquals("REQUESTED", result.get(0).getStatus());
    }
}
