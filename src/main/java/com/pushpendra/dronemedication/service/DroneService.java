/**
 * Drone Service Class
 * This contains all the service related logics.
 *
 * @author  pushpendra
 * @version 1.0
 */
package com.pushpendra.dronemedication.service;

import com.pushpendra.dronemedication.entity.Drone;
import com.pushpendra.dronemedication.exception.GeneralException;
import com.pushpendra.dronemedication.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DroneService {
    @Autowired
    private DroneRepository droneRepository;

    public String registerDrone(Drone drone) throws GeneralException {
        String serialNumber = drone.getSerialNumber();
        Optional<Drone> existingDrone = droneRepository.findById(serialNumber);
        if (existingDrone.isPresent()) {
            throw new GeneralException(HttpStatus.CONFLICT, "Drone with id " + serialNumber + " already registered!");
        } else {
            droneRepository.save(drone);
            return "Drone with id " + serialNumber + " successfully registered!";
        }

    }

    public void saveDrone(Drone drone) {
        droneRepository.save(drone);
    }

    public List<Drone> getAvailableDroneByState(String state) {

        return droneRepository.findByState(state);
    }

    public String getDroneBatteryLevel(String serialNumber) throws GeneralException {
        Optional<Drone> existingDrone = droneRepository.findById(serialNumber);
        if (existingDrone.isPresent()) {
            Drone drone = existingDrone.get();
            return "The Battery Capacity of Drone " + serialNumber + " is " + drone.getBatteryCapacity() + "%";
        } else {
            throw new GeneralException(HttpStatus.NOT_FOUND,"Drone with id " + serialNumber + " is not registered!");
        }
    }

    public Drone getDroneDetails(String serialNumber) {
        Optional<Drone> existingDrone = droneRepository.findById(serialNumber);
        return existingDrone.orElse(null);
    }

    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

}