package com.codecool.solar_watch.model.payload;

import lombok.Data;

@Data
public class UserLogInRequest {
    private String solarUsername;
    private String password;
}
