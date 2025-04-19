package com.openclassrooms.safetyNet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetyNet.dto.ChildAlertDTO;
import com.openclassrooms.safetyNet.dto.FireResidentDTO;
import com.openclassrooms.safetyNet.dto.FireResponseDTO;
import com.openclassrooms.safetyNet.dto.HouseholdMemberDTO;
import com.openclassrooms.safetyNet.dto.PersonInfoDTO;
import com.openclassrooms.safetyNet.model.Firestation;
import com.openclassrooms.safetyNet.model.MedicalRecord;
import com.openclassrooms.safetyNet.model.Person;
import com.openclassrooms.safetyNet.repository.FirestationRepository;
import com.openclassrooms.safetyNet.repository.MedicalRecordRepository;
import com.openclassrooms.safetyNet.repository.PersonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonInfoService {
	
	private final PersonAgeService personAgeService;
	private final PersonAccessService personAccessService;
	private final FirestationRepository fireStationRepository;
	private final MedicalRecordRepository medicalRecordRepository;
	private final MedicalRecordService medicalRecordService;
	private final PersonRepository personRepository;

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
		// 1. Récupérer le numéro de la caserne
	    Optional<Firestation> stationOpt = fireStationRepository.findByAddress(address).stream().findFirst();
	    String stationNumber = stationOpt.map(Firestation::getStation).orElse("N/A");

	    // 2. Récupérer les résidents à cette adresse via PersonAccessService ✅
	    List<Person> residents = personAccessService.getResidentsAtAddress(address);

	    // 3. Mapper chaque résident vers un DTO
	    List<FireResidentDTO> residentDTOs = residents.stream()
	        .map(person -> {
	            Integer age = personAgeService.getAgeForPerson(person);
	            MedicalRecord record = medicalRecordRepository
	                .findByFirstNameAndLastName(person.getFirstName(), person.getLastName())
	                .stream()
	                .findFirst()
	                .orElse(null);

	            return new FireResidentDTO(
	                person.getFirstName(),
	                person.getLastName(),
	                person.getPhone(),
	                age != null ? age : 0,
	                record != null ? record.getMedications() : List.of(),
	                record != null ? record.getAllergies() : List.of()
	            );
	        })
	        .collect(Collectors.toList());

	    return new FireResponseDTO(stationNumber, residentDTOs);
	}
	
	public List<PersonInfoDTO> getPersonsByLastName(String lastName) {
	    // 1. Récupérer toutes les personnes ayant ce nom
	    List<Person> persons = personRepository.findByLastName(lastName);

	    // 2. Pour chaque personne, construire le DTO enrichi
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

}
