package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountValuesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountValues.class);
        AccountValues accountValues1 = new AccountValues();
        accountValues1.setId(1L);
        AccountValues accountValues2 = new AccountValues();
        accountValues2.setId(accountValues1.getId());
        assertThat(accountValues1).isEqualTo(accountValues2);
        accountValues2.setId(2L);
        assertThat(accountValues1).isNotEqualTo(accountValues2);
        accountValues1.setId(null);
        assertThat(accountValues1).isNotEqualTo(accountValues2);
    }
}
