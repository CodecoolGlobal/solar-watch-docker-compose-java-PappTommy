package com.codecool.solar_watch.service;

import com.codecool.solar_watch.model.sunrise_sunset.SunriseSunsetDTO;
import com.codecool.solar_watch.model.sunrise_sunset.SunriseSunsetAPIResults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SunriseSunsetAPIServiceTest {
    public static final String SUNRISE_SUNSET_API_URL = "https://api.sunrise-sunset.org/json";
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private SunriseSunsetAPIService sunriseSunsetAPIService;

    @Test
    void testGetSunriseSunset_ValidCoordinates_SunriseSunset() {
        // Arrange
        double lat = 36.7201600;
        double lng = 4.4203400;
        LocalDate date = LocalDate.of(2024, 4, 29);
        SunriseSunsetDTO expectedSunriseSunset = new SunriseSunsetDTO("4:48:40 AM", "6:30:33 PM");

        SunriseSunsetAPIResults expectedSunriseSunsetAPIResults = new SunriseSunsetAPIResults(expectedSunriseSunset);
        System.out.println(expectedSunriseSunsetAPIResults.results());
        Mockito.when(restTemplate.getForObject(SUNRISE_SUNSET_API_URL + "?lat=" + lat + "&lng=" + lng + "&date=" + date, SunriseSunsetAPIResults.class))
                .thenReturn(expectedSunriseSunsetAPIResults);

        // Act
        SunriseSunsetDTO actualSunriseSunset = sunriseSunsetAPIService.fetchSunriseSunsetDTO(lat, lng, date);

        // Assert
        assertEquals(expectedSunriseSunset, actualSunriseSunset);
    }
}