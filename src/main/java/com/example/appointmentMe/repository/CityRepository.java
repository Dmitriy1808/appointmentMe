package com.example.appointmentMe.repository;

import com.example.appointmentMe.model.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {

    Optional<Integer> findTimezoneByCityIgnoreCase(String city);

}
