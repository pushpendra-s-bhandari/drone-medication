package com.pushpendra.dronemedication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DroneMedicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroneMedicationApplication.class, args);
    }

}
