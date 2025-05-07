package com.openclassrooms.safetyNet.dto;

import lombok.Data;
import java.util.List;

@Data
public class MedicalRecordDTO {
    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;
}