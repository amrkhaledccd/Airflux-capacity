package com.yiluhub.airflux.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime departure;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_code")
    private Airport origin;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_code")
    private Airport destination;

    @Column(name = "flight_time")
    private Duration flightTime;
}
