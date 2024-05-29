package com.codecool.solar_watch.service;

import com.codecool.solar_watch.model.entity.City;
import com.codecool.solar_watch.model.location_of_city.LocationOfCity;
import com.codecool.solar_watch.repository.CityRepository;
import com.codecool.solar_watch.service.exceptions.InvalidCityNameException;
import com.codecool.solar_watch.service.exceptions.NotSupportedCityException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
public class OpenWeatherAPIService {
    private static final String API_KEY = "35fb764a4645cb378ad9da11b13e017f";
    public static final String OPEN_WEATHER_API_URL = "http://api.openweathermap.org/geo/1.0/direct";
    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherAPIService.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final CityRepository cityRepository;


    public OpenWeatherAPIService(RestTemplate restTemplate, ObjectMapper mapper, CityRepository cityRepository) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.cityRepository = cityRepository;
    }


    public City getCoordinatesOfFirstCity(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidCityNameException();
        }
        if (cityRepository.findByNameIgnoreCase(name).isEmpty()) {
            logger.info(name + " is not in the database.");
            LocationOfCity locationOfCity = fetchFirstCity(name);

            if (Objects.equals(locationOfCity.name(), name)) {
                City city = createCity(locationOfCity.name(), locationOfCity.lat(), locationOfCity.lon(), locationOfCity.state(), locationOfCity.country());
                cityRepository.save(city);
                logger.info(city.getName() + " is successfully saved in database.");
            } else {
                throw new NotSupportedCityException(name);
            }
        }
        return getCityByName(name);
    }

    private LocationOfCity fetchFirstCity(String name) {
        String url = String.format(OPEN_WEATHER_API_URL + "?q=%s&limit=5&appid=%s", name, API_KEY);

        ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(url, Object[].class);
        Object[] objects = responseEntity.getBody();

        if (objects == null || objects.length == 0) {
            throw new InvalidCityNameException();
        }
        logger.info("Successful fetchFirstCity method!");

        return Arrays.stream(objects)
                .map(object -> mapper.convertValue(object, LocationOfCity.class))
                .toList().get(0);
    }

    private City getCityByName(String name) {
        return cityRepository.findByNameIgnoreCase(name).orElseThrow(() -> new NotSupportedCityException(name));
    }

    private City createCity(String name, double latitude, double longitude, String state, String country) {
        City city = new City();
        city.setName(name);
        city.setLatitude(latitude);
        city.setLongitude(longitude);
        city.setState(state);
        city.setCountry(country);

        return city;
    }

    public City createCityToCityDB(LocationOfCity locationOfCity) {
        City city = createCity(locationOfCity.name(), locationOfCity.lat(), locationOfCity.lon(), locationOfCity.state(), locationOfCity.country());
        cityRepository.save(city);
        return city;
    }

    public Optional<City> findByName(String name) {
        return cityRepository.findByNameIgnoreCase(name);
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}
