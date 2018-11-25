package com.yiluhub.airflux.service;

import com.yiluhub.airflux.model.Flight;
import com.yiluhub.airflux.model.OperatingInstruction;

import java.util.List;
import java.util.Optional;

public interface OperationManagementApi {

    // Runs a days flights simulations to populate flight lists
    void plan();

    // Returns a list of all flights or airport flights
    List<Flight> findFlights(Optional<String> airportCode);

    // Returns a list of operating instructions for an aircraft
    List<OperatingInstruction> findOperattingInstructions(String registration);
}
