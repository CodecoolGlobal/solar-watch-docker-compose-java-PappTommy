package com.codecool.solar_watch.model.sunrise_sunset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunriseSunsetAPIResults(SunriseSunsetDTO results) {
}
