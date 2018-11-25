package com.yiluhub.airflux.service;

import com.yiluhub.airflux.persistence.entity.Airport;
import com.yiluhub.airflux.persistence.entity.Schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface AirportApi {

    //  Initialize schedules, airports and flightDuration map
    void init();

    // Returns a list of airports for specific city
    List<Airport> airports(String cityCode);

    // Returns all schedules sorted by departure time
    List<Schedule> allSchedules();

    // Returns the number of remaining schedules for specific airport after a given time
    int countRemainingSchedules(String airportCode, LocalDateTime time);

    // Hardcoded the flight durations between airports for simplicity
    Duration flightDuration(String origin, String destination);
}
