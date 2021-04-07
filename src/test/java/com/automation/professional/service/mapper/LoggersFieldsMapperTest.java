package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoggersFieldsMapperTest {

    private LoggersFieldsMapper loggersFieldsMapper;

    @BeforeEach
    public void setUp() {
        loggersFieldsMapper = new LoggersFieldsMapperImpl();
    }
}
