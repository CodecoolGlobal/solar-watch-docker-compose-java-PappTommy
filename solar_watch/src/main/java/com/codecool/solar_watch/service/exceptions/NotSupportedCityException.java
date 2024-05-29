package com.codecool.solar_watch.service.exceptions;

public class NotSupportedCityException extends RuntimeException{
    public NotSupportedCityException(String cityName) {
        super(String.format("City '%s' is not supported!", cityName));
    }
}
