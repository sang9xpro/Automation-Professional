package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedulerTaskDeviceMapperTest {

    private SchedulerTaskDeviceMapper schedulerTaskDeviceMapper;

    @BeforeEach
    public void setUp() {
        schedulerTaskDeviceMapper = new SchedulerTaskDeviceMapperImpl();
    }
}
