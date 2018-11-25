package com.yiluhub.airflux.service.impl;

import com.yiluhub.airflux.persistence.entity.Airport;
import com.yiluhub.airflux.persistence.entity.Schedule;
import com.yiluhub.airflux.persistence.repository.AirportRepository;
import com.yiluhub.airflux.persistence.repository.ScheduleRepository;
import com.yiluhub.airflux.service.AirportApi;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;
import com.yiluhub.airflux.model.AirportFlightDuration;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.Comparator.comparing;


/*
    Responsible for providing airport schedules per day
 */
@Service
public class AirportService implements AirportApi {

    private AirportRepository airportRepository;
    private ScheduleRepository scheduleRepository;

    // Holds a map of airport code as key
    // list of Schedules as a valeu
    private Map<String, List<Schedule>> schedules;

    // Map of city code as a key
    // list of airports as value
    private Map<String, List<Airport>> airports;

    // Dummy hardcoded list of flight durations between airports
    // Used it to save time
    private List<AirportFlightDuration> flightDurations;


    public AirportService(AirportRepository airportRepository,
                          ScheduleRepository scheduleRepository) {
        this.airportRepository = airportRepository;
        this.scheduleRepository = scheduleRepository;
    }

    //  Initialize schedules, airports and flightDuration map
    public void init() {

        initFlightDuration();

        airports = StreamSupport
                .stream(airportRepository.findAll().spliterator(), false)
                .collect(groupingBy(airport -> airport.getCity().getCode()));

        schedules = StreamSupport
                .stream(scheduleRepository.findAll().spliterator(), false)
                .collect(groupingBy(schedule -> schedule.getOrigin().getCode()));
    }

    // Returns a list of airports for specific city
    public List<Airport> airports(String cityCode) {
        return airports.getOrDefault(cityCode, new ArrayList<>());
    }

    /*
        Returns all schedules sorted by departure time
     */
    public List<Schedule> allSchedules() {
        return schedules
                .values()
                .stream()
                .flatMap(schedules -> schedules.stream())
                .sorted(comparing(Schedule::getDeparture))
                .collect(toList());
    }

    // Returns the number of remaining schedules for specific airport after a given time
    public int countRemainingSchedules(String airportCode, LocalDateTime time) {
       return schedules
                .getOrDefault(airportCode, new ArrayList<>())
                .stream()
                .filter(schedule -> schedule.getDeparture().compareTo(time) > 0)
                .collect(toList())
                .size();
    }

    /*
        This is a dummy method just to save time.
        I hardcoded the flight durations between airports for simplicity
        this could be loaded from database.

        Also to save I assume there is a direct flight between every two airports
        if not I could've used a graph data structure to model airports as nodes
        and flight duration as edge weight, and calculate the shortest path.
     */
    private void initFlightDuration() {
        flightDurations = new ArrayList<>();

        flightDurations.add(new AirportFlightDuration("TXL", "MUC", Duration.ofHours(1)));
        flightDurations.add(new AirportFlightDuration("TXL", "HAM", Duration.ofMinutes(40)));
        flightDurations.add(new AirportFlightDuration("TXL", "LHR", Duration.ofMinutes(2)));
        flightDurations.add(new AirportFlightDuration("MUC", "LHR", Duration.ofMinutes(2)));
        flightDurations.add(new AirportFlightDuration("MUC", "HAM", Duration.ofMinutes(1)));
        flightDurations.add(new AirportFlightDuration("HAM", "LHR",
                Duration.ofMinutes(2).plusMinutes(30)));

    }

    public Duration flightDuration(String origin, String destination) {
       return flightDurations
                .stream()
                .filter(airportDuration ->
                        (airportDuration.getOriginCode().equals(origin) &&
                                airportDuration.getDestinationCode().equals(destination)) ||
                                (airportDuration.getOriginCode().equals(destination) &&
                                        airportDuration.getDestinationCode().equals(origin)))
                .map(AirportFlightDuration::getFlightDuration)
                .findFirst()
                .get();
    }
}