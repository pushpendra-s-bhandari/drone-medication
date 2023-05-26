package com.pushpendra.dronemedication.service;

import com.pushpendra.dronemedication.entity.Medication;
import com.pushpendra.dronemedication.exception.GeneralException;
import com.pushpendra.dronemedication.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class MedicationService {
    @Autowired
    private MedicationRepository medicationRepository;
    @Value("${medication.image.path}")
    private String imagePath;

    public Medication getMedicationByCode(String code){
       Optional<Medication> medication = medicationRepository.findById(code);
       return medication.orElse(null);

    }

    public String registerMedication(Medication medication) throws GeneralException {
        String code = medication.getCode();
        Optional<Medication> existingMedication = medicationRepository.findById(code);
        if (existingMedication.isPresent()){
            throw new GeneralException(HttpStatus.CONFLICT,"Medication with code " + code + " already registered!") ;
        }
        else {
            medicationRepository.save(medication);
            return "Medication with code " + code + " successfully registered!";
        }
    }

    public String uploadMedicationImage(String code, MultipartFile multipartFile) throws GeneralException, IOException {
        Optional<Medication> existingMedication = medicationRepository.findById(code);
        if (!existingMedication.isPresent()){
            throw new GeneralException(HttpStatus.NOT_FOUND,"Medication with code " + code + " is not registered!") ;
        }
        else {
            String imageName = multipartFile.getOriginalFilename();
            String imageFullPath = imagePath + imageName;
            Medication medication = existingMedication.get();
            medication.setImage(imageName);
            medicationRepository.save(medication);
            multipartFile.transferTo(new File(imageFullPath));
            return "Medication Image was successfully uploaded";
        }
    }
}