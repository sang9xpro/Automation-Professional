package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulersDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulersDTO.class);
        SchedulersDTO schedulersDTO1 = new SchedulersDTO();
        schedulersDTO1.setId(1L);
        SchedulersDTO schedulersDTO2 = new SchedulersDTO();
        assertThat(schedulersDTO1).isNotEqualTo(schedulersDTO2);
        schedulersDTO2.setId(schedulersDTO1.getId());
        assertThat(schedulersDTO1).isEqualTo(schedulersDTO2);
        schedulersDTO2.setId(2L);
        assertThat(schedulersDTO1).isNotEqualTo(schedulersDTO2);
        schedulersDTO1.setId(null);
        assertThat(schedulersDTO1).isNotEqualTo(schedulersDTO2);
    }
}
