package com.hms.repository;

import com.hms.entity.LaboratoryTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaboratoryTestRepository extends JpaRepository<LaboratoryTest, Long> {

    List<LaboratoryTest> findByStatus(String status);

    List<LaboratoryTest> findByTestNameContainingIgnoreCase(String testName);
}
