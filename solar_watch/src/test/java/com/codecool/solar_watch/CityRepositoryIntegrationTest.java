package com.codecool.solar_watch;

import com.codecool.solar_watch.model.entity.City;
import com.codecool.solar_watch.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class CityRepositoryIntegrationTest {

    @Autowired
    private CityRepository cityRepository;

    @Test
    void testFindByNameIgnoreCase() {
        City city = new City();
        city.setName("London");
        city.setLatitude(51.5074);
        city.setLongitude(-0.1278);
        city.setState("England");
        city.setCountry("United Kingdom");

        cityRepository.save(city);

        Optional<City> foundCity = cityRepository.findByNameIgnoreCase("London");
        assertTrue(foundCity.isPresent());
        assertEquals("London", foundCity.get().getName());
        assertEquals(51.5074, foundCity.get().getLatitude());
        assertEquals(-0.1278, foundCity.get().getLongitude());
        assertEquals("England", foundCity.get().getState());
        assertEquals("United Kingdom", foundCity.get().getCountry());
    }
}
