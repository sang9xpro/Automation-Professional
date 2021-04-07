package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MostOfContentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MostOfContentDTO.class);
        MostOfContentDTO mostOfContentDTO1 = new MostOfContentDTO();
        mostOfContentDTO1.setId(1L);
        MostOfContentDTO mostOfContentDTO2 = new MostOfContentDTO();
        assertThat(mostOfContentDTO1).isNotEqualTo(mostOfContentDTO2);
        mostOfContentDTO2.setId(mostOfContentDTO1.getId());
        assertThat(mostOfContentDTO1).isEqualTo(mostOfContentDTO2);
        mostOfContentDTO2.setId(2L);
        assertThat(mostOfContentDTO1).isNotEqualTo(mostOfContentDTO2);
        mostOfContentDTO1.setId(null);
        assertThat(mostOfContentDTO1).isNotEqualTo(mostOfContentDTO2);
    }
}
