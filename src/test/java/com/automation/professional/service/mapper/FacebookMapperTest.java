package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacebookMapperTest {

    private FacebookMapper facebookMapper;

    @BeforeEach
    public void setUp() {
        facebookMapper = new FacebookMapperImpl();
    }
}
