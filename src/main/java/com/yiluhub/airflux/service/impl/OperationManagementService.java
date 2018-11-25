package com.yiluhub.airflux.service.impl;

import com.yiluhub.airflux.logging.AirLogger;
import com.yiluhub.airflux.model.Flight;
import com.yiluhub.airflux.model.OperatingInstruction;
import com.yiluhub.airflux.persistence.entity.Aircraft;
import com.yiluhub.airflux.persistence.entity.Airport;
import com.yiluhub.airflux.persistence.entity.Schedule;
import com.yiluhub.airflux.service.AircraftTrackingApi;
import com.yiluhub.airflux.service.AirportApi;
import com.yiluhub.airflux.service.OperationManagementApi;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import static java.util.stream.Collectors.toList;

/*
    Responsible to assign the avaiable aircraft to schedule
    and provide a list of Flights
 */
@Service
public class OperationManagementService implements OperationManagementApi, AirLogger {

    AirportApi airportService;
    AircraftTrackingApi aircraftTrackingService;
    List<Flight> flights;

    // Map of Aircraft registration as key and OperatingInstruction as value
    Map<String, List<OperatingInstruction>> operatingInstructionMap;


    public OperationManagementService(AircraftTrackingApi aircraftTrackingService,
                                      AirportApi airportService) {
        this.aircraftTrackingService = aircraftTrackingService;
        this.airportService = airportService;
        this.flights = new ArrayList<>();
        this.operatingInstructionMap = new HashMap<>();
    }

    /*
        TODO Missing after planing all flights, I should position all the aircraft to its hub
        TODO this is not implemented but it is easy to implement, after flight planning check
        TODO each airport if there are aircraft that doesn't belong to it, schedule return home flight
     */
    public void plan() {
        airportService
                .allSchedules()
                .stream()
                .forEach(schedule -> {
                   Optional<Aircraft> aircraft = aircraftTrackingService
                            .availableAircraft(schedule.getOrigin().getCode(), schedule.getDeparture());

                   if(aircraft.isPresent()) {

                       addFlight(schedule, aircraft.get());
                       addOperatingInstruction(schedule, aircraft.get());

                       //If number of aircraft at the airport is greater than remaining flights
                       // position idle aircraft to home hub
                       addPositioningFlights(schedule);
                   } else {
                       String message = String.format("No available aircraft for schedule id %s", schedule.getId());
                       getLogger().error(message);
                   }
                });
    }

    // Returns a list of all flights or airport flights
    public List<Flight> findFlights(Optional<String> airportCode) {
        return airportCode
                .map(airport -> flights
                        .stream()
                        .filter(flight -> flight.getOrigin().equals(airport))
                        .collect(toList())
                )
                .orElseGet(() -> flights);
    }

    // Returns a list of operating instructions for an aircraft
    public List<OperatingInstruction> findOperattingInstructions(String registration) {
       return operatingInstructionMap.getOrDefault(registration, new ArrayList<>());
    }

    private void addOperatingInstruction(Schedule schedule, Aircraft aircraft) {
        operatingInstructionMap.compute(aircraft.getRegistration(),
                (registration, instrucitons) -> {
                    OperatingInstruction operatingInstruction = new OperatingInstruction(
                            schedule.getOrigin().getCode(),
                            schedule.getDestination().getCode(),
                            schedule.getDeparture());

                  return Optional.ofNullable(instrucitons)
                            .map(list -> {
                                list.add(operatingInstruction);
                                return list;
                            })
                            .orElse(new ArrayList<>(Arrays.asList(operatingInstruction)));
                });
    }

    private void addFlight(Schedule schedule, Aircraft aircraft) {

        aircraftTrackingService.moveAircraft(
                schedule.getOrigin().getCode(),
                schedule.getDestination().getCode(),
                aircraft,
                schedule.getDeparture().plus(schedule.getFlightTime())
        );

        Flight flight = new Flight(
                schedule.getOrigin().getCode(),
                schedule.getDestination().getCode(),
                schedule.getDeparture(),
                schedule.getDeparture().plus(schedule.getFlightTime()),
                aircraft.getModel());

        flights.add(flight);
    }

    private void addPositioningFlights(Schedule schedule) {

        int remaingFlightsCount = airportService.countRemainingSchedules(schedule.getOrigin().getCode(),
                schedule.getDeparture());

        aircraftTrackingService
                .aircraftToPosition(schedule.getOrigin().getCode(), remaingFlightsCount)
                .stream()
                .forEach(aircraft -> {
                    Schedule positioningSchedule = createPositioningSchedule(schedule, aircraft);
                    addFlight(positioningSchedule, aircraft);
                });
    }

    private Schedule createPositioningSchedule(Schedule schedule, Aircraft aircraft) {
        Airport origin =  schedule.getOrigin();
        Airport destination =
                airportService.airports(aircraft.getHome().getCode())
                        .stream().findFirst().get();

        LocalDateTime departure = schedule.getDeparture();
        Duration flightDuration = airportService.flightDuration(origin.getCode(), destination.getCode());

        return new Schedule(null, departure, origin, destination, flightDuration);
    }
}

