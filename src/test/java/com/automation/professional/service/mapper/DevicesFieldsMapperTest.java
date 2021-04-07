package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DevicesFieldsMapperTest {

    private DevicesFieldsMapper devicesFieldsMapper;

    @BeforeEach
    public void setUp() {
        devicesFieldsMapper = new DevicesFieldsMapperImpl();
    }
}
