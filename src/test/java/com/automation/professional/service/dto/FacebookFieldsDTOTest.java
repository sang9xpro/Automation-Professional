package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacebookFieldsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacebookFieldsDTO.class);
        FacebookFieldsDTO facebookFieldsDTO1 = new FacebookFieldsDTO();
        facebookFieldsDTO1.setId(1L);
        FacebookFieldsDTO facebookFieldsDTO2 = new FacebookFieldsDTO();
        assertThat(facebookFieldsDTO1).isNotEqualTo(facebookFieldsDTO2);
        facebookFieldsDTO2.setId(facebookFieldsDTO1.getId());
        assertThat(facebookFieldsDTO1).isEqualTo(facebookFieldsDTO2);
        facebookFieldsDTO2.setId(2L);
        assertThat(facebookFieldsDTO1).isNotEqualTo(facebookFieldsDTO2);
        facebookFieldsDTO1.setId(null);
        assertThat(facebookFieldsDTO1).isNotEqualTo(facebookFieldsDTO2);
    }
}
