package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeviceValuesMapperTest {

    private DeviceValuesMapper deviceValuesMapper;

    @BeforeEach
    public void setUp() {
        deviceValuesMapper = new DeviceValuesMapperImpl();
    }
}
