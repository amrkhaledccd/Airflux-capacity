package com.yiluhub.airflux.service.impl;


import com.yiluhub.airflux.MockingUtil;
import com.yiluhub.airflux.persistence.entity.Airport;
import com.yiluhub.airflux.persistence.entity.Schedule;
import com.yiluhub.airflux.persistence.repository.AirportRepository;
import com.yiluhub.airflux.persistence.repository.ScheduleRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.FieldSetter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;



public class AirportServiceTest extends MockingUtil {

    AirportRepository airportRepository;
    ScheduleRepository scheduleRepository;

    AirportService airportService;

    @Before
    public void setup() {
        airportRepository = mock(AirportRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);

        airportService = new AirportService(airportRepository, scheduleRepository);
    }

    @Test
    public void whenInit_thenInitMaps() {
        List<Airport> airports = mockedAirportList();
        List<Schedule> schedules = mockedSchedules();

        when(airportRepository.findAll()).thenReturn(airports);
        when(scheduleRepository.findAll()).thenReturn(schedules);

        airportService.init();

        verify(airportRepository, times(1)).findAll();
        verify(scheduleRepository, times(1)).findAll();
    }

    @Test
    public void whenAirportsAndCityCodeIsExist_thenReturnListOfAirports() throws Exception {

        Map<String, List<Airport>> airportsMap = mockedAirportsMap();
        FieldSetter.setField(airportService,
                AirportService.class.getDeclaredField("airports"), airportsMap);

        List<Airport> result = airportService.airports("BE");

        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getCode(), "TXL");
    }

    @Test
    public void whenAirportsAndCityCodeIsNotExist_thenReturnEmptyList() throws Exception {

        Map<String, List<Airport>> airportsMap = mockedAirportsMap();
        FieldSetter.setField(airportService,
                AirportService.class.getDeclaredField("airports"), airportsMap);

        List<Airport> result = airportService.airports("FRA");

        assertNotNull(result);
        assertEquals(result.size(), 0);
    }

    @Test
    public void whenAllSchedules_thenReturnAllSchedules() throws Exception {
        Map<String, List<Schedule>> schedulesMap = mockedScheduleMap();
        FieldSetter.setField(airportService,
                AirportService.class.getDeclaredField("schedules"), schedulesMap);

        List<Schedule> result = airportService.allSchedules();

        assertNotNull(result);
        assertEquals(result.size(), 4);
    }

    @Test
    public void whencountRemainingSchedulesAndTime_thenReturnCount() throws Exception {
        Map<String, List<Schedule>> schedulesMap = mockedScheduleMap();
        FieldSetter.setField(airportService,
                AirportService.class.getDeclaredField("schedules"), schedulesMap);

        LocalDateTime time = LocalDateTime.of(2018, 4, 13, 7, 0, 0);
        int result = airportService.countRemainingSchedules("TXL", time);

        assertEquals(result, 1);
    }
}
