package com.openclassrooms.safetyNet.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Firestation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String address;
	private String station;
}
