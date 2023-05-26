package com.pushpendra.dronemedication.repository;

import com.pushpendra.dronemedication.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone,String> {
    List<Drone> findByState(String state);
}
