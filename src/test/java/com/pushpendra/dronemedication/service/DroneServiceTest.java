package com.pushpendra.dronemedication.service;

import com.pushpendra.dronemedication.entity.Drone;
import com.pushpendra.dronemedication.exception.GeneralException;
import com.pushpendra.dronemedication.repository.DroneRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DroneServiceTest {
    @InjectMocks
    DroneService droneService;

    @Mock
    DroneRepository droneRepository;

    private static final String serialNumber = "droneTest";
    static Drone drone = new Drone();
    static Drone droneTwo = new Drone();
    static Drone droneThree = new Drone();

    @BeforeAll
    public static void setDroneData() {
        drone.setSerialNumber(serialNumber);
        drone.setModel("LightWeight");
        drone.setBatteryCapacity(100);
        drone.setBatteryCapacity(100);
        drone.setWeightLimit(500);
        drone.setState("IDLE");

        droneTwo.setSerialNumber("droneTestTwo");
        droneTwo.setModel("HeavyWeight");
        droneTwo.setBatteryCapacity(50);
        droneTwo.setWeightLimit(500);
        droneTwo.setState("IDLE");

        droneThree.setSerialNumber("droneTestThree");
        droneThree.setModel("HeavyWeight");
        droneThree.setBatteryCapacity(90);
        droneThree.setWeightLimit(500);
        droneThree.setState("LOADED");

    }

    @Test
    @DisplayName("Test if registering of a drone works for a new Drone")
    public void when_registerDroneWithNewDrone_thenCorrect() throws GeneralException {

        String expectedOutput = "Drone with id " + serialNumber + " successfully registered!";

        Mockito.when(droneRepository.findById(serialNumber)).thenReturn(Optional.empty());
        Mockito.when(droneRepository.save(Mockito.any())).thenReturn(drone);

        String actualOutput = droneService.registerDrone(drone);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    @DisplayName("Test if registering of a drone works for an already existing Drone")
    public void when_registerDroneWithExistingDrone_thenCorrect() {
        String expectedOutput = "Drone with id " + serialNumber + " already registered!";

        Mockito.when(droneRepository.findById(serialNumber)).thenReturn(Optional.of(drone));

        GeneralException actualException = assertThrows(GeneralException.class, () -> droneService.registerDrone(drone));

        String actualOutput = actualException.getErrorMsg();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    @DisplayName("Test if it returns battery level of an already existing drone")
    public void when_getDroneBatteryLevelWithExistingDrone_thenCorrect() throws GeneralException {
        String expectedOutput = "The Battery Capacity of Drone " + serialNumber + " is " + drone.getBatteryCapacity() + "%";

        Mockito.when(droneRepository.findById(serialNumber)).thenReturn(Optional.of(drone));

        String actualOutput = droneService.getDroneBatteryLevel(serialNumber);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    @DisplayName("Test if it returns battery level of a non existing drone")
    public void when_getDroneBatteryLevelWithNonExistingDrone_thenCorrect() {
        String expectedOutput = "Drone with id " + serialNumber + " is not registered!";

        Mockito.when(droneRepository.findById(serialNumber)).thenReturn(Optional.empty());

        GeneralException actualException = assertThrows(GeneralException.class, () -> droneService.getDroneBatteryLevel(serialNumber));

        String actualOutput = actualException.getErrorMsg();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    @DisplayName("Test if it returns details of an existing drone")
    public void when_getDroneDetailsWithExistingDrone_thenCorrect() {
        String expectedSerialNumber = drone.getSerialNumber();

        Mockito.when(droneRepository.findById(serialNumber)).thenReturn(Optional.of(drone));

        Drone actualDrone = droneService.getDroneDetails(serialNumber);

        String actualSerialNumber = actualDrone.getSerialNumber();

        assertEquals(expectedSerialNumber, actualSerialNumber);
    }

    @Test
    @DisplayName("Test if it returns details of a non existing drone")
    public void when_getDroneDetailsWithNonExistingDrone_thenCorrect() {

        Mockito.when(droneRepository.findById(serialNumber)).thenReturn(Optional.empty());

        Drone actualDrone = droneService.getDroneDetails(serialNumber);
        assertNull(actualDrone);
    }

    @Test
    @DisplayName("Test if it returns single drone record based on the state")
    public void when_getAvailableDroneByStateWithSingleDroneRecord_thenCorrect() {

        Mockito.when(droneRepository.findByState("LOADED")).thenReturn(Collections.singletonList(droneThree));

        List<Drone> actualDroneList = droneService.getAvailableDroneByState("LOADED");
        assertEquals(1, actualDroneList.size());
    }

    @Test
    @DisplayName("Test if it returns multiple drone record based on the state")
    public void when_getAvailableDroneByStateWithMultipleDroneRecord_thenCorrect() {

        Mockito.when(droneRepository.findByState("IDLE")).thenReturn(Arrays.asList(drone, droneTwo));

        List<Drone> actualDroneList = droneService.getAvailableDroneByState("IDLE");
        assertEquals(2, actualDroneList.size());
    }

    @Test
    @DisplayName("Test if it returns single record if total drones available is only one")
    public void when_getAllDronesWithSingleDroneRecord_thenCorrect() {

        Mockito.when(droneRepository.findAll()).thenReturn(Collections.singletonList(drone));

        List<Drone> actualDroneList = droneService.getAllDrones();
        assertEquals(1, actualDroneList.size());
    }

    @Test
    @DisplayName("Test if it returns all the total record of drones")
    public void when_getAllDronesWithMultipleDroneRecord_thenCorrect() {

        Mockito.when(droneRepository.findAll()).thenReturn(Arrays.asList(drone, droneTwo, droneThree));

        List<Drone> actualDroneList = droneService.getAllDrones();
        assertEquals(3, actualDroneList.size());
    }
}