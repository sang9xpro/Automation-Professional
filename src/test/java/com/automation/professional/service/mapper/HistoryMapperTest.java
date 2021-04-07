package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoryMapperTest {

    private HistoryMapper historyMapper;

    @BeforeEach
    public void setUp() {
        historyMapper = new HistoryMapperImpl();
    }
}
