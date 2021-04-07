package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulerFieldsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulerFieldsDTO.class);
        SchedulerFieldsDTO schedulerFieldsDTO1 = new SchedulerFieldsDTO();
        schedulerFieldsDTO1.setId(1L);
        SchedulerFieldsDTO schedulerFieldsDTO2 = new SchedulerFieldsDTO();
        assertThat(schedulerFieldsDTO1).isNotEqualTo(schedulerFieldsDTO2);
        schedulerFieldsDTO2.setId(schedulerFieldsDTO1.getId());
        assertThat(schedulerFieldsDTO1).isEqualTo(schedulerFieldsDTO2);
        schedulerFieldsDTO2.setId(2L);
        assertThat(schedulerFieldsDTO1).isNotEqualTo(schedulerFieldsDTO2);
        schedulerFieldsDTO1.setId(null);
        assertThat(schedulerFieldsDTO1).isNotEqualTo(schedulerFieldsDTO2);
    }
}
