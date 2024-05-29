package com.codecool.solar_watch;

import com.codecool.solar_watch.controller.SolarAdminController;
import com.codecool.solar_watch.controller.SolarWatchController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class SolarWatchApplicationTests {

    @Autowired
    private SolarWatchController solarWatchController;

    @Autowired
    private SolarAdminController solarAdminController;

    @Test
    void contextLoads() {
        assertThat(solarWatchController).isNotNull();
        assertThat(solarAdminController).isNotNull();
    }

}
