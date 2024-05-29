package com.codecool.solar_watch.model.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
public class SolarUser {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true, length = 20)
    private String solarUsername;
    @Column(nullable = false, length = 64)
    private String password;

    @Column(nullable = false, length = 20)
    private String firstName;

   @Column(nullable = false, length = 20)
    private String lastName;

   @Column(nullable = false, unique = true, length = 45)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    public SolarUser(String solarUsername, String password, Set<Role> roles,String firstName,String lastName,String email) {
        this.solarUsername = solarUsername;
        this.password = password;
        this.roles = roles;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
    }

    public SolarUser(){}


    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSolarUsername() {
        return solarUsername;
    }

    public void setSolarUsername(String solarUserName) {
        this.solarUsername = solarUserName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
