package com.hms.service.impl;

import com.hms.entity.Department;
import com.hms.repository.DepartmentRepository;
import com.hms.service.DepartmentService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    @Override
    public Department createDepartment(Department department) {
        log.info("Entering createDepartment({})", department);
        Department result = departmentRepository.save(department);
        log.debug("Exiting createDepartment: {}", result);
        return result;
    }

    @Override
    public Department findById(Long id) {
        log.info("Entering findById({})", id);
        Department result = departmentRepository.findById(id).orElse(null);
        log.debug("Exiting findById: {}", result);
        return result;
    }

    @Override
    public Department findByName(String name) {
        log.info("Entering findByName({})", name);
        Department result = departmentRepository.findByName(name).orElse(null);
        log.debug("Exiting findByName: {}", result);
        return result;
    }

    @Override
    public List<Department> findAll() {
        log.info("Entering findAll()");
        List<Department> result = departmentRepository.findAll();
        log.debug("Exiting findAll: {} results", result.size());
        return result;
    }

    @Override
public Department update(Long id, Department department) {
    log.info("Entering update({}, {})", id, department);
    
    // Use .orElseThrow() to halt execution cleanly if the ID isn't located
    Department existing = departmentRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        
    existing.setName(department.getName());
    existing.setDescription(department.getDescription());
    existing.setLocation(department.getLocation());
    
    Department result = departmentRepository.save(existing);
    log.debug("Exiting update: {}", result);
    return result;
}

    @Override
    public void delete(Long id) {
        log.info("Entering delete({})", id);
        departmentRepository.deleteById(id);
        log.debug("Exiting delete");
    }
}
