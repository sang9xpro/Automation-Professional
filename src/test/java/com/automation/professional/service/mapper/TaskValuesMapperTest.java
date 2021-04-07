package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskValuesMapperTest {

    private TaskValuesMapper taskValuesMapper;

    @BeforeEach
    public void setUp() {
        taskValuesMapper = new TaskValuesMapperImpl();
    }
}
