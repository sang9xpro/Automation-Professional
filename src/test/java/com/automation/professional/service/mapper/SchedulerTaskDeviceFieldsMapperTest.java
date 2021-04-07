package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedulerTaskDeviceFieldsMapperTest {

    private SchedulerTaskDeviceFieldsMapper schedulerTaskDeviceFieldsMapper;

    @BeforeEach
    public void setUp() {
        schedulerTaskDeviceFieldsMapper = new SchedulerTaskDeviceFieldsMapperImpl();
    }
}
