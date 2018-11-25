package com.yiluhub.airflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class AirportFlightDuration {
    String originCode;
    String destinationCode;
    Duration flightDuration;
}