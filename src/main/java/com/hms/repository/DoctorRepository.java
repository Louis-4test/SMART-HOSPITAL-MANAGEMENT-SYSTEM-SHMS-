package com.hms.repository;

import com.hms.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    Optional<Doctor> findByEmail(String email);

    List<Doctor> findBySpecialization(String specialization);

    List<Doctor> findByDepartmentId(Long departmentId);

    @Query("SELECT d FROM Doctor d WHERE d.firstName LIKE %:keyword% OR d.lastName LIKE %:keyword%")
    List<Doctor> searchByName(@Param("keyword") String keyword);

    @Query("SELECT d FROM Doctor d ORDER BY SIZE(d.appointments) DESC")
    List<Doctor> findBusiestDoctors();

    @Query(value = "SELECT * FROM person WHERE person_type = 'DOCTOR' AND department_id = :deptId", nativeQuery = true)
    List<Doctor> findByDepartmentIdNative(@Param("deptId") Long deptId);
}
