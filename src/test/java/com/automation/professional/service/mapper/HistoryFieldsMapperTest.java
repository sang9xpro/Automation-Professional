package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoryFieldsMapperTest {

    private HistoryFieldsMapper historyFieldsMapper;

    @BeforeEach
    public void setUp() {
        historyFieldsMapper = new HistoryFieldsMapperImpl();
    }
}
