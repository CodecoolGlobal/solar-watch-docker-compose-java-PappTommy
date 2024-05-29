package com.codecool.solar_watch.service;

import com.codecool.solar_watch.repository.SunriseSunsetRepository;
import com.codecool.solar_watch.model.sunrise_sunset.SunriseSunsetDTO;
import com.codecool.solar_watch.model.sunrise_sunset.SunriseSunsetAPIResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
public class SunriseSunsetAPIService {
    public static final String SUNRISE_SUNSET_API_URL = "https://api.sunrise-sunset.org/json";
    private static final Logger logger = LoggerFactory.getLogger(SunriseSunsetAPIService.class);
    private final RestTemplate restTemplate;
    private final SunriseSunsetRepository sunriseSunsetRepository;


    public SunriseSunsetAPIService(RestTemplate restTemplate, SunriseSunsetRepository sunriseSunsetRepository) {
        this.restTemplate = restTemplate;
        this.sunriseSunsetRepository = sunriseSunsetRepository;
    }


    public SunriseSunsetDTO fetchSunriseSunsetDTO(double lat, double lng, LocalDate date) {

        String url = String.format(SUNRISE_SUNSET_API_URL + "?lat=%s&lng=%s&date=%s", lat, lng, date);

        SunriseSunsetAPIResults response = restTemplate.getForObject(url, SunriseSunsetAPIResults.class);

        logger.info("Successful fetchSunriseSunsetDTO method!");

        return new SunriseSunsetDTO(response.results().sunrise(), response.results().sunset());
    }
}
