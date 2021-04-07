package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MostOfContFieldsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MostOfContFieldsDTO.class);
        MostOfContFieldsDTO mostOfContFieldsDTO1 = new MostOfContFieldsDTO();
        mostOfContFieldsDTO1.setId(1L);
        MostOfContFieldsDTO mostOfContFieldsDTO2 = new MostOfContFieldsDTO();
        assertThat(mostOfContFieldsDTO1).isNotEqualTo(mostOfContFieldsDTO2);
        mostOfContFieldsDTO2.setId(mostOfContFieldsDTO1.getId());
        assertThat(mostOfContFieldsDTO1).isEqualTo(mostOfContFieldsDTO2);
        mostOfContFieldsDTO2.setId(2L);
        assertThat(mostOfContFieldsDTO1).isNotEqualTo(mostOfContFieldsDTO2);
        mostOfContFieldsDTO1.setId(null);
        assertThat(mostOfContFieldsDTO1).isNotEqualTo(mostOfContFieldsDTO2);
    }
}
