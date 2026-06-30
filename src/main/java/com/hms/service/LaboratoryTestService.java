package com.hms.service;

import com.hms.entity.LaboratoryTest;
import java.util.List;

public interface LaboratoryTestService {
    LaboratoryTest requestTest(LaboratoryTest test);
    LaboratoryTest findById(Long id);
    List<LaboratoryTest> findByStatus(String status);
    List<LaboratoryTest> findAll();
    LaboratoryTest updateResult(Long id, String result);
}
