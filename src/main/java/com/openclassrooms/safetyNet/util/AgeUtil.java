package com.openclassrooms.safetyNet.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

// outil de calcul pur
public class AgeUtil {
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Calcule l'âge à partir d'une date de naissance au format MM/dd/yyyy.
     */
    public static Integer calculateAge(String birthdate) {
        LocalDate birth = LocalDate.parse(birthdate, FORMATTER);
        return Period.between(birth, LocalDate.now()).getYears();
    }

    /**
     * Détermine si la personne est un enfant (18 ans ou moins).
     */
    public static boolean isChild(String birthdate) {
        return calculateAge(birthdate) <= 18;
    }
}
