package com.openclassrooms.safetyNet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseholdMemberDTO {
    private String firstName;
    private String lastName;
}