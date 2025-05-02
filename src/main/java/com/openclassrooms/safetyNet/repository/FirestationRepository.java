package com.openclassrooms.safetyNet.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetyNet.model.entity.Firestation;

@Repository
public interface FirestationRepository extends JpaRepository<Firestation, Long>{

	List<Firestation> findByStation(String stationNumber);
	List<Firestation> findByAddress(String address);
}
