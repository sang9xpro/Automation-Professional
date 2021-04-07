package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MostOfContValuesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MostOfContValuesDTO.class);
        MostOfContValuesDTO mostOfContValuesDTO1 = new MostOfContValuesDTO();
        mostOfContValuesDTO1.setId(1L);
        MostOfContValuesDTO mostOfContValuesDTO2 = new MostOfContValuesDTO();
        assertThat(mostOfContValuesDTO1).isNotEqualTo(mostOfContValuesDTO2);
        mostOfContValuesDTO2.setId(mostOfContValuesDTO1.getId());
        assertThat(mostOfContValuesDTO1).isEqualTo(mostOfContValuesDTO2);
        mostOfContValuesDTO2.setId(2L);
        assertThat(mostOfContValuesDTO1).isNotEqualTo(mostOfContValuesDTO2);
        mostOfContValuesDTO1.setId(null);
        assertThat(mostOfContValuesDTO1).isNotEqualTo(mostOfContValuesDTO2);
    }
}
