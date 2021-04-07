package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountFieldsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountFields.class);
        AccountFields accountFields1 = new AccountFields();
        accountFields1.setId(1L);
        AccountFields accountFields2 = new AccountFields();
        accountFields2.setId(accountFields1.getId());
        assertThat(accountFields1).isEqualTo(accountFields2);
        accountFields2.setId(2L);
        assertThat(accountFields1).isNotEqualTo(accountFields2);
        accountFields1.setId(null);
        assertThat(accountFields1).isNotEqualTo(accountFields2);
    }
}
