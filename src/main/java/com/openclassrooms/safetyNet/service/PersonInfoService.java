package com.openclassrooms.safetyNet.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetyNet.model.Person;
import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.dto.ChildAlertDTO;
import com.openclassrooms.safetyNet.dto.CommunityEmailDTO;
import com.openclassrooms.safetyNet.dto.FireResidentDTO;
import com.openclassrooms.safetyNet.dto.FireResponseDTO;
import com.openclassrooms.safetyNet.dto.HouseholdMemberDTO;
import com.openclassrooms.safetyNet.dto.PersonInfoDTO;
import com.openclassrooms.safetyNet.model.MedicalRecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonInfoService {
	
	private final PersonAgeService personAgeService;
	private final PersonAccessService personAccessService;
	private final MedicalRecordService medicalRecordService;
	private final JsonDataStore dataStore;

	// service pour traiter le endpoint 1
	public List<ChildAlertDTO> getChildrenByAddress(String address) {
		log.info("Recherche des enfants à l'adresse : {}", address);
		
		List<Person> residents = personAccessService.getResidentsAtAddress(address) ;
		log.debug("{} résident(s) trouvé(s) à cette adresse", residents.size());
		
		List<ChildAlertDTO> children = new ArrayList<>();
		
		for (Person person : residents) {
	        Integer age = personAgeService.getAgeForPerson(person);
	        if (age == null) {
	        	log.warn("Pas de dossier médical pour {} {}", person.getFirstName(), person.getLastName());
				continue;
	        }
	        
	        if (personAgeService.isChild(person)) {
	            // construire la liste des autres membres du foyer
	            List<HouseholdMemberDTO> others = residents.stream()
	                .filter(p -> !p.getFirstName().equals(person.getFirstName()) ||
	                             !p.getLastName().equals(person.getLastName()))
	                .map(p -> new HouseholdMemberDTO(p.getFirstName(), p.getLastName()))
	                .collect(Collectors.toList());

	            children.add(new ChildAlertDTO(
	                person.getFirstName(),
	                person.getLastName(),
	                age,
	                others
	            ));
	        }
		}
		log.info("Nombre d'enfants trouvés à l'adresse {} : {}", address, children.size());
		return children;
	}

	// service pour traiter le endpoint 4
	public FireResponseDTO getResidentsByAddressWithMedicalInfo(String address) {
	    log.info("Récupération des résidents avec infos médicales pour l'adresse : {}", address);

	    String stationNumber = dataStore.getFirestations().stream()
	        .filter(f -> f.getAddress().equals(address))
	        .map(f -> f.getStation())
	        .findFirst()
	        .orElse("N/A");

	    List<Person> residents = personAccessService.getResidentsAtAddress(address);

	    List<FireResidentDTO> residentDTOs = residents.stream()
	        .map(person -> {
	            Optional<MedicalRecord> recordOpt = medicalRecordService
	                .getByName(person.getFirstName(), person.getLastName());

	            return new FireResidentDTO(
	                person.getFirstName(),
	                person.getLastName(),
	                person.getPhone(),
	                recordOpt.map(MedicalRecord::getMedications).orElse(List.of()),
	                recordOpt.map(MedicalRecord::getAllergies).orElse(List.of())
	            );
	        })
	        .collect(Collectors.toList());

	    return new FireResponseDTO(stationNumber, residentDTOs);
	}
	
	public List<PersonInfoDTO> getPersonsByLastName(String lastName) {
	    List<Person> persons = dataStore.getPersons().stream()
	        .filter(p -> p.getLastName().equals(lastName))
	        .collect(Collectors.toList());

	    return persons.stream()
	        .map(person -> {
	            Optional<MedicalRecord> medicalRecord = medicalRecordService
	                .getByName(person.getFirstName(), person.getLastName());

	            int age = personAgeService.getAgeForPerson(person);

	            List<String> medications = medicalRecord.map(MedicalRecord::getMedications).orElse(List.of());
	            List<String> allergies = medicalRecord.map(MedicalRecord::getAllergies).orElse(List.of());

	            return new PersonInfoDTO(
	                person.getFirstName(),
	                person.getLastName(),
	                person.getAddress(),
	                age,
	                person.getEmail(),
	                medications,
	                allergies
	            );
	        })
	        .collect(Collectors.toList());
	}

	public List<CommunityEmailDTO> getEmailByCity(String city) {
		log.info("Recherche des emails par ville : {}", city);
		
		List<Person> residents = personAccessService.getResidentsAtCity(city) ;
		log.debug("{} résident(s) trouvé(s) dans cette ville", residents.size());
		
		Set<CommunityEmailDTO> emailSet = new HashSet<>();
	    for (Person person : residents) {
	        emailSet.add(new CommunityEmailDTO(person.getEmail()));
	    }

	    List<CommunityEmailDTO> emails = new ArrayList<>(emailSet);
	    log.info("Nombre d'emails uniques trouvés pour la ville {} : {}", city, emails.size());

	    return emails;
	}

}
