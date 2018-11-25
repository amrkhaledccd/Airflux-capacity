package com.yiluhub.airflux.endpoint;


import com.yiluhub.airflux.logging.AirLogger;
import com.yiluhub.airflux.model.Flight;
import com.yiluhub.airflux.model.OperatingInstruction;
import com.yiluhub.airflux.service.OperationManagementApi;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class FlightOperationEndpoint implements AirLogger {

    private OperationManagementApi operationManagementService;

    public FlightOperationEndpoint(OperationManagementApi operationManagementService) {
        this.operationManagementService = operationManagementService;
    }


    @GetMapping(value = "/flightplan", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> flightPlans(
            @RequestParam(value = "airport", required = false) Optional<String> airport) {

        getLogger().info(String.format("Recieved a request for airport %s", airport));

        List<Flight> flights = operationManagementService.findFlights(airport);

        if(flights.size() > 0) {
            return ResponseEntity.ok(flights);
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @GetMapping(value = "/operationsplan/{registration}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> operationsPlan(@PathVariable("registration") String registration) {

        getLogger().info(String.format("Recieved a request for registration %s", registration));

        List<OperatingInstruction> operatingInstructions =
               operationManagementService.findOperattingInstructions(registration);

        if(operatingInstructions.size() > 0) {
            return ResponseEntity.ok(operatingInstructions);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
