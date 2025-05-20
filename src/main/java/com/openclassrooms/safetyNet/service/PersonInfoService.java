package com.openclassrooms.safetyNet.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openclassrooms.safetyNet.model.Person;
import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.dto.ChildAlertDTO;
import com.openclassrooms.safetyNet.dto.CommunityEmailDTO;
import com.openclassrooms.safetyNet.dto.FireResidentDTO;
import com.openclassrooms.safetyNet.dto.FireResponseDTO;
import com.openclassrooms.safetyNet.dto.HouseholdMemberDTO;
import com.openclassrooms.safetyNet.dto.PersonInfoDTO;
import com.openclassrooms.safetyNet.model.Firestation;
import com.openclassrooms.safetyNet.model.MedicalRecord;


@Service
public class PersonInfoService {
	
	private static final Logger log = LoggerFactory.getLogger(PersonInfoService.class);
	
	private final PersonAgeService personAgeService;
	private final PersonAccessService personAccessService;
	private final MedicalRecordService medicalRecordService;
	private final JsonDataStore dataStore;

	
	
	public PersonInfoService(PersonAgeService personAgeService, PersonAccessService personAccessService,
			MedicalRecordService medicalRecordService, JsonDataStore dataStore) {
		super();
		this.personAgeService = personAgeService;
		this.personAccessService = personAccessService;
		this.medicalRecordService = medicalRecordService;
		this.dataStore = dataStore;
	}

	// service pour traiter le endpoint 2
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
	        		    .filter(p -> !(p.getFirstName().equals(person.getFirstName()) &&
	        		                   p.getLastName().equals(person.getLastName())))
	        		    .filter(p -> {
	        		        Integer memberAge = personAgeService.getAgeForPerson(p);
	        		        return memberAge == null || memberAge >= 18;
	        		    })
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
	    
	    String stationNumber = "N/A";
	    for (Firestation firestation : dataStore.getFirestations()) {
	        if (firestation.getAddress().equals(address)) {
	            stationNumber = firestation.getStation();
	            break;
	        }
	    }

	    return new FireResponseDTO(stationNumber, residentDTOs);
	}

	public List<PersonInfoDTO> getPersonsByLastName(String lastName) {
	    List<Person> persons = dataStore.getPersons().stream()
	        .filter(p -> p.getLastName().equals(lastName))
	        .collect(Collectors.toList());

	    return persons.stream()
	    	.filter(p -> personAgeService.getAgeForPerson(p) != null)
	        .map(person -> {
	        	Integer age = personAgeService.getAgeForPerson(person);
	            Optional<MedicalRecord> medicalRecord = medicalRecordService
	                .getByName(person.getFirstName(), person.getLastName());

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
		
		Set<CommunityEmailDTO> emailSet = new LinkedHashSet<>();
	    for (Person person : residents) {
	        emailSet.add(new CommunityEmailDTO(person.getEmail()));
	    }

	    List<CommunityEmailDTO> emails = new ArrayList<>(emailSet);
	    log.info("Nombre d'emails uniques trouvés pour la ville {} : {}", city, emails.size());

	    return emails;
	}

}
