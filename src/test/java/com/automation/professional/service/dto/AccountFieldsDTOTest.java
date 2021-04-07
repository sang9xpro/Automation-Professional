package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountFieldsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountFieldsDTO.class);
        AccountFieldsDTO accountFieldsDTO1 = new AccountFieldsDTO();
        accountFieldsDTO1.setId(1L);
        AccountFieldsDTO accountFieldsDTO2 = new AccountFieldsDTO();
        assertThat(accountFieldsDTO1).isNotEqualTo(accountFieldsDTO2);
        accountFieldsDTO2.setId(accountFieldsDTO1.getId());
        assertThat(accountFieldsDTO1).isEqualTo(accountFieldsDTO2);
        accountFieldsDTO2.setId(2L);
        assertThat(accountFieldsDTO1).isNotEqualTo(accountFieldsDTO2);
        accountFieldsDTO1.setId(null);
        assertThat(accountFieldsDTO1).isNotEqualTo(accountFieldsDTO2);
    }
}
