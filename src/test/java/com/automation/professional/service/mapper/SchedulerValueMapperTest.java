package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedulerValueMapperTest {

    private SchedulerValueMapper schedulerValueMapper;

    @BeforeEach
    public void setUp() {
        schedulerValueMapper = new SchedulerValueMapperImpl();
    }
}
