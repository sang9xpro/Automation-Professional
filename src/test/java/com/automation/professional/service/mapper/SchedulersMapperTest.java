package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedulersMapperTest {

    private SchedulersMapper schedulersMapper;

    @BeforeEach
    public void setUp() {
        schedulersMapper = new SchedulersMapperImpl();
    }
}
