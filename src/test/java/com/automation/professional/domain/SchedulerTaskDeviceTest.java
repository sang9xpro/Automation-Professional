package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulerTaskDeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulerTaskDevice.class);
        SchedulerTaskDevice schedulerTaskDevice1 = new SchedulerTaskDevice();
        schedulerTaskDevice1.setId(1L);
        SchedulerTaskDevice schedulerTaskDevice2 = new SchedulerTaskDevice();
        schedulerTaskDevice2.setId(schedulerTaskDevice1.getId());
        assertThat(schedulerTaskDevice1).isEqualTo(schedulerTaskDevice2);
        schedulerTaskDevice2.setId(2L);
        assertThat(schedulerTaskDevice1).isNotEqualTo(schedulerTaskDevice2);
        schedulerTaskDevice1.setId(null);
        assertThat(schedulerTaskDevice1).isNotEqualTo(schedulerTaskDevice2);
    }
}
