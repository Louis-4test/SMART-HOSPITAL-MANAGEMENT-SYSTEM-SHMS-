package com.hms.service;

import com.hms.dto.CollectionComparisonDTO;
import com.hms.repository.*;
import com.hms.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private LaboratoryTestRepository laboratoryTestRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    private ReportService reportService;

    @BeforeEach
    void setUp() {
        reportService = new ReportServiceImpl(
                patientRepository,
                doctorRepository,
                laboratoryTestRepository,
                departmentRepository,
                appointmentRepository,
                invoiceRepository
        );
    }

    @Test
    void getCollectionComparisonTable_ShouldReturnDocumentedComparisonRows() {
        List<CollectionComparisonDTO> rows = reportService.getCollectionComparisonTable();

        assertEquals(22, rows.size());
        assertTrue(rows.stream().anyMatch(row ->
                "ArrayList vs LinkedList".equals(row.getComparisonGroup())
                        && "Get(index)".equals(row.getFeature())
                        && "O(1)".equals(row.getFirstCollection())
                        && "O(n)".equals(row.getSecondCollection())));
        assertTrue(rows.stream().anyMatch(row ->
                "Stack vs Deque".equals(row.getComparisonGroup())
                        && "Best for".equals(row.getFeature())
                        && "Undo operations".equals(row.getFirstCollection())
                        && "Navigation history".equals(row.getSecondCollection())));
    }
}
