package com.hms.service.impl;

import com.hms.entity.LaboratoryTest;
import com.hms.repository.LaboratoryTestRepository;
import com.hms.service.LaboratoryTestService;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LaboratoryTestServiceImpl implements LaboratoryTestService {

    private final LaboratoryTestRepository laboratoryTestRepository;

    public LaboratoryTestServiceImpl(LaboratoryTestRepository laboratoryTestRepository) {
        this.laboratoryTestRepository = laboratoryTestRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(LaboratoryTestServiceImpl.class);

    @Override
    public LaboratoryTest requestTest(LaboratoryTest test) {
        log.info("Entering requestTest({})", test);
        if (test.getRequestedByDoctor() == null || test.getRequestedByDoctor().isBlank()) {
            throw new IllegalArgumentException("Laboratory tests require a doctor's request");
        }
        test.setStatus("REQUESTED");
        LaboratoryTest result = laboratoryTestRepository.save(test);
        log.debug("Exiting requestTest: {}", result);
        return result;
    }

    @Override
    public LaboratoryTest findById(Long id) {
        log.info("Entering findById({})", id);
        LaboratoryTest result = laboratoryTestRepository.findById(id).orElse(null);
        log.debug("Exiting findById: {}", result);
        return result;
    }

    @Override
    public List<LaboratoryTest> findByStatus(String status) {
        log.info("Entering findByStatus({})", status);
        List<LaboratoryTest> result = laboratoryTestRepository.findByStatus(status);
        log.debug("Exiting findByStatus: {} results", result.size());
        return result;
    }

    @Override
    public List<LaboratoryTest> findAll() {
        log.info("Entering findAll()");
        List<LaboratoryTest> result = laboratoryTestRepository.findAll();
        log.debug("Exiting findAll: {} results", result.size());
        return result;
    }

    @Override
    public LaboratoryTest updateResult(Long id, String result) {
        log.info("Entering updateResult({}, {})", id, result);
        LaboratoryTest test = laboratoryTestRepository.findById(id).orElse(null);
        if (test == null) {
            log.debug("Exiting updateResult: test not found");
            return null;
        }
        test.setResult(result);
        test.setStatus("COMPLETED");
        LaboratoryTest saved = laboratoryTestRepository.save(test);
        log.debug("Exiting updateResult: {}", saved);
        return saved;
    }
}
