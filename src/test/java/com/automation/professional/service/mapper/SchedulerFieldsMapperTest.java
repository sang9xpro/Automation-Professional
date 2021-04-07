package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedulerFieldsMapperTest {

    private SchedulerFieldsMapper schedulerFieldsMapper;

    @BeforeEach
    public void setUp() {
        schedulerFieldsMapper = new SchedulerFieldsMapperImpl();
    }
}
