package com.yiluhub.airflux.model;

import com.yiluhub.airflux.persistence.entity.Aircraft;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.Optional;

/*
  This is to track the aircraft
  which airport and the arrival time
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AircraftTracking {

    private Aircraft aircraft;
    private Optional<LocalDateTime> arrivalTime;
}
