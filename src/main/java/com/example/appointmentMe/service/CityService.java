package com.example.appointmentMe.service;

import com.example.appointmentMe.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CityService {

    private static final int DEFAULT_TIMEZONE_OFFSET = 3;

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public int getCityTimezone(String cityName) {
        return cityRepository.findTimezoneByCityIgnoreCase(cityName).orElse(DEFAULT_TIMEZONE_OFFSET);
    }

    public int getDefaultTimezone() {
        return DEFAULT_TIMEZONE_OFFSET;
    }

}
