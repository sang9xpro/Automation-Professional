package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacebookValuesMapperTest {

    private FacebookValuesMapper facebookValuesMapper;

    @BeforeEach
    public void setUp() {
        facebookValuesMapper = new FacebookValuesMapperImpl();
    }
}
