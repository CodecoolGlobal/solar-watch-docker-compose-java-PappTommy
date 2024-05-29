package com.codecool.solar_watch.service;

import com.codecool.solar_watch.model.entity.SolarUser;
import com.codecool.solar_watch.repository.SolarUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class SolarUserService {

    private final SolarUserRepository solarUserRepository;

    public SolarUserService(SolarUserRepository solarUserRepository) {
        this.solarUserRepository = solarUserRepository;
    }

    public void createSolarUser(SolarUser user) {
        // TODO: nameCheck in DB - String userName = user.getSolarUserName();
        solarUserRepository.save(user);
    }

    public SolarUser findCurrentSolarUser() {
        UserDetails contextUser = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String username = contextUser.getUsername();
        return solarUserRepository.findBySolarUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(format("could not find user %s in the repository", username)));
        //  TODO UsernameNotFoundException?
    }


}
