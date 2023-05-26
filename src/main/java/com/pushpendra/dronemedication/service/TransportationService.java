package com.pushpendra.dronemedication.service;

import com.pushpendra.dronemedication.dto.TransportationDto;
import com.pushpendra.dronemedication.entity.Drone;
import com.pushpendra.dronemedication.entity.Medication;
import com.pushpendra.dronemedication.entity.Transportation;
import com.pushpendra.dronemedication.exception.GeneralException;
import com.pushpendra.dronemedication.repository.TransportationRepository;
import com.pushpendra.dronemedication.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransportationService {
    @Autowired
    private TransportationRepository transportationRepository;
    @Autowired
    private DroneService droneService;
    @Autowired
    private MedicationService medicationService;

    public String loadDroneWithMedications(TransportationDto transportationDto) throws GeneralException, InterruptedException {
        Integer totalWeight = 0;
        String serialNumber = transportationDto.getSerialNumber();
        Drone drone = droneService.getDroneDetails(serialNumber);

        if (drone == null) {
            throw new GeneralException(HttpStatus.NOT_FOUND,"Drone with id " + serialNumber + " is not registered!");
        }
        Integer weightLimit = drone.getWeightLimit();
        Integer batteryCapacity = drone.getBatteryCapacity();
        Integer loadingTimeInMinutes = transportationDto.getLoadingTimeInMinutes();
        Integer tid;
        String state = drone.getState();

        if (!state.equals("IDLE")) {
            throw new GeneralException(HttpStatus.BAD_REQUEST,"Drone is in " + state + " state but it must be in IDLE state to be loaded with Medications");
        }

        List<String> medicationList = new ArrayList<>(transportationDto.getMedications());
        for (String medicationCode : medicationList) {
            Medication medication = medicationService.getMedicationByCode(medicationCode);
            if (medication == null) {
                throw new GeneralException(HttpStatus.NOT_FOUND,"Medication with code " + medicationCode + " is not registered!");
            }
            totalWeight += medication.getWeight();

        }

        if (totalWeight > weightLimit) {
            throw new GeneralException(HttpStatus.BAD_REQUEST,"The Drone can carry only " + weightLimit + " grams but the total weight of medications is " + totalWeight + " grams");
        }
        if (batteryCapacity < 25) {
            throw new GeneralException(HttpStatus.BAD_REQUEST,"The drone battery level is " + batteryCapacity + "% Drone can only carry medications only if battery level is greater than 25 %");
        }

        drone.setState("LOADING");
        droneService.saveDrone(drone);

        Util.sleepInMinutes(loadingTimeInMinutes);

        tid = saveDroneWithMedications(transportationDto);

        drone.setState("LOADED");
        drone.setTid(tid);
        droneService.saveDrone(drone);

        return "Drone with id " + serialNumber + " is successfully is loaded with medications";

    }


    public Integer saveDroneWithMedications(TransportationDto transportationDto){

        Transportation transportation = new Transportation();

        transportation.setSerialNumber(transportationDto.getSerialNumber());
        transportation.setDescription(transportationDto.getDescription());

        List<String> medications = new ArrayList<>(transportationDto.getMedications());

        transportation.setMedications(medications);
        transportation.setState("LOADED");
        transportation.setLoadedDateTime(Util.getCurrentDateTime());

        transportationRepository.save(transportation);

        return transportation.getTid();
    }

    public List<Medication> getLoadedMedications(String serialNumber) throws GeneralException {
        Drone drone = droneService.getDroneDetails(serialNumber);
        List<Medication> medicationList = new ArrayList<>();

        if (drone == null) {
            throw new GeneralException(HttpStatus.NOT_FOUND,"Drone with id " + serialNumber + " is not registered!");
        }

        if (!drone.getState().equals("LOADED")) {
            throw new GeneralException(HttpStatus.BAD_REQUEST,"The State of the given drone is " + drone.getState() + ". Please select Drone which has state 'LOADED'");
        }

        Integer tid = drone.getTid();

        List<String> medications = getMedications(tid);

        for (String medicationCode : medications) {
            Medication medication = medicationService.getMedicationByCode(medicationCode);

            if (medication != null) {
                medicationList.add(medication);
            }

        }

        return medicationList;
    }
   public List<String> getMedications(Integer tid){
        Optional<Transportation> transportation = transportationRepository.findById(tid);
        return transportation.map(Transportation::getMedications).orElse(null);
   }
}