package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacebookValuesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacebookValuesDTO.class);
        FacebookValuesDTO facebookValuesDTO1 = new FacebookValuesDTO();
        facebookValuesDTO1.setId(1L);
        FacebookValuesDTO facebookValuesDTO2 = new FacebookValuesDTO();
        assertThat(facebookValuesDTO1).isNotEqualTo(facebookValuesDTO2);
        facebookValuesDTO2.setId(facebookValuesDTO1.getId());
        assertThat(facebookValuesDTO1).isEqualTo(facebookValuesDTO2);
        facebookValuesDTO2.setId(2L);
        assertThat(facebookValuesDTO1).isNotEqualTo(facebookValuesDTO2);
        facebookValuesDTO1.setId(null);
        assertThat(facebookValuesDTO1).isNotEqualTo(facebookValuesDTO2);
    }
}
