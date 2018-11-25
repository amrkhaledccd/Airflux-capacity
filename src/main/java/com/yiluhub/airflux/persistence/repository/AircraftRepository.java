package com.yiluhub.airflux.persistence.repository;

import com.yiluhub.airflux.persistence.entity.Aircraft;
import org.springframework.data.repository.CrudRepository;


public interface AircraftRepository extends CrudRepository<Aircraft, String> {
}
