package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MostOfContValuesMapperTest {

    private MostOfContValuesMapper mostOfContValuesMapper;

    @BeforeEach
    public void setUp() {
        mostOfContValuesMapper = new MostOfContValuesMapperImpl();
    }
}
