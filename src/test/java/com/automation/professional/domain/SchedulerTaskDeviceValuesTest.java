package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulerTaskDeviceValuesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulerTaskDeviceValues.class);
        SchedulerTaskDeviceValues schedulerTaskDeviceValues1 = new SchedulerTaskDeviceValues();
        schedulerTaskDeviceValues1.setId(1L);
        SchedulerTaskDeviceValues schedulerTaskDeviceValues2 = new SchedulerTaskDeviceValues();
        schedulerTaskDeviceValues2.setId(schedulerTaskDeviceValues1.getId());
        assertThat(schedulerTaskDeviceValues1).isEqualTo(schedulerTaskDeviceValues2);
        schedulerTaskDeviceValues2.setId(2L);
        assertThat(schedulerTaskDeviceValues1).isNotEqualTo(schedulerTaskDeviceValues2);
        schedulerTaskDeviceValues1.setId(null);
        assertThat(schedulerTaskDeviceValues1).isNotEqualTo(schedulerTaskDeviceValues2);
    }
}
