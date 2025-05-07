package com.openclassrooms.safetyNet.service;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.Firestation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FirestationService {

    private final JsonDataStore dataStore;
    private final JsonFileService jsonFileService;


    // endpoint 9 Ajout d'un mapping caserne/adresse
    public Firestation save(Firestation firestation) {
    	dataStore.getFirestations().add(firestation);
        jsonFileService.saveDataToFile();
        return firestation;
    }


    // endpoint 1 retourne toutes les adresses couvertes pour 1 caserne
    public Set<String> getAddressesForStation(String stationNumber) {
    	return dataStore.getFirestations().stream()
                .filter(f -> f.getStation().equals(stationNumber))
                .map(Firestation::getAddress)
                .collect(Collectors.toSet());
    }
    
    // endpoint 9 Mettre à jour le n° de la caserne pour 1 adresse passée en paramètre
    public List<Firestation> updateStationNumber(String address, String newStationNumber) {
    	List<Firestation> updated = new ArrayList<>();
        for (Firestation f : dataStore.getFirestations()) {
            if (f.getAddress().equals(address)) {
                f.setStation(newStationNumber);
                updated.add(f);
            }
        }
        jsonFileService.saveDataToFile();
        if (updated.isEmpty()) {
            throw new NoSuchElementException("Aucun mapping trouvé pour l'adresse : " + address);
        }
        return updated;
    }

    // endpoint 9 Supprimer le mapping d'une adresse
    public void deleteByAddress(String address) {
    	dataStore.getFirestations().removeIf(f -> f.getAddress().equals(address));
        jsonFileService.saveDataToFile();
    }
    
    // endpoint 9 Supprimer le mapping d'une caserne
    public void deleteByStation(String stationNumber) {
    	dataStore.getFirestations().removeIf(f -> f.getStation().equals(stationNumber));
        jsonFileService.saveDataToFile();
    }
    
    // endpoint 4 récupère le n° de caserne d'1 adresse
    public Set<String> getStationNumbersByAddress(String address) {
    	return dataStore.getFirestations().stream()
                .filter(f -> f.getAddress().equals(address))
                .map(Firestation::getStation)
                .collect(Collectors.toSet());
    }
}
