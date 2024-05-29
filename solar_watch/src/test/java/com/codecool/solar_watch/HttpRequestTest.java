package com.codecool.solar_watch;

import com.codecool.solar_watch.model.payload.UserRegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void registerUserShouldReturnCreatedStatus() {
        // create new user:
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setSolarUsername("test");
        userRegisterRequest.setPassword("password123");
        userRegisterRequest.setFirstName("Test");
        userRegisterRequest.setLastName("User");
        userRegisterRequest.setEmail("testuser@example.com");

        // create HttpEntity:
        HttpEntity<UserRegisterRequest> request = new HttpEntity<>(userRegisterRequest);

        // send POST request to /solar-user/register endpoint:
        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:" + port + "/solar-user/register", request, Void.class);

        // check the status of the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
