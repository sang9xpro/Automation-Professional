package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulerTaskDeviceValuesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulerTaskDeviceValuesDTO.class);
        SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO1 = new SchedulerTaskDeviceValuesDTO();
        schedulerTaskDeviceValuesDTO1.setId(1L);
        SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO2 = new SchedulerTaskDeviceValuesDTO();
        assertThat(schedulerTaskDeviceValuesDTO1).isNotEqualTo(schedulerTaskDeviceValuesDTO2);
        schedulerTaskDeviceValuesDTO2.setId(schedulerTaskDeviceValuesDTO1.getId());
        assertThat(schedulerTaskDeviceValuesDTO1).isEqualTo(schedulerTaskDeviceValuesDTO2);
        schedulerTaskDeviceValuesDTO2.setId(2L);
        assertThat(schedulerTaskDeviceValuesDTO1).isNotEqualTo(schedulerTaskDeviceValuesDTO2);
        schedulerTaskDeviceValuesDTO1.setId(null);
        assertThat(schedulerTaskDeviceValuesDTO1).isNotEqualTo(schedulerTaskDeviceValuesDTO2);
    }
}
