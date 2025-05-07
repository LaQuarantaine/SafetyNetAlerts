package com.openclassrooms.safetyNet.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.Person;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonAccessService {
	
	private final JsonDataStore dataStore;

    public List<Person> getResidentsAtAddress(String address) {
        if (address == null) return Collections.emptyList();
        return dataStore.getPersons().stream()
                .filter(p -> address.equals(p.getAddress()))
                .collect(Collectors.toList());
    }

    public List<Person> getResidentsAtAddresses(Set<String> addresses) {
        if (addresses == null || addresses.isEmpty()) return Collections.emptyList();
        return dataStore.getPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .distinct()
                .collect(Collectors.toList());
    }
    
    public List<Person> getResidentsAtCity(String city) {
    	return dataStore.getPersons().stream()
                .filter(p -> city.equals(p.getCity()))
                .collect(Collectors.toList());
    }
}

