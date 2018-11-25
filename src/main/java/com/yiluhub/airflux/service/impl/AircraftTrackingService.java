package com.yiluhub.airflux.service.impl;

import com.yiluhub.airflux.exception.ApplicationException;
import com.yiluhub.airflux.logging.AirLogger;
import com.yiluhub.airflux.model.AircraftTracking;
import com.yiluhub.airflux.persistence.entity.Aircraft;
import com.yiluhub.airflux.persistence.entity.Airport;
import com.yiluhub.airflux.persistence.repository.AircraftRepository;
import com.yiluhub.airflux.service.AircraftTrackingApi;
import com.yiluhub.airflux.service.AirportApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import static java.util.stream.Collectors.toList;

import static java.util.Optional.empty;

/*
    Responsible for tracking the aircraft
 */
@Service
public class AircraftTrackingService implements AircraftTrackingApi,AirLogger {

    private AircraftRepository aircraftRepository;
    private AirportApi airportService;
    private int aircraftMaintainceTime;

    // For fast access it holds a map of airport code as a key
    // tracking as a value
    Map<String, List<AircraftTracking>> trackings;

    public AircraftTrackingService(AircraftRepository aircraftRepository,
                                   AirportApi airportService,
                                   @Value("${airflux.aircraft.maintaince-time}") int maintainceTime) {
        this.aircraftRepository = aircraftRepository;
        this.airportService = airportService;
        this.trackings = new HashMap<>();
        this.aircraftMaintainceTime = maintainceTime;
    }

    /*
        Initialize the map
        if arrival time is empty, this means it is the first flight and aircraft is ready to take off
    */
    public void init() {
        aircraftRepository
                .findAll()
                .forEach(aircraft -> {
                    // Normally each city may have multiple airports
                    // but for the task I will only assume it is only one airport
                    Airport airport = airportService
                            .airports(aircraft.getHome().getCode())
                            .stream()
                            .findFirst()
                            .<ApplicationException>orElseThrow(() -> {
                                String message = String.format("Airport not found for city code %s",
                                        aircraft.getHome().getCode());

                                getLogger().error(message);
                                throw new ApplicationException(message);
                            });

                    trackings.put(airport.getCode(),
                            new ArrayList<>(Arrays.asList(new AircraftTracking(aircraft, empty()))));
        });
    }

    /*
        Returns a list of aircrafts that are ready to take off
     */
    public Optional<Aircraft> availableAircraft(String airportCode, LocalDateTime departure) {
       return trackings
                .getOrDefault(airportCode, new ArrayList<>())
                .stream()
                .filter(tracking -> isReadyToTakeOff(tracking.getArrivalTime(), departure))
                .map(AircraftTracking::getAircraft)
                .findFirst();
    }

    /*
        Moves the aircraft from origin airport to destination airport
     */
    public void moveAircraft(String origin, String destination, Aircraft aircraft, LocalDateTime arrivalTime) {
        removeTracking(origin, aircraft);
        addTracking(destination, aircraft, arrivalTime);
    }

    /*
        Return a list of aircraft to Position their hub
     */
    public List<Aircraft> aircraftToPosition(String airportCode, int remaingFlightsCount) {

      List<Aircraft> aircrafts = trackings
              .getOrDefault(airportCode, new ArrayList<>())
              .stream()
              .sorted((t1, t2) -> t2.getArrivalTime().get().compareTo(t1.getArrivalTime().get()))
              .map(AircraftTracking::getAircraft)
              .collect(toList());

        if(aircrafts.size() > remaingFlightsCount) {
            return aircrafts.subList(0, remaingFlightsCount);
        }

        return new ArrayList<>();
    }

    private boolean isReadyToTakeOff(Optional<LocalDateTime> arrivalTime, LocalDateTime departureTime) {
        return !arrivalTime.isPresent() ||
                arrivalTime
                        .map(time -> time.compareTo(departureTime.plusMinutes(aircraftMaintainceTime)) < 0)
                        .orElse(false);
    }

    private void removeTracking(String origin, Aircraft aircraft) {

        if(trackings.containsKey(origin)) {
            List<AircraftTracking> originTrackings = trackings.getOrDefault(origin, new ArrayList<>());
            originTrackings
                    .stream()
                    .filter(tracking -> tracking.getAircraft().equals(aircraft))
                    .findFirst()
                    .ifPresent(originTrackings::remove);
        } else{
            String message = String.format("No trackings found for airport code %s" , origin);
            getLogger().warn(message);
        }
    }

    private void addTracking(String destination, Aircraft aircraft, LocalDateTime arrivalTime) {

        if(trackings.containsKey(destination)) {
            List<AircraftTracking> destinationTrackings = trackings.getOrDefault(destination, new ArrayList<>());
            destinationTrackings.add(new AircraftTracking(aircraft, Optional.of(arrivalTime)));

            trackings.put(destination, destinationTrackings);
        } else{
            String message = String.format("No trackings found for airport code %s" , destination);
            getLogger().warn(message);
        }
    }

}
