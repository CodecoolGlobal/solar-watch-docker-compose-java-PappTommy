package com.codecool.solar_watch.controller;

import com.codecool.solar_watch.model.entity.Role;
import com.codecool.solar_watch.model.entity.SolarUser;
import com.codecool.solar_watch.model.location_of_city.LocationOfCity;
import com.codecool.solar_watch.model.payload.AdminRegisterRequest;
import com.codecool.solar_watch.model.payload.JwtResponse;
import com.codecool.solar_watch.model.payload.UserLogInRequest;
import com.codecool.solar_watch.model.sunrise_sunset.NewSunriseSunsetDTO;
import com.codecool.solar_watch.model.sunrise_sunset.SunriseToUpdateDTO;
import com.codecool.solar_watch.security.jwt.JwtUtils;
import com.codecool.solar_watch.service.OpenWeatherAPIService;
import com.codecool.solar_watch.service.SolarUserService;
import com.codecool.solar_watch.service.SolarWatchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/solar-admin")
public class SolarAdminController {

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;
    private final OpenWeatherAPIService openWeatherAPIService;
    private final SolarWatchService solarWatchService;
    private final SolarUserService solarUserService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public SolarAdminController(OpenWeatherAPIService openWeatherAPIService, SolarWatchService solarWatchService, SolarUserService solarUserService, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.openWeatherAPIService = openWeatherAPIService;
        this.solarWatchService = solarWatchService;
        this.solarUserService = solarUserService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createAdmin(@RequestBody AdminRegisterRequest signUpRequest) {
        if (!signUpRequest.getAdminPassword().equals(adminPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        SolarUser user = new SolarUser(signUpRequest.getSolarUsername(), encoder.encode(signUpRequest.getPassword()), Set.of(Role.ROLE_USER, Role.ROLE_ADMIN), signUpRequest.getFirstName(),
                signUpRequest.getLastName(), signUpRequest.getEmail());
        solarUserService.createSolarUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody UserLogInRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getSolarUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getUsername(), roles));
    }


    @PostMapping("/solar-city")
    public String createCity(@RequestBody LocationOfCity city) {
        openWeatherAPIService.createCityToCityDB(city);
        return city.name() + " is successfully added to the city database.";
    }

    @DeleteMapping("/solar-city/{id}")
    public void deleteCity(@PathVariable Long id) {
        openWeatherAPIService.deleteCity(id);
    }

    @PostMapping("/sunrise-sunset")
    public void createSunriseSunset(@RequestBody NewSunriseSunsetDTO sunriseSunset) {
        solarWatchService.createSunriseSunsetToSunriseSunsetDB(sunriseSunset);
    }

    @DeleteMapping("/sunrise-sunset/{id}")
    public void deleteSunriseSunset(@PathVariable Long id) {
        solarWatchService.deleteSunriseSunset(id);
    }

    @PatchMapping("/sunrise")
    public void updateSunrise(@RequestBody SunriseToUpdateDTO sunriseToUpdateDTO) {
        solarWatchService.updateSunrise(sunriseToUpdateDTO.id(), sunriseToUpdateDTO.sunriseOrSunset());
    }

    @PatchMapping("/sunset")
    public void updateSunset(@RequestBody SunriseToUpdateDTO sunriseToUpdateDTO) {
        solarWatchService.updateSunset(sunriseToUpdateDTO.id(), sunriseToUpdateDTO.sunriseOrSunset());
    }
}
