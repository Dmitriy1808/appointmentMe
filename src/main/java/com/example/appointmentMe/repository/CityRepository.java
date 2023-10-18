package com.example.appointmentMe.repository;

import com.example.appointmentMe.model.City;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {
    @Query(value = "select c.timezoneOffset from City c where lower(c.city) like lower(:city)")
    Optional<Integer> findTimezoneByCityIgnoreCase(@Param("city") String city);

    Optional<City> findByTimezoneOffsetAndCityIgnoreCase(Integer timezoneOffset, String city);
}
