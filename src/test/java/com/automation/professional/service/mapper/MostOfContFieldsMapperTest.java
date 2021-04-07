package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MostOfContFieldsMapperTest {

    private MostOfContFieldsMapper mostOfContFieldsMapper;

    @BeforeEach
    public void setUp() {
        mostOfContFieldsMapper = new MostOfContFieldsMapperImpl();
    }
}
