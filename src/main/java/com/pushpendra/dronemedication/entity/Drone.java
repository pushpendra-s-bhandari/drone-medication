package com.pushpendra.dronemedication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Drone {
    @Id
    @Size(max = 10, message = "Serial Number should not be more than 100 characters")
    private String serialNumber;
    private String model;
    @Max(value = 500, message = "The weight cannot more more than 500 gram")
    private Integer weightLimit;
    private Integer batteryCapacity;
    private String state;

    private Integer tid;
}
