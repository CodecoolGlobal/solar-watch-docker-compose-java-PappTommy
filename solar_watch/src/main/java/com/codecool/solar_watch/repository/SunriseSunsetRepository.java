package com.codecool.solar_watch.repository;

import com.codecool.solar_watch.model.entity.City;
import com.codecool.solar_watch.model.entity.SunriseSunset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SunriseSunsetRepository extends JpaRepository<SunriseSunset, Long> {
    Optional<SunriseSunset> findByCityAndDate(City city, LocalDate date);
}
