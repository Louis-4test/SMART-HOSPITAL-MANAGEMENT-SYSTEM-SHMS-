package com.hms.service;

import com.hms.entity.Department;
import java.util.List;

public interface DepartmentService {
    Department createDepartment(Department department);
    Department findById(Long id);
    Department findByName(String name);
    List<Department> findAll();
    Department update(Long id, Department department);
    void delete(Long id);
}
