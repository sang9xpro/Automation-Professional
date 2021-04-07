package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoggersMapperTest {

    private LoggersMapper loggersMapper;

    @BeforeEach
    public void setUp() {
        loggersMapper = new LoggersMapperImpl();
    }
}
