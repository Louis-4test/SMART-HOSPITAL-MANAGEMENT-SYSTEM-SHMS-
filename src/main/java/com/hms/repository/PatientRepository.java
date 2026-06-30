package com.hms.repository;

import com.hms.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByNationalId(String nationalId);

    List<Patient> findByFirstNameContainingIgnoreCase(String name);

    List<Patient> findByLastNameContainingIgnoreCase(String name);

    List<Patient> findByPatientStatus(String status);

    List<Patient> findByGender(String gender);

    @Query("SELECT p FROM Patient p WHERE p.firstName LIKE %:keyword% OR p.lastName LIKE %:keyword%")
    List<Patient> searchByName(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM person WHERE person_type = 'PATIENT' AND email = :email", nativeQuery = true)
    Optional<Patient> findByEmailNative(@Param("email") String email);
}
