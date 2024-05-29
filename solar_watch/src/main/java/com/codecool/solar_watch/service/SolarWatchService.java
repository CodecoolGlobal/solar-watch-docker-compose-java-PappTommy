package com.codecool.solar_watch.service;

import com.codecool.solar_watch.model.entity.City;
import com.codecool.solar_watch.model.entity.SunriseSunset;
import com.codecool.solar_watch.model.location_of_city.LocationOfCity;
import com.codecool.solar_watch.model.sunrise_sunset.NewSunriseSunsetDTO;
import com.codecool.solar_watch.model.sunrise_sunset.SunriseSunsetDTO;
import com.codecool.solar_watch.repository.SunriseSunsetRepository;
import com.codecool.solar_watch.service.exceptions.NotSupportedSunsetException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SolarWatchService {
    private static final Logger logger = LoggerFactory.getLogger(SunriseSunsetAPIService.class);
    private final OpenWeatherAPIService openWeatherAPIService;
    private final SunriseSunsetAPIService sunriseSunsetAPIService;
    private final SunriseSunsetRepository sunriseSunsetRepository;


    public SolarWatchService(OpenWeatherAPIService openWeatherAPIService, SunriseSunsetAPIService sunriseSunsetAPIService, SunriseSunsetRepository sunriseSunsetRepository) {
        this.openWeatherAPIService = openWeatherAPIService;
        this.sunriseSunsetAPIService = sunriseSunsetAPIService;
        this.sunriseSunsetRepository = sunriseSunsetRepository;
    }


    public SunriseSunsetDTO getSunriseSunset(String name, LocalDate date) {
        City cityEntity = openWeatherAPIService.getCoordinatesOfFirstCity(name);

        if (sunriseSunsetRepository.findByCityAndDate(cityEntity, date).isEmpty()) {
            logger.info(date + " for this " + name + " is not in the database.");
            SunriseSunsetDTO sunriseSunsetDTO = sunriseSunsetAPIService.fetchSunriseSunsetDTO(cityEntity.getLatitude(), cityEntity.getLongitude(), date);
            SunriseSunset sunriseSunset = createSunriseSunset(sunriseSunsetDTO.sunrise(), sunriseSunsetDTO.sunset(), date, cityEntity);
            sunriseSunsetRepository.save(sunriseSunset);
            logger.info(date + " for " + name + " is successfully saved in database.");
        }
        return createSunriseSunsetDTOFromEntity(cityEntity, date);
    }

    private SunriseSunset createSunriseSunset(String sunrise, String sunset, LocalDate date, City city) {
        SunriseSunset sunriseSunset = new SunriseSunset();
        sunriseSunset.setSunrise(sunrise);
        sunriseSunset.setSunset(sunset);
        sunriseSunset.setDate(date);
        sunriseSunset.setCity(city);

        return sunriseSunset;
    }

    public void createSunriseSunsetToSunriseSunsetDB(NewSunriseSunsetDTO sunriseSunset) {
        LocationOfCity locationOfCity = sunriseSunset.city();
        LocalDate date = sunriseSunset.date();
        String cityName = locationOfCity.name();

        // Optional<City> cityEntity = openWeatherAPIService.findByName(cityName);
        //        if (cityEntity.isEmpty()) {
        //            openWeatherAPIService.createCityToCityDB(locationOfCity);
        //        }
        //        City newCityEntity = openWeatherAPIService.findByName(locationOfCity.name()).get();
        // refactored version:

        Optional<City> cityEntityOpt = openWeatherAPIService.findByName(cityName);
        City cityEntity = cityEntityOpt.orElseGet(() -> openWeatherAPIService.createCityToCityDB(locationOfCity));

        if (sunriseSunsetRepository.findByCityAndDate(cityEntity, date).isEmpty()) {
            logger.info(date + " for this " + cityName + " is not in the database.");
            SunriseSunset newSunriseSunset = createSunriseSunset(sunriseSunset.sunrise(), sunriseSunset.sunset(), date, cityEntity);
            sunriseSunsetRepository.save(newSunriseSunset);
            logger.info(date + " for " + cityName + " is successfully saved in database.");
        }
    }

    private SunriseSunsetDTO createSunriseSunsetDTOFromEntity(City city, LocalDate date) {
        SunriseSunset sunriseSunset = getSunriseSunsetFromDatabase(city, date);
        logger.info(date + " for " + city.getName() + " is successfully get from database.");
        return new SunriseSunsetDTO(sunriseSunset.getSunrise(), sunriseSunset.getSunset());
    }

    private SunriseSunset getSunriseSunsetFromDatabase(City city, LocalDate date) {
        return sunriseSunsetRepository.findByCityAndDate(city, date).orElseThrow(() -> new NotSupportedSunsetException(city, date));
    }

    @Transactional
    public void updateSunrise(Long id, String sunrise) {
        SunriseSunset sunriseSunset = sunriseSunsetRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is no entity with " + id + " id.");
                    return new EntityNotFoundException("SunriseSunset entity not found with id: " + id);
                });
        sunriseSunset.setSunrise(sunrise);
    }

    @Transactional
    public void updateSunset(Long id, String sunset) {
        SunriseSunset sunriseSunset = sunriseSunsetRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is no entity with " + id + " id.");
                    return new EntityNotFoundException("SunriseSunset entity not found with id: " + id);
                });
        sunriseSunset.setSunset(sunset);
    }

    public void deleteSunriseSunset(Long id){
        sunriseSunsetRepository.deleteById(id);
    }
}
