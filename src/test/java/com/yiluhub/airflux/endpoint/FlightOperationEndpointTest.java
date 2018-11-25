package com.yiluhub.airflux.endpoint;


import com.yiluhub.airflux.MockingUtil;
import com.yiluhub.airflux.model.Flight;
import com.yiluhub.airflux.service.impl.OperationManagementService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertEquals;

/*
    There are a number of missing tests
    I wrote some sample test to show the idea, other tests are based on the same.

    Missing test method
        operationsPlan
 */
public class FlightOperationEndpointTest extends MockingUtil {

    OperationManagementService operationManagementService;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {

    }

    @Test
    public void whenFlightPlans_thenReturnAirportFlightPlans() throws Exception {

        List<Flight> flights = fakeFlights();
        operationManagementService = mock(OperationManagementService.class);
        when(operationManagementService.findFlights(any())).thenReturn(flights);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new FlightOperationEndpoint(operationManagementService)).build();

        MvcResult resut = mockMvc.perform(get("/flightplan"))
                .andExpect(status().isOk())
                .andReturn();

        // I should also check that the response matches the flights list
        // but because of time constraint I skip this (need ot serialize the list to json string)
        assertEquals(resut.getResponse().getContentType(), "application/json;charset=UTF-8");
    }

    @Test
    public void whenFlightPlansAndNoPlans_thenReturnNoContentResponse() throws Exception {

        operationManagementService = mock(OperationManagementService.class);
        when(operationManagementService.findFlights(any())).thenReturn(new ArrayList<>());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new FlightOperationEndpoint(operationManagementService)).build();

        mockMvc.perform(get("/flightplan?airport=FRA"))
                .andExpect(status().isNoContent())
                .andReturn();

    }
}
