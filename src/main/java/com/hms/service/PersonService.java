package com.hms.service;

import com.hms.entity.Person;
import java.util.List;

public interface PersonService {
    Person findById(Long id);
    List<Person> findAll();
    void delete(Long id);
}
