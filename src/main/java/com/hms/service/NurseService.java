package com.hms.service;

import com.hms.entity.Nurse;
import java.util.List;

public interface NurseService {
    Nurse registerNurse(Nurse nurse);
    Nurse findById(Long id);
    List<Nurse> findByDepartment(String department);
    List<Nurse> findAll();
    Nurse update(Long id, Nurse nurse);
    void delete(Long id);
}
