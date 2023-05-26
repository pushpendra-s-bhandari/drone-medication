package com.pushpendra.dronemedication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transportation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer tid;

    private String description;

    private String serialNumber;
    private List<String> medications;

    private String state;
    private String loadedDateTime;
    private String deliveredDateTime;
}
