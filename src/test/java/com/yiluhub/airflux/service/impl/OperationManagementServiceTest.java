package com.yiluhub.airflux.service.impl;

import com.yiluhub.airflux.MockingUtil;
import com.yiluhub.airflux.model.Flight;
import com.yiluhub.airflux.persistence.entity.Aircraft;
import com.yiluhub.airflux.persistence.entity.Schedule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.FieldSetter;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;



/*
    There are a number of missing tests
    I wrote some sample test to show the idea, other tests are based on the same.

    Missing test method
        findOperattingInstructions
 */
public class OperationManagementServiceTest extends MockingUtil {

    AirportService airportService;
    AircraftTrackingService aircraftTrackingService;

    OperationManagementService operationManagementService;

    @Before
    public void setup() {
        airportService = mock(AirportService.class);
        aircraftTrackingService = mock(AircraftTrackingService.class);

        operationManagementService = new OperationManagementService(aircraftTrackingService, airportService);
    }

    @Test
    public void whenPlan_thenPlanFlights() {
        List<Schedule> allSchedules = mockedSchedules();
        Optional<Aircraft> aircraft = Optional.of(mockedAircraftList().get(0));

        when(airportService.allSchedules()).thenReturn(allSchedules);
        when(aircraftTrackingService.availableAircraft(any(), any())).thenReturn(aircraft);

        operationManagementService.plan();

        verify(airportService, times(1)).allSchedules();
        verify(aircraftTrackingService, times(4)).availableAircraft(any(), any());
    }

    @Test
    public void whenFindFlightsAndAirportCodeNotExist_thenReturnAllFlights() throws Exception{
       List<Flight> flights = mockedFlights();
        FieldSetter.setField(operationManagementService,
                OperationManagementService.class.getDeclaredField("flights"), flights);

        List<Flight> result = operationManagementService.findFlights(Optional.empty());

        assertNotNull(result);
        assertEquals(result.size(), 4);
    }

    @Test
    public void whenFindFlightsAndAirportCodeExist_thenReturnAirportFlights() throws Exception{
        List<Flight> flights = mockedFlights();
        FieldSetter.setField(operationManagementService,
                OperationManagementService.class.getDeclaredField("flights"), flights);

        List<Flight> result = operationManagementService.findFlights(Optional.of("TXL"));

        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertTrue(result.stream().allMatch(flight -> flight.getOrigin().equals("TXL")));
    }


}
