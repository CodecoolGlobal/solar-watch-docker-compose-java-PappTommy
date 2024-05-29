package com.codecool.solar_watch.service.exceptions;

import com.codecool.solar_watch.model.entity.City;

import java.time.LocalDate;

public class NotSupportedSunsetException extends RuntimeException {

    public NotSupportedSunsetException(City city, LocalDate date) {
        super(String.format("City '%s' and date '%s' are not supported!", city, date));
    }
}