package com.codecool.solar_watch.service;

import com.codecool.solar_watch.model.entity.City;
import com.codecool.solar_watch.model.entity.SunriseSunset;
import com.codecool.solar_watch.repository.SunriseSunsetRepository;
import com.codecool.solar_watch.model.sunrise_sunset.SunriseSunsetDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolarWatchServiceTest {
    @Mock
    private OpenWeatherAPIService openWeatherAPIService;
    @Mock
    private SunriseSunsetRepository sunriseSunsetRepository;
    @InjectMocks
    private SolarWatchService solarWatchService;

    @Test
    public void testGetSunriseSunset_ValidCity() {
        //Arrange
        String london = "London";
        LocalDate dateToUse = LocalDate.of(2024, 4, 25);

        City cityMock = createCityEntity(1L, london, 51.5073219, -0.1276474, "England", "GB");
        when(openWeatherAPIService.getCoordinatesOfFirstCity(london)).thenReturn(cityMock);

        SunriseSunsetDTO expectedSunriseSunsetDTO = new SunriseSunsetDTO("4:40:45 AM", "7:16:01 PM");

        SunriseSunset expectedSunriseSunset = createSunriseSunsetEntity(102L, "4:40:45 AM", "7:16:01 PM", dateToUse, cityMock);
        when(sunriseSunsetRepository.findByCityAndDate(cityMock, dateToUse)).thenReturn(Optional.of(expectedSunriseSunset));

        //Act
        SunriseSunsetDTO actualSunriseSunsetDTO = solarWatchService.getSunriseSunset(london, dateToUse);

        //Assert
        assertEquals(expectedSunriseSunsetDTO, actualSunriseSunsetDTO);
    }

    private City createCityEntity(Long id, String name, double latitude, double longitude, String state, String country) {
        City city = new City();
        city.setId(id);
        city.setName(name);
        city.setLatitude(latitude);
        city.setLongitude(longitude);
        city.setState(state);
        city.setCountry(country);

        return city;
    }

    private SunriseSunset createSunriseSunsetEntity(Long id, String sunrise, String sunset, LocalDate date, City city) {
        SunriseSunset sunriseSunset = new SunriseSunset();
        sunriseSunset.setId(id);
        sunriseSunset.setSunrise(sunrise);
        sunriseSunset.setSunset(sunset);
        sunriseSunset.setDate(date);
        sunriseSunset.setCity(city);

        return sunriseSunset;
    }

}