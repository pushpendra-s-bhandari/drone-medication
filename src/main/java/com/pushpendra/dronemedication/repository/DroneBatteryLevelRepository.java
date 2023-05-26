package com.pushpendra.dronemedication.repository;

import com.pushpendra.dronemedication.entity.DroneBatteryLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneBatteryLevelRepository extends JpaRepository<DroneBatteryLevel,Integer> {
}
