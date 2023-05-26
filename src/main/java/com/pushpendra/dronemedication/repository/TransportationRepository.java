package com.pushpendra.dronemedication.repository;

import com.pushpendra.dronemedication.entity.Transportation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationRepository extends JpaRepository<Transportation,Integer> {
}
