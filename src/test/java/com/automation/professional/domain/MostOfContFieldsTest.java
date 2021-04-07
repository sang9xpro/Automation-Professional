package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MostOfContFieldsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MostOfContFields.class);
        MostOfContFields mostOfContFields1 = new MostOfContFields();
        mostOfContFields1.setId(1L);
        MostOfContFields mostOfContFields2 = new MostOfContFields();
        mostOfContFields2.setId(mostOfContFields1.getId());
        assertThat(mostOfContFields1).isEqualTo(mostOfContFields2);
        mostOfContFields2.setId(2L);
        assertThat(mostOfContFields1).isNotEqualTo(mostOfContFields2);
        mostOfContFields1.setId(null);
        assertThat(mostOfContFields1).isNotEqualTo(mostOfContFields2);
    }
}
