package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskFieldsMapperTest {

    private TaskFieldsMapper taskFieldsMapper;

    @BeforeEach
    public void setUp() {
        taskFieldsMapper = new TaskFieldsMapperImpl();
    }
}
