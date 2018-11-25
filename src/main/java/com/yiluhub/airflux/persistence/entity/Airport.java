package com.yiluhub.airflux.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Airport {

    @Id
    private String code;
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_code")
    private City city;
}
