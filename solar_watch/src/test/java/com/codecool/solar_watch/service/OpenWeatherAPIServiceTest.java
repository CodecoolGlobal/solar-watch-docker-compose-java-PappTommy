package com.codecool.solar_watch.service;

import com.codecool.solar_watch.model.entity.City;
import com.codecool.solar_watch.model.location_of_city.LocationOfCity;
import com.codecool.solar_watch.repository.CityRepository;
import com.codecool.solar_watch.service.exceptions.InvalidCityNameException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenWeatherAPIServiceTest {

    private static final String API_KEY = "35fb764a4645cb378ad9da11b13e017f";
    public static final String OPEN_WEATHER_API_URL = "http://api.openweathermap.org/geo/1.0/direct";
    @Mock
    CityRepository cityRepository;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    private OpenWeatherAPIService openWeatherAPIService;


    @Test
    void testGetCoordinatesOfCity_ValidName_ReturnsLocationList() {
        // Arrange
        String cserkeszolo = "cserkeszőlő";

        City expectedCityEntity = createCityEntity(4L, cserkeszolo, 46.8675023, 20.195096, null, "HU");

        when(cityRepository.findByNameIgnoreCase(cserkeszolo)).thenReturn(Optional.of(expectedCityEntity));
        // Act
        City actualCityEntity = openWeatherAPIService.getCoordinatesOfFirstCity(cserkeszolo);
        //Assert
        assertEquals(expectedCityEntity, actualCityEntity);
    }

    @Test
    void testGetCoordinatesOfCity_InvalidName_ThrowsInvalidCityNameException() {
        // Arrange
        String cityName = "";
        // Assert
        assertThrows(InvalidCityNameException.class, () -> {
            // Act
            openWeatherAPIService.getCoordinatesOfFirstCity(cityName);
        });
    }

    @Test
    void testGetCoordinatesOfCity_WithEmptyRepository(){
        //Arrange
        City expectedCityEntity = createCityEntity(4L, "Cserkeszőlő", 46.8675023, 20.195096, null, "HU");
        when(cityRepository.findByNameIgnoreCase("Cserkeszőlő")).thenReturn(Optional.empty()).thenReturn(Optional.of(expectedCityEntity));
        LocationOfCity locationOfCity = new LocationOfCity("Cserkeszőlő", 46.8675023, 20.195096, null, "HU");
        Object[] objects = {locationOfCity};
        ResponseEntity<Object[]> responseEntity = new ResponseEntity<>(objects, HttpStatusCode.valueOf(200));
        String url = String.format(OPEN_WEATHER_API_URL + "?q=%s&limit=5&appid=%s", "Cserkeszőlő", API_KEY);
        when(restTemplate.getForEntity(url, Object[].class)).thenReturn(responseEntity);

        when(objectMapper.convertValue(any(Object.class), any(Class.class))).thenReturn(locationOfCity);

        //when(cityRepository.findByNameIgnoreCase("Cserkeszőlő")).thenReturn(Optional.of(expectedCityEntity));
        //Act
        City actualCityEntity = openWeatherAPIService.getCoordinatesOfFirstCity("Cserkeszőlő");
        //Assert
        assertEquals(expectedCityEntity, actualCityEntity);
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
}