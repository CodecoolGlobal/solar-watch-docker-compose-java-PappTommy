package com.codecool.solar_watch.model.payload;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String solarUsername;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
