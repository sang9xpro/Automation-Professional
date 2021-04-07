package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacebookDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacebookDTO.class);
        FacebookDTO facebookDTO1 = new FacebookDTO();
        facebookDTO1.setId(1L);
        FacebookDTO facebookDTO2 = new FacebookDTO();
        assertThat(facebookDTO1).isNotEqualTo(facebookDTO2);
        facebookDTO2.setId(facebookDTO1.getId());
        assertThat(facebookDTO1).isEqualTo(facebookDTO2);
        facebookDTO2.setId(2L);
        assertThat(facebookDTO1).isNotEqualTo(facebookDTO2);
        facebookDTO1.setId(null);
        assertThat(facebookDTO1).isNotEqualTo(facebookDTO2);
    }
}
