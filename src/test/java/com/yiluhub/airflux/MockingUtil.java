package com.yiluhub.airflux;

import com.yiluhub.airflux.model.AircraftTracking;
import com.yiluhub.airflux.model.Flight;
import com.yiluhub.airflux.persistence.entity.Aircraft;
import com.yiluhub.airflux.persistence.entity.Airport;
import com.yiluhub.airflux.persistence.entity.City;
import com.yiluhub.airflux.persistence.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class MockingUtil {

    public List<Airport> mockedAirportList() {
        Airport airport1 = mockAirport("TXL", new City("BE", "Berlin"));
        Airport airport2 = mockAirport("LHR", new City("LDN", "London"));

        return Arrays.asList(airport1, airport2);
    }

    // Map of city code as key, list of airports as value
    public Map<String, List<Airport>> mockedAirportsMap() {
        return mockedAirportList()
                .stream()
                .collect(groupingBy(airport -> airport.getCity().getCode()));
    }

    public List<Aircraft> mockedAircraftList() {
        Aircraft aircraft1 = mock(Aircraft.class);
        Aircraft aircraft2 = mock(Aircraft.class);

        when(aircraft1.getSerial()).thenReturn("0001");
        when(aircraft2.getSerial()).thenReturn("0002");

        when(aircraft1.getHome()).thenReturn(new City("BE", "Berlin"));
        when(aircraft2.getHome()).thenReturn(new City("LDN", "London"));

        return Arrays.asList(aircraft1, aircraft2);
    }

    public Map<String, List<AircraftTracking>> mockedAircraftTrackings() {

        return mockedAircraftList()
                .stream()
                .map(aircraft -> {
                    LocalDateTime arrivalTime =
                            LocalDateTime.of(2018, 4, 13, 8, 0, 0);
                    return new AircraftTracking(aircraft, Optional.of(arrivalTime));
                })
                .map(tracking -> new Tuple<>(tracking.getAircraft().getSerial(), tracking))
                .collect(toMap(
                        tuple -> tuple.key == "0001"? "TXL": "LHR",
                        tuple -> new ArrayList<>(Arrays.asList(tuple.value)))
                );
    }

    public List<Schedule> mockedSchedules() {

        Airport airport1 = mockAirport("TXL", new City("BE", "Berlin"));
        Airport airport2 = mockAirport("LHR", new City("LDN", "London"));
        Airport airport3 = mockAirport("MUC", new City("MUC", "Munich"));

        LocalDateTime departure1 = LocalDateTime.of(2018, 4, 13, 8, 0, 0);
        LocalDateTime departure2 = LocalDateTime.of(2018, 4, 13, 12, 0, 0);
        LocalDateTime departure3 = LocalDateTime.of(2018, 4, 13, 15, 0, 0);
        LocalDateTime departure4 = LocalDateTime.of(2018, 4, 13, 20, 0, 0);

        Duration duration1 = Duration.ofHours(1);
        Duration duration2 = Duration.ofHours(1).plusMinutes(30);
        Duration duration3 = Duration.ofMinutes(30);
        Duration duration4 = Duration.ofHours(2);

        Schedule schedule1 = mockSchedule(departure1, airport1, airport2, duration1);
        Schedule schedule2 = mockSchedule(departure2, airport2, airport1, duration2);
        Schedule schedule3 = mockSchedule(departure3, airport2, airport3, duration3);
        Schedule schedule4 = mockSchedule(departure4, airport3, airport2, duration4);

        return new ArrayList<>(Arrays.asList(schedule1, schedule2, schedule3, schedule4));
    }

    // Map of airport code as key, list of schedules as value
    public Map<String, List<Schedule>> mockedScheduleMap() {
        return mockedSchedules()
                .stream()
                .collect(groupingBy(schedule -> schedule.getOrigin().getCode()));
    }

    public List<Flight> mockedFlights() {

        LocalDateTime departure1 = LocalDateTime.of(2018, 4, 13, 8, 0, 0);
        LocalDateTime departure2 = LocalDateTime.of(2018, 4, 13, 12, 0, 0);
        LocalDateTime departure3 = LocalDateTime.of(2018, 4, 13, 15, 0, 0);
        LocalDateTime departure4 = LocalDateTime.of(2018, 4, 13, 20, 0, 0);


        Flight flight1 = mockFlight("TXL", "MUC", departure1, departure1.plusHours(1), "737");
        Flight flight2 = mockFlight("TXL", "HAM", departure2, departure2.plusHours(2), "A321");
        Flight flight3 = mockFlight("MUC", "LHR", departure3, departure3.plusHours(1), "747-400");
        Flight flight4 = mockFlight("LHR", "TXL", departure4, departure4.plusHours(1), "A320");

        return Arrays.asList(flight1, flight2, flight3, flight4);
    }


    // To test endpoint, spring unable to serialize mocked objects
    // I have to create a fake objects without mocking
    public List<Flight> fakeFlights() {

        LocalDateTime departure1 = LocalDateTime.of(2018, 4, 13, 8, 0, 0);
        LocalDateTime departure2 = LocalDateTime.of(2018, 4, 13, 12, 0, 0);
        LocalDateTime departure3 = LocalDateTime.of(2018, 4, 13, 15, 0, 0);
        LocalDateTime departure4 = LocalDateTime.of(2018, 4, 13, 20, 0, 0);


        Flight flight1 = fakeFlight("TXL", "MUC", departure1, departure1.plusHours(1), "737");
        Flight flight2 = fakeFlight("TXL", "HAM", departure2, departure2.plusHours(2), "A321");
        Flight flight3 = fakeFlight("MUC", "LHR", departure3, departure3.plusHours(1), "747-400");
        Flight flight4 = fakeFlight("LHR", "TXL", departure4, departure4.plusHours(1), "A320");

        return Arrays.asList(flight1, flight2, flight3, flight4);
    }

    private Flight fakeFlight(String origin, String destination, LocalDateTime departure,
                              LocalDateTime arrival, String equipment) {

        Flight flight = new Flight(origin, destination, departure, arrival, equipment);
        return flight;
    }

    private Flight mockFlight(String origin, String destination, LocalDateTime departure,
                              LocalDateTime arrival, String equipment) {

        Flight flight = mock(Flight.class);

        when(flight.getOrigin()).thenReturn(origin);
        when(flight.getDestination()).thenReturn(destination);
        when(flight.getDeparture()).thenReturn(departure);
        when(flight.getArrival()).thenReturn(arrival);
        when(flight.getEquipment()).thenReturn(equipment);

        return flight;
    }

    private Airport mockAirport(String code, City city) {
        Airport airport = mock(Airport.class);
        when(airport.getCode()).thenReturn(code);
        when(airport.getCity()).thenReturn(city);

        return airport;
    }

    private Schedule mockSchedule(LocalDateTime departure, Airport origin,
                                  Airport destination, Duration flightTime) {

        Schedule schedule = mock(Schedule.class);
        when(schedule.getDeparture()).thenReturn(departure);
        when(schedule.getOrigin()).thenReturn(origin);
        when(schedule.getDestination()).thenReturn(destination);
        when(schedule.getFlightTime()).thenReturn(flightTime);

        return schedule;
    }
}


@Data
@AllArgsConstructor
class Tuple<K, V> {

    K key;
    V value;
}