package com.hms.service.impl;

import com.hms.entity.Person;
import com.hms.repository.*;
import com.hms.service.PersonService;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final ReceptionistRepository receptionistRepository;

    public PersonServiceImpl(PatientRepository patientRepository,
                             DoctorRepository doctorRepository,
                             NurseRepository nurseRepository,
                             ReceptionistRepository receptionistRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.nurseRepository = nurseRepository;
        this.receptionistRepository = receptionistRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Override
    public Person findById(Long id) {
        log.info("Entering findById({})", id);
        Person person = patientRepository.findById(id).orElse(null);
        if (person != null) {
            log.debug("Exiting findById: {}", person);
            return person;
        }
        person = doctorRepository.findById(id).orElse(null);
        if (person != null) {
            log.debug("Exiting findById: {}", person);
            return person;
        }
        person = nurseRepository.findById(id).orElse(null);
        if (person != null) {
            log.debug("Exiting findById: {}", person);
            return person;
        }
        log.debug("Exiting findById: {}", person);
        return receptionistRepository.findById(id).orElse(null);
    }

    @Override
    public List<Person> findAll() {
        log.info("Entering findAll()");
        List<Person> result = List.of();
        log.debug("Exiting findAll: {}", result);
        return result;
    }

    @Override
    public void delete(Long id) {
        log.info("Entering delete({})", id);
        Person person = findById(id);
        if (person != null) {
            person.setDeleted(true);
            if (person instanceof com.hms.entity.Patient) {
                patientRepository.save((com.hms.entity.Patient) person);
            } else if (person instanceof com.hms.entity.Doctor) {
                doctorRepository.save((com.hms.entity.Doctor) person);
            } else if (person instanceof com.hms.entity.Nurse) {
                nurseRepository.save((com.hms.entity.Nurse) person);
            } else if (person instanceof com.hms.entity.Receptionist) {
                receptionistRepository.save((com.hms.entity.Receptionist) person);
            }
        }
        log.debug("Exiting delete");
    }
}
