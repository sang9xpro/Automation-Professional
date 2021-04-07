package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulerTaskDeviceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulerTaskDeviceDTO.class);
        SchedulerTaskDeviceDTO schedulerTaskDeviceDTO1 = new SchedulerTaskDeviceDTO();
        schedulerTaskDeviceDTO1.setId(1L);
        SchedulerTaskDeviceDTO schedulerTaskDeviceDTO2 = new SchedulerTaskDeviceDTO();
        assertThat(schedulerTaskDeviceDTO1).isNotEqualTo(schedulerTaskDeviceDTO2);
        schedulerTaskDeviceDTO2.setId(schedulerTaskDeviceDTO1.getId());
        assertThat(schedulerTaskDeviceDTO1).isEqualTo(schedulerTaskDeviceDTO2);
        schedulerTaskDeviceDTO2.setId(2L);
        assertThat(schedulerTaskDeviceDTO1).isNotEqualTo(schedulerTaskDeviceDTO2);
        schedulerTaskDeviceDTO1.setId(null);
        assertThat(schedulerTaskDeviceDTO1).isNotEqualTo(schedulerTaskDeviceDTO2);
    }
}
