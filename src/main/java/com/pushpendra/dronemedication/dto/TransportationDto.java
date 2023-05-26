package com.pushpendra.dronemedication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportationDto {
    private String serialNumber;
    private String description;
    private Integer loadingTimeInMinutes;
    private List<String> medications;
}
