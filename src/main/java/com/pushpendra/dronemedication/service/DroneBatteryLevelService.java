package com.pushpendra.dronemedication.service;

import com.pushpendra.dronemedication.entity.Drone;
import com.pushpendra.dronemedication.entity.DroneBatteryLevel;
import com.pushpendra.dronemedication.repository.DroneBatteryLevelRepository;
import com.pushpendra.dronemedication.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DroneBatteryLevelService {
    @Autowired
    private DroneBatteryLevelRepository droneBatteryLevelRepository;
    @Autowired
    private DroneService droneService;

    public void saveDroneBatteryLevel(DroneBatteryLevel droneBatteryLevel) {
        droneBatteryLevelRepository.save(droneBatteryLevel);
    }

    @Scheduled(cron = "@hourly")
    public void createDroneBatteryLevelAudit() {
        List<Drone> drones = droneService.getAllDrones();

        for (Drone drone : drones) {
            DroneBatteryLevel droneBatteryLevel = new DroneBatteryLevel();
            droneBatteryLevel.setSerialNumber(drone.getSerialNumber());
            droneBatteryLevel.setBatteryCapacity(drone.getBatteryCapacity());
            droneBatteryLevel.setDateTime(Util.getCurrentDateTime());
            saveDroneBatteryLevel(droneBatteryLevel);
        }
    }
}
