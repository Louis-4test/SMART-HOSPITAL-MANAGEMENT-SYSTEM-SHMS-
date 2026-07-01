package com.hms.service.impl;

import com.hms.entity.Receptionist;
import com.hms.repository.ReceptionistRepository;
import com.hms.service.ReceptionistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReceptionistServiceImpl implements ReceptionistService {

    private final ReceptionistRepository receptionistRepository;
    private static final Logger log = LoggerFactory.getLogger(ReceptionistServiceImpl.class);

    public ReceptionistServiceImpl(ReceptionistRepository receptionistRepository) {
        this.receptionistRepository = receptionistRepository;
    }

    @Override
    public Receptionist registerReceptionist(Receptionist receptionist) {
        log.info("Entering registerReceptionist({})", receptionist);
        Receptionist result = receptionistRepository.save(receptionist);
        log.debug("Exiting registerReceptionist: {}", result);
        return result;
    }

    @Override
    public Receptionist findById(Long id) {
        log.info("Entering findById({})", id);
        Receptionist result = receptionistRepository.findById(id).orElse(null);
        log.debug("Exiting findById: {}", result);
        return result;
    }

    @Override
    public List<Receptionist> findAll() {
        log.info("Entering findAll()");
        List<Receptionist> result = receptionistRepository.findAll();
        log.debug("Exiting findAll: {} results", result.size());
        return result;
    }

    @Override
    public Receptionist update(Long id, Receptionist receptionist) {
        log.info("Entering update({}, {})", id, receptionist);
        Receptionist existing = receptionistRepository.findById(id).orElse(null);
        if (existing == null) {
            log.debug("Exiting update: receptionist not found");
            return null;
        }
        existing.setFirstName(receptionist.getFirstName());
        existing.setLastName(receptionist.getLastName());
        existing.setEmail(receptionist.getEmail());
        existing.setPhone(receptionist.getPhone());
        existing.setEmployeeCode(receptionist.getEmployeeCode());
        existing.setShift(receptionist.getShift());
        Receptionist result = receptionistRepository.save(existing);
        log.debug("Exiting update: {}", result);
        return result;
    }

    @Override
    public void delete(Long id) {
        log.info("Entering delete({})", id);
        receptionistRepository.deleteById(id);
        log.debug("Exiting delete");
    }
}
