package com.hms.service.impl;

import com.hms.entity.Nurse;
import com.hms.repository.NurseRepository;
import com.hms.service.NurseService;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;

    public NurseServiceImpl(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(NurseServiceImpl.class);

    @Override
    public Nurse registerNurse(Nurse nurse) {
        log.info("Entering registerNurse({})", nurse);
        Nurse result = nurseRepository.save(nurse);
        log.debug("Exiting registerNurse: {}", result);
        return result;
    }

    @Override
    public Nurse findById(Long id) {
        log.info("Entering findById({})", id);
        Nurse result = nurseRepository.findById(id).orElse(null);
        log.debug("Exiting findById: {}", result);
        return result;
    }

    @Override
    public List<Nurse> findByDepartment(String department) {
        log.info("Entering findByDepartment({})", department);
        List<Nurse> result = nurseRepository.findByDepartment(department);
        log.debug("Exiting findByDepartment: {} results", result.size());
        return result;
    }

    @Override
    public List<Nurse> findAll() {
        log.info("Entering findAll()");
        List<Nurse> result = nurseRepository.findAll();
        log.debug("Exiting findAll: {} results", result.size());
        return result;
    }

    @Override
    public Nurse update(Long id, Nurse nurse) {
        log.info("Entering update({}, {})", id, nurse);
        Nurse existing = nurseRepository.findById(id).orElse(null);
        if (existing == null) {
            log.debug("Exiting update: nurse not found");
            return null;
        }
        existing.setFirstName(nurse.getFirstName());
        existing.setLastName(nurse.getLastName());
        existing.setEmail(nurse.getEmail());
        existing.setPhone(nurse.getPhone());
        existing.setShift(nurse.getShift());
        existing.setDepartment(nurse.getDepartment());
        Nurse result = nurseRepository.save(existing);
        log.debug("Exiting update: {}", result);
        return result;
    }

    @Override
    public void delete(Long id) {
        log.info("Entering delete({})", id);
        nurseRepository.deleteById(id);
        log.debug("Exiting delete");
    }
}
