package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MostOfContentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MostOfContent.class);
        MostOfContent mostOfContent1 = new MostOfContent();
        mostOfContent1.setId(1L);
        MostOfContent mostOfContent2 = new MostOfContent();
        mostOfContent2.setId(mostOfContent1.getId());
        assertThat(mostOfContent1).isEqualTo(mostOfContent2);
        mostOfContent2.setId(2L);
        assertThat(mostOfContent1).isNotEqualTo(mostOfContent2);
        mostOfContent1.setId(null);
        assertThat(mostOfContent1).isNotEqualTo(mostOfContent2);
    }
}
