package com.codecool.solar_watch.security.service;

import com.codecool.solar_watch.model.entity.Role;
import com.codecool.solar_watch.model.entity.SolarUser;
import com.codecool.solar_watch.repository.SolarUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SolarUserRepository solarUserRepository;

    @Autowired
    public UserDetailsServiceImpl(SolarUserRepository solarUserRepository) {
        this.solarUserRepository = solarUserRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SolarUser solarUser = solarUserRepository.findBySolarUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        for (Role role : solarUser.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role.name()));
        }

        return new User(solarUser.getSolarUsername(), solarUser.getPassword(), roles);
    }
}
