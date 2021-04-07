package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulerValueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulerValueDTO.class);
        SchedulerValueDTO schedulerValueDTO1 = new SchedulerValueDTO();
        schedulerValueDTO1.setId(1L);
        SchedulerValueDTO schedulerValueDTO2 = new SchedulerValueDTO();
        assertThat(schedulerValueDTO1).isNotEqualTo(schedulerValueDTO2);
        schedulerValueDTO2.setId(schedulerValueDTO1.getId());
        assertThat(schedulerValueDTO1).isEqualTo(schedulerValueDTO2);
        schedulerValueDTO2.setId(2L);
        assertThat(schedulerValueDTO1).isNotEqualTo(schedulerValueDTO2);
        schedulerValueDTO1.setId(null);
        assertThat(schedulerValueDTO1).isNotEqualTo(schedulerValueDTO2);
    }
}
