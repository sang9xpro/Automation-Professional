package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountsMapperTest {

    private AccountsMapper accountsMapper;

    @BeforeEach
    public void setUp() {
        accountsMapper = new AccountsMapperImpl();
    }
}
