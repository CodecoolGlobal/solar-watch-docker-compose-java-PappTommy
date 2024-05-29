package com.codecool.solar_watch.model.sunrise_sunset;

import com.codecool.solar_watch.model.location_of_city.LocationOfCity;

import java.time.LocalDate;

public record NewSunriseSunsetDTO(String sunrise, String sunset, LocalDate date, LocationOfCity city) {
}
