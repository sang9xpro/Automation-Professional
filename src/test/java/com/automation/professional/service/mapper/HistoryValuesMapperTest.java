package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoryValuesMapperTest {

    private HistoryValuesMapper historyValuesMapper;

    @BeforeEach
    public void setUp() {
        historyValuesMapper = new HistoryValuesMapperImpl();
    }
}
