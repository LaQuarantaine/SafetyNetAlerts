package com.openclassrooms.safetyNet.model.view;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lecture seule
 */
@Entity
@Immutable
@Table(name = "station_address_view")
@Data
@NoArgsConstructor
public class StationAddressView {

    @Id
    @Column(name = "address")
    private String address;

    @Column(name = "station_number")
    private String stationNumber;
}
