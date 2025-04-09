package com.openclassrooms.safetyNet.util;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Converter;

@Converter //Dit à JPA que cette classe est un convertisseur pour une entité
public class StringListJsonConverter implements JsonAttributeConverter<List<String>> {

	// ObjectMapper : classe de la bibliothèque Jackson
	//permet de faire la conversion JSON>Java et Java>JSON
	//utilise Jackson pour transformer les objets en JSON
	private final ObjectMapper mapper = new ObjectMapper(); 

	@Override	//Java > JSON (pour stocker dans SQL)
	public String convertToDatabaseColumn(List<String> attribute) {	//prend une list en paramètre
		try {
            return mapper.writeValueAsString(attribute);	//écrire un objet Java en JSON
        } catch (Exception e) {
            throw new RuntimeException("Erreur de conversion de la liste en JSON", e);
        }
	}

	@Override	// JSON > Java (pour lire depuis SQL)
	public List<String> convertToEntityAttribute(String dbData) {	//prend le contenu de la colonne SQL (texte JSON)
		try {
            return mapper.readValue(dbData, new TypeReference<List<String>>() {});	//transforme en lsite Java
        } catch (Exception e) {
            throw new RuntimeException("Erreur de conversion du JSON en liste", e);
        }
	}

}
