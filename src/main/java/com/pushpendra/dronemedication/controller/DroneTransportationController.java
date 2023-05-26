package com.pushpendra.dronemedication.controller;

import com.pushpendra.dronemedication.dto.TransportationDto;
import com.pushpendra.dronemedication.entity.Drone;
import com.pushpendra.dronemedication.entity.Medication;
import com.pushpendra.dronemedication.exception.GeneralException;
import com.pushpendra.dronemedication.service.DroneService;
import com.pushpendra.dronemedication.service.MedicationService;
import com.pushpendra.dronemedication.service.TransportationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/transportation")
public class DroneTransportationController {
    @Autowired
    private DroneService droneService;
    @Autowired
    private TransportationService transportationService;
    @Autowired
    private MedicationService medicationService;

    @PostMapping("/registerdrone")
    public String registerDrone(@Valid @RequestBody Drone drone) throws GeneralException {

        return droneService.registerDrone(drone);
    }

    @PostMapping("/registermedication")
    public String registerMedication(@Valid @RequestBody Medication medication) throws GeneralException {
        return medicationService.registerMedication(medication);
    }

    @PostMapping("/uploadmedicationimage")
    public String uploadMedicationImage(@RequestParam("code") String code, @RequestParam("image")MultipartFile multipartFile) throws GeneralException, IOException {
        return medicationService.uploadMedicationImage(code,multipartFile);
    }

    @GetMapping("/getdronesforloading")
    public List<Drone> getAvailableDroneForLoading() {

        return droneService.getAvailableDroneByState("IDLE");
    }

    @GetMapping("/getdronesbystate/{state}")
    public List<Drone> getAvailableDroneByState(@PathVariable String state) {
        return droneService.getAvailableDroneByState(state);
    }

    @GetMapping("/getdronebatterylevel/{serialNumber}")
    public String getDroneBatteryLevel(@PathVariable String serialNumber) throws GeneralException {
        return droneService.getDroneBatteryLevel(serialNumber);
    }

    @PostMapping("/loaddroneswithmedications")
    public String loadDroneWithMedications(@RequestBody TransportationDto transportationDto) throws InterruptedException, GeneralException {

        return transportationService.loadDroneWithMedications(transportationDto);

    }

    @GetMapping("/getloadedmedications/{serialNumber}")
    public ResponseEntity<List<Medication>> getLoadedMedications(@PathVariable String serialNumber) throws GeneralException {

        return new ResponseEntity<>(transportationService.getLoadedMedications(serialNumber), HttpStatus.OK);
    }
}