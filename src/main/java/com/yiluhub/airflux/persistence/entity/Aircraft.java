package com.yiluhub.airflux.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class Aircraft {

    @Id
    private String serial;

    private String manufacturer;

    private String model;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_code")
    private City home;

    private String registration;
}
