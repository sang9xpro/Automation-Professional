package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulerTaskDeviceFieldsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulerTaskDeviceFields.class);
        SchedulerTaskDeviceFields schedulerTaskDeviceFields1 = new SchedulerTaskDeviceFields();
        schedulerTaskDeviceFields1.setId(1L);
        SchedulerTaskDeviceFields schedulerTaskDeviceFields2 = new SchedulerTaskDeviceFields();
        schedulerTaskDeviceFields2.setId(schedulerTaskDeviceFields1.getId());
        assertThat(schedulerTaskDeviceFields1).isEqualTo(schedulerTaskDeviceFields2);
        schedulerTaskDeviceFields2.setId(2L);
        assertThat(schedulerTaskDeviceFields1).isNotEqualTo(schedulerTaskDeviceFields2);
        schedulerTaskDeviceFields1.setId(null);
        assertThat(schedulerTaskDeviceFields1).isNotEqualTo(schedulerTaskDeviceFields2);
    }
}
