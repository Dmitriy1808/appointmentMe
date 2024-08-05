package ru.appointme.service;

import ru.appointme.model.City;
import ru.appointme.model.User;
import ru.appointme.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CityService {

    private static final Integer DEFAULT_TIMEZONE_OFFSET = 3;

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public Integer getCityTimezoneOffset(String city) {
        return cityRepository.findTimezoneByCityIgnoreCase(city).orElse(DEFAULT_TIMEZONE_OFFSET);
    }

    public int getDefaultTimezoneOffset() {
        return DEFAULT_TIMEZONE_OFFSET;
    }

    public String getCityByTimezoneOffsetAndDirtyCityName(int timezoneOffset, String dirtyCityName) {
        Optional<City> cityOptional = cityRepository.findByTimezoneOffsetAndCityIgnoreCase(timezoneOffset, dirtyCityName);
        return cityOptional.isPresent()
                ? cityOptional.get().getCity()
                : User.DEFAULT_CITY;
    }

}
