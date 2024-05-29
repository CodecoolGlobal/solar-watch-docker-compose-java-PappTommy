package com.codecool.solar_watch.controller;

import com.codecool.solar_watch.model.entity.Role;
import com.codecool.solar_watch.model.entity.SolarUser;
import com.codecool.solar_watch.model.payload.JwtResponse;
import com.codecool.solar_watch.model.payload.UserLogInRequest;
import com.codecool.solar_watch.model.payload.UserRegisterRequest;
import com.codecool.solar_watch.model.sunrise_sunset.SunriseSunsetDTO;
import com.codecool.solar_watch.security.jwt.JwtUtils;
import com.codecool.solar_watch.service.SolarUserService;
import com.codecool.solar_watch.service.SolarWatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/solar-user")
public class SolarWatchController {
    private final SolarWatchService solarWatchService;
    private final SolarUserService solarUserService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    public SolarWatchController(SolarWatchService solarWatchService, SolarUserService solarUserService, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.solarWatchService = solarWatchService;
        this.solarUserService = solarUserService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody UserRegisterRequest signUpRequest) {
        SolarUser user = new SolarUser(signUpRequest.getSolarUsername(), encoder.encode(signUpRequest.getPassword()), Set.of(Role.ROLE_USER), signUpRequest.getFirstName(),
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

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public String me() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return "Hello " + user.getUsername();
    }

    @GetMapping("/solar-watch")
    public SunriseSunsetDTO getSolarWatch(@RequestParam String city, @RequestParam LocalDate date) {
        return solarWatchService.getSunriseSunset(city, date);
    }
}
