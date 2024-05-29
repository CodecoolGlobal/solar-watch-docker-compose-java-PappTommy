package com.codecool.solar_watch.repository;

import com.codecool.solar_watch.model.entity.SolarUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolarUserRepository extends JpaRepository<SolarUser, Long> {

    Optional<SolarUser> findBySolarUsername(String name);
}
