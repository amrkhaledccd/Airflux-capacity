package com.yiluhub.airflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OperatingInstruction {

    private String origin;
    private String destination;
    private LocalDateTime departure;
}
