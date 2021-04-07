package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacebookFieldsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacebookFields.class);
        FacebookFields facebookFields1 = new FacebookFields();
        facebookFields1.setId(1L);
        FacebookFields facebookFields2 = new FacebookFields();
        facebookFields2.setId(facebookFields1.getId());
        assertThat(facebookFields1).isEqualTo(facebookFields2);
        facebookFields2.setId(2L);
        assertThat(facebookFields1).isNotEqualTo(facebookFields2);
        facebookFields1.setId(null);
        assertThat(facebookFields1).isNotEqualTo(facebookFields2);
    }
}
