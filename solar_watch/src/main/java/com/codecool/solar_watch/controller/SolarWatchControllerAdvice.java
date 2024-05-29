package com.codecool.solar_watch.controller;

import com.codecool.solar_watch.service.exceptions.InvalidCityNameException;
import com.codecool.solar_watch.service.exceptions.NotSupportedCityException;
import com.codecool.solar_watch.service.exceptions.NotSupportedSunsetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class SolarWatchControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(SolarWatchControllerAdvice.class);

    @ResponseBody
    @ExceptionHandler(InvalidCityNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String InvalidOrMissingCityNameExceptionHandler(InvalidCityNameException ex) {
        logger.error("Invalid city parameter");
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String MethodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        logger.error("Invalid path parameter");
        return "Error: " + ex.getMessage() + " Invalid path parameter!";
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        logger.error("Missing path parameter");
        return "Error:" + ex.getMessage() + " Missing path parameter!";
    }

    @ResponseBody
    @ExceptionHandler(NotSupportedSunsetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String NotSupportedSunsetExceptionHandler(NotSupportedSunsetException ex) {
        logger.error("Not supported Sunrise and Sunset data");
        return "Error:" + ex.getMessage() + " Not supported Sunrise and Sunset data";
    }

    @ResponseBody
    @ExceptionHandler(NotSupportedCityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String NotSupportedCityExceptionHandler(NotSupportedCityException ex) {
        logger.error("Not supported City data");
        return "Error:" + ex.getMessage() + " Not supported City data";
    }


}
