package com.hms.service;

import com.hms.entity.Receptionist;
import java.util.List;

public interface ReceptionistService {
    Receptionist registerReceptionist(Receptionist receptionist);
    Receptionist findById(Long id);
    List<Receptionist> findAll();
    Receptionist update(Long id, Receptionist receptionist);
    void delete(Long id);
}
