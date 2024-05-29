package com.codecool.solar_watch.model.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class City {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private double longitude;
    private double latitude;
    private String state;
    private String country;
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private Set<SunriseSunset> sunriseSunsets;

    public Set<SunriseSunset> getSunriseSunsets() {
        return sunriseSunsets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
