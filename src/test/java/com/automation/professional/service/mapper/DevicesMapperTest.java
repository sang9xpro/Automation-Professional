package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DevicesMapperTest {

    private DevicesMapper devicesMapper;

    @BeforeEach
    public void setUp() {
        devicesMapper = new DevicesMapperImpl();
    }
}
