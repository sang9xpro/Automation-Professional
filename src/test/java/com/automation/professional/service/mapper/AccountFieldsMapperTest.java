package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountFieldsMapperTest {

    private AccountFieldsMapper accountFieldsMapper;

    @BeforeEach
    public void setUp() {
        accountFieldsMapper = new AccountFieldsMapperImpl();
    }
}
