package com.codecool.solar_watch;

import com.codecool.solar_watch.model.entity.SolarUser;
import com.codecool.solar_watch.model.payload.UserLogInRequest;
import com.codecool.solar_watch.model.payload.UserRegisterRequest;
import com.codecool.solar_watch.model.sunrise_sunset.SunriseSunsetDTO;
import com.codecool.solar_watch.repository.SolarUserRepository;
import com.codecool.solar_watch.service.SolarWatchService;
import com.codecool.solar_watch.service.exceptions.InvalidCityNameException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class SolarWatchControllerMockMvcIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SolarWatchService solarWatchService;

    @Autowired
    private SolarUserRepository solarUserRepository;


    @Test
    void registerUserShouldReturnCreatedStatus() throws Exception {
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setSolarUsername("test");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("Test");
        registerRequest.setLastName("User");
        registerRequest.setEmail("testuser@example.com");

        mockMvc.perform(post("/solar-user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        Optional<SolarUser> savedUserOptional = solarUserRepository.findBySolarUsername("test");
        assertTrue(savedUserOptional.isPresent());
    }

    @Test
    void authenticateUserShouldReturnOkStatus() throws Exception {
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setSolarUsername("test1");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("Test1");
        registerRequest.setLastName("User1");
        registerRequest.setEmail("testuser1@example.com");

        mockMvc.perform(post("/solar-user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        Optional<SolarUser> savedUserOptional = solarUserRepository.findBySolarUsername("test1");
        assertTrue(savedUserOptional.isPresent());

        UserLogInRequest loginRequest = new UserLogInRequest();
        loginRequest.setSolarUsername("test1");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/solar-user/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateUserWithWrongCredentialsShouldReturnUnauthorizedStatus() throws Exception {
        UserLogInRequest loginRequest = new UserLogInRequest();
        loginRequest.setSolarUsername("test2");
        loginRequest.setPassword("wrong-password");

        mockMvc.perform(post("/solar-user/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

        Optional<SolarUser> savedUserOptional = solarUserRepository.findBySolarUsername("test2");
        assertTrue(savedUserOptional.isEmpty());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetSunriseSunset() throws Exception {
        when(solarWatchService.getSunriseSunset("London", LocalDate.of(2020, 5, 30)))
                .thenReturn(new SunriseSunsetDTO("3:47:54 AM", "8:08:21 PM"));

        mockMvc.perform(MockMvcRequestBuilders.get("/solar-user/solar-watch")
                        .param("city", "London")
                        .param("date", LocalDate.of(2020, 5, 30).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sunrise").value("3:47:54 AM"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sunset").value("8:08:21 PM"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetSunriseSunset_CityNotFound() throws Exception {
        when(solarWatchService.getSunriseSunset("UnknownCity", LocalDate.now()))
                .thenThrow(new InvalidCityNameException());

        mockMvc.perform(MockMvcRequestBuilders.get("/solar-user/solar-watch")
                        .param("city", "UnknownCity")
                        .param("date", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}


