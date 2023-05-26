package com.pushpendra.dronemedication.repository;

import com.pushpendra.dronemedication.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication,String> {
}
