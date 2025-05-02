package com.openclassrooms.safetyNet.service;

import com.openclassrooms.safetyNet.model.entity.Firestation;
import com.openclassrooms.safetyNet.repository.FirestationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FirestationService {

    private final FirestationRepository firestationRepository;



    // endpoint 9 Ajout d'un mapping caserne/adresse
    public Firestation save(Firestation firestation) {
        return firestationRepository.save(firestation);
    }


    // endpoint 1 retourne toutes les adresses couvertes pour 1 caserne
    public Set<String> getAddressesForStation(String stationNumber) {
        return firestationRepository.findByStation(stationNumber).stream()
                .map(Firestation::getAddress)
                .collect(Collectors.toSet());
    }
    
    // endpoint 9 Mettre à jour le n° de la caserne pour 1 adresse passée en paramètre
    public List<Firestation> updateStationNumber(String address, String newStationNumber) {
        List<Firestation> stationsAtAddress = firestationRepository.findByAddress(address);

        if (stationsAtAddress.isEmpty()) {
            throw new NoSuchElementException("Aucun mapping trouvé pour l'adresse : " + address);
        }

        // Met à jour le numéro de station pour chaque mapping trouvé
        for (Firestation firestation : stationsAtAddress) {
            firestation.setStation(newStationNumber);
        }

        // Sauvegarde tous les mappings modifiés
        return firestationRepository.saveAll(stationsAtAddress);
    }

    // endpoint 9 Supprimer le mapping d'une adresse
    public void deleteByAddress(String address) {
        firestationRepository.findByAddress(address)
                .forEach(firestationRepository::delete);
    }
    
    // endpoint 9 Supprimer le mapping d'une caserne
    public void deleteByStation(String stationNumber) {
        firestationRepository.findByStation(stationNumber)
            .forEach(firestationRepository::delete);
    }
    
    // endpoint 4 récupère le n° de caserne d'1 adresse
    public Set<String> getStationNumbersByAddress(String address) {
        return firestationRepository.findByAddress(address).stream()
                .map(Firestation::getStation)
                .collect(Collectors.toSet());
    }
}
