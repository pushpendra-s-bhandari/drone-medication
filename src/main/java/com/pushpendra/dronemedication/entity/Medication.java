package com.pushpendra.dronemedication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medication {
    @Id
    @Pattern(regexp = "^[A-Z-0-9_]*$", message = "Only Upper case letters, numbers and _ are allowed")
    private String code;
    @Pattern(regexp = "^[a-zA-Z0-9-_]*$", message = "Only Letter, Numbers, - and _ are allowed")
    private String name;
    private Integer weight;
    private String image;
}