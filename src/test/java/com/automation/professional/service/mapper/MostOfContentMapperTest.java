package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MostOfContentMapperTest {

    private MostOfContentMapper mostOfContentMapper;

    @BeforeEach
    public void setUp() {
        mostOfContentMapper = new MostOfContentMapperImpl();
    }
}
