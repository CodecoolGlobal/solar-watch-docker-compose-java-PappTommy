package com.codecool.solar_watch.model.payload;

import java.util.List;

public record JwtResponse(String jwt, String username, List<String> roles) {
}
