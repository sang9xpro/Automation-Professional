package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MostOfContValuesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MostOfContValues.class);
        MostOfContValues mostOfContValues1 = new MostOfContValues();
        mostOfContValues1.setId(1L);
        MostOfContValues mostOfContValues2 = new MostOfContValues();
        mostOfContValues2.setId(mostOfContValues1.getId());
        assertThat(mostOfContValues1).isEqualTo(mostOfContValues2);
        mostOfContValues2.setId(2L);
        assertThat(mostOfContValues1).isNotEqualTo(mostOfContValues2);
        mostOfContValues1.setId(null);
        assertThat(mostOfContValues1).isNotEqualTo(mostOfContValues2);
    }
}
