package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoggersValuesMapperTest {

    private LoggersValuesMapper loggersValuesMapper;

    @BeforeEach
    public void setUp() {
        loggersValuesMapper = new LoggersValuesMapperImpl();
    }
}
