package com.openclassrooms.safetyNet.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetyNet.model.Firestation;

@Repository
public interface FirestationRepository extends JpaRepository<Firestation, Long>{

}
