package com.yiluhub.airflux.service.impl;

import com.yiluhub.airflux.MockingUtil;
import com.yiluhub.airflux.exception.ApplicationException;
import com.yiluhub.airflux.model.AircraftTracking;
import com.yiluhub.airflux.persistence.entity.Aircraft;
import com.yiluhub.airflux.persistence.entity.Airport;
import com.yiluhub.airflux.persistence.repository.AircraftRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.FieldSetter;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

/*
    There are a number of missing tests
    I wrote some sample test to show the idea, other tests are based on the same
    Missing tests for methods:
        moveAircraft
        aircraftToPosition
 */
public class AirportTrackingServiceTest extends MockingUtil {

    AircraftRepository aircraftRepository;
    AirportService airportService;
    AircraftTrackingService aircraftTrackingService;

    @Before
    public void setup() {
        aircraftRepository = mock(AircraftRepository.class);
        airportService = mock(AirportService.class);
        aircraftTrackingService = new AircraftTrackingService(aircraftRepository, airportService, 60);
    }

    @Test
    public void whenInitAndAirportExist_thenInitTracking() {
        List<Aircraft> aircrafts = mockedAircraftList();
        List<Airport> airports = mockedAirportList();
        when(aircraftRepository.findAll()).thenReturn(aircrafts);
        when(airportService.airports(any())).thenReturn(airports);

        aircraftTrackingService.init();

        verify(aircraftRepository, times(1)).findAll();
        verify(airportService, times(2)).airports(anyString());
    }

    @Test(expected = ApplicationException.class)
    public void whenInitAndAirportNotExist_thenThrowApplicationException() {
        List<Aircraft> aircrafts = mockedAircraftList();
        when(aircraftRepository.findAll()).thenReturn(aircrafts);
        when(airportService.airports(any())).thenReturn(new ArrayList<>());

        aircraftTrackingService.init();
    }

    @Test
    public void whenAaviableAircraftExist_thenRetunAircraft() throws Exception {

        Map<String, List<AircraftTracking>> trackings = mockedAircraftTrackings();
        FieldSetter.setField(aircraftTrackingService,
                AircraftTrackingService.class.getDeclaredField("trackings"), trackings);

        Optional<Aircraft> result = aircraftTrackingService.availableAircraft("TXL",
                LocalDateTime.of(2018, 4, 13, 10, 0, 0));

        assertTrue(result.isPresent());
        assertEquals(result.get().getSerial(), "0001");
    }

    @Test
    public void whenAaviableAircraftNotExist_thenRetunEmptyOptional() throws Exception {

        Map<String, List<AircraftTracking>> trackings = new HashMap<>();
        FieldSetter.setField(aircraftTrackingService,
                AircraftTrackingService.class.getDeclaredField("trackings"), trackings);

        Optional<Aircraft> result = aircraftTrackingService.availableAircraft("TXL",
                LocalDateTime.of(2018, 4, 13, 10, 0, 0));

        assertFalse(result.isPresent());
    }


}
