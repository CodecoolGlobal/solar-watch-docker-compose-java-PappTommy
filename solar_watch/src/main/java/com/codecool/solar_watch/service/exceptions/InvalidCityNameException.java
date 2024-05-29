package com.codecool.solar_watch.service.exceptions;

public class InvalidCityNameException extends RuntimeException {
    public InvalidCityNameException() {
        super(":O\nPlease write the proper name of the place!");
    }
}
