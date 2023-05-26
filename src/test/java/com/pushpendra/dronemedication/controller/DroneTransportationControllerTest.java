package com.pushpendra.dronemedication.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pushpendra.dronemedication.entity.Drone;
import com.pushpendra.dronemedication.entity.Medication;
import com.pushpendra.dronemedication.exception.GeneralException;
import com.pushpendra.dronemedication.service.DroneService;
import com.pushpendra.dronemedication.service.MedicationService;
import com.pushpendra.dronemedication.service.TransportationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DroneTransportationController.class)
public class DroneTransportationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    DroneService droneService;
    @MockBean
    TransportationService transportationService;
    @MockBean
    MedicationService medicationService;
    private static final String serialNumber = "droneTest";
    private static final String code = "MED1";
    static Drone drone = new Drone();
    static Drone droneTwo = new Drone();
    static Drone droneThree = new Drone();
    static Medication medication = new Medication();

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

        medication.setCode(code);
        medication.setName("SINEX");
        medication.setWeight(34);


    }

    @Test
    @DisplayName("test rest end point for creating a new drone record")
    public void when_registerDroneWithNewDrone_thenCorrect() throws Exception {
        String expectedOutput = "Drone with id " + serialNumber + " successfully registered!";

        Mockito.when(droneService.registerDrone(drone)).thenReturn(expectedOutput);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/transportation/registerdrone")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(drone));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals(expectedOutput, result.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("test rest end point for creating a new drone record if the newly added drone already exists")
    public void when_registerDroneWithExistingDrone_thenCorrect() throws Exception {
        String expectedOutput = "Drone with id " + serialNumber + " already registered!";

        Mockito.when(droneService.registerDrone(drone)).thenThrow(new GeneralException(HttpStatus.CONFLICT, expectedOutput));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/transportation/registerdrone")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(drone));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Map responseError = objectMapper.readValue(response.getContentAsString(), Map.class);
        String actualOutput = (String) responseError.get("error");
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertTrue(result.getResolvedException() instanceof GeneralException);
        assertEquals(expectedOutput, actualOutput);

    }

    @Test
    @DisplayName("test rest end point for creating a new medication record")
    public void when_registerMedicationWithNewMedication_thenCorrect() throws Exception {
        String expectedOutput = "Medication with code " + code + " successfully registered!";

        Mockito.when(medicationService.registerMedication(medication)).thenReturn(expectedOutput);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/transportation/registermedication")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medication));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals(expectedOutput, result.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("test rest end point for creating a new medication record if the newly added medication already exists")
    public void when_registerMedicationWithExistingMedication_thenCorrect() throws Exception {
        String expectedOutput = "Medication with code " + code + " already registered!";

        Mockito.when(medicationService.registerMedication(medication)).thenThrow(new GeneralException(HttpStatus.CONFLICT, expectedOutput));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/transportation/registermedication")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medication));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Map responseError = objectMapper.readValue(response.getContentAsString(), Map.class);
        String actualOutput = (String) responseError.get("error");
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertTrue(result.getResolvedException() instanceof GeneralException);
        assertEquals(expectedOutput, actualOutput);

    }

    @Test
    @DisplayName("test rest end point for creating a new medication record")
    public void when_getAvailableDroneForLoadingWithRecords_thenCorrect() throws Exception {

        Mockito.when(droneService.getAvailableDroneByState("IDLE")).thenReturn(Arrays.asList(drone, droneTwo));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/transportation/getdronesforloading")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(mockRequest).andReturn();

        MockHttpServletResponse response = result.getResponse();
        List<Drone> droneList = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Drone>>() {
        });
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(2, droneList.size());

    }

    @Test
    @DisplayName("test rest end point for creating a new medication record")
    public void when_getAvailableDroneForLoadingWithNoRecords_thenCorrect() throws Exception {

        Mockito.when(droneService.getAvailableDroneByState("IDLE")).thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/transportation/getdronesforloading")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(mockRequest).andReturn();

        MockHttpServletResponse response = result.getResponse();

        List<Drone> droneList = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Drone>>() {
        });
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(0, droneList.size());
    }
}
