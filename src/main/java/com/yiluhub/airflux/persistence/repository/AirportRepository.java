package com.yiluhub.airflux.persistence.repository;

import com.yiluhub.airflux.persistence.entity.Airport;
import org.springframework.data.repository.CrudRepository;

public interface AirportRepository extends CrudRepository<Airport, String> {
}
