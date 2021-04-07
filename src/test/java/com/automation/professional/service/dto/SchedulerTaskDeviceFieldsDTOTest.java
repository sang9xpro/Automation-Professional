package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulerTaskDeviceFieldsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulerTaskDeviceFieldsDTO.class);
        SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO1 = new SchedulerTaskDeviceFieldsDTO();
        schedulerTaskDeviceFieldsDTO1.setId(1L);
        SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO2 = new SchedulerTaskDeviceFieldsDTO();
        assertThat(schedulerTaskDeviceFieldsDTO1).isNotEqualTo(schedulerTaskDeviceFieldsDTO2);
        schedulerTaskDeviceFieldsDTO2.setId(schedulerTaskDeviceFieldsDTO1.getId());
        assertThat(schedulerTaskDeviceFieldsDTO1).isEqualTo(schedulerTaskDeviceFieldsDTO2);
        schedulerTaskDeviceFieldsDTO2.setId(2L);
        assertThat(schedulerTaskDeviceFieldsDTO1).isNotEqualTo(schedulerTaskDeviceFieldsDTO2);
        schedulerTaskDeviceFieldsDTO1.setId(null);
        assertThat(schedulerTaskDeviceFieldsDTO1).isNotEqualTo(schedulerTaskDeviceFieldsDTO2);
    }
}
