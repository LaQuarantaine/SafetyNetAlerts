package com.openclassrooms.safetyNet.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetyNet.model.Person;
import com.openclassrooms.safetyNet.repository.PersonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonAccessService {
	
	private final PersonRepository personRepository;

    public List<Person> getResidentsAtAddress(String address) {
        if (address == null) return Collections.emptyList();
        return personRepository.findByAddress(address);
    }

    public List<Person> getResidentsAtAddresses(Set<String> addresses) {
        if (addresses == null || addresses.isEmpty()) return Collections.emptyList();
        return personRepository.findByAddressIn(List.copyOf(addresses)).stream()
                .distinct()
                .collect(Collectors.toList());
    }
    
    public List<Person> getResidentsAtCity(String city) {
    	if (city == null) return Collections.emptyList();
    	return personRepository.findByCity(city);
    }
}

