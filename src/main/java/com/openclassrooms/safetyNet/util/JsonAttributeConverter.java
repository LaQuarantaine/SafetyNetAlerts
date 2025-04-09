package com.openclassrooms.safetyNet.util;

import jakarta.persistence.AttributeConverter;

// contrat de conversion entre un type Java complexe et une chaîne de caractères, via JSON
// AttributeConverter est un interface de JPA qui convertit <T> en String dans ma BDD
public interface JsonAttributeConverter<T> extends AttributeConverter<T, String> {

}
