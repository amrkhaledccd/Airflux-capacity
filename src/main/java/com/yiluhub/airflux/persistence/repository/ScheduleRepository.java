package com.yiluhub.airflux.persistence.repository;

import com.yiluhub.airflux.persistence.entity.Schedule;
import org.springframework.data.repository.CrudRepository;


public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
}
