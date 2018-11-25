package com.yiluhub.airflux.service;

import com.yiluhub.airflux.persistence.entity.Aircraft;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AircraftTrackingApi {

    // Initialize the trackng map
    void init();

    // Returns a list of aircrafts that are ready to take off
    Optional<Aircraft> availableAircraft(String airportCode, LocalDateTime departure);

    // Moves the aircraft from origin airport to destination airport
    void moveAircraft(String origin, String destination, Aircraft aircraft, LocalDateTime arrivalTime);

    //  Return a list of aircraft to Position their hub
    List<Aircraft> aircraftToPosition(String airportCode, int remaingFlightsCount);
}
