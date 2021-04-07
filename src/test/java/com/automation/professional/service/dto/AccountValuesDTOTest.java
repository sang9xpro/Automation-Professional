package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountValuesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountValuesDTO.class);
        AccountValuesDTO accountValuesDTO1 = new AccountValuesDTO();
        accountValuesDTO1.setId(1L);
        AccountValuesDTO accountValuesDTO2 = new AccountValuesDTO();
        assertThat(accountValuesDTO1).isNotEqualTo(accountValuesDTO2);
        accountValuesDTO2.setId(accountValuesDTO1.getId());
        assertThat(accountValuesDTO1).isEqualTo(accountValuesDTO2);
        accountValuesDTO2.setId(2L);
        assertThat(accountValuesDTO1).isNotEqualTo(accountValuesDTO2);
        accountValuesDTO1.setId(null);
        assertThat(accountValuesDTO1).isNotEqualTo(accountValuesDTO2);
    }
}
