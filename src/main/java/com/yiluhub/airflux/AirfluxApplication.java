package com.yiluhub.airflux;

import com.yiluhub.airflux.exception.ApplicationException;
import com.yiluhub.airflux.logging.AirLogger;
import com.yiluhub.airflux.service.impl.AircraftTrackingService;
import com.yiluhub.airflux.service.impl.AirportService;
import com.yiluhub.airflux.service.impl.OperationManagementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class AirfluxApplication implements AirLogger {


	public static void main(String[] args) {

		SpringApplication.run(AirfluxApplication.class, args);
    }

    /*
        I used commandLineRunner to initialize system components
        but this could be a cron job that plan flights for the next day

        I also assumed that it is only used for 13th of April 2018
        but this can be easily change and pass the date as parameter.
     */
    @Bean
    public CommandLineRunner commandLineRunner(AirportService airportService,
                                               AircraftTrackingService aircraftTrackingService,
                                               OperationManagementService operationManagementService) {
	    return args -> {
	        try {
                airportService.init();
                aircraftTrackingService.init();
                operationManagementService.plan();

                getLogger().info("Airflux application initialized successfully");

            } catch (ApplicationException e) {
	            getLogger().error("Error initializing Airflux application", e);
            }
	    };
    }
}
