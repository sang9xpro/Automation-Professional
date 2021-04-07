package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulerValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulerValue.class);
        SchedulerValue schedulerValue1 = new SchedulerValue();
        schedulerValue1.setId(1L);
        SchedulerValue schedulerValue2 = new SchedulerValue();
        schedulerValue2.setId(schedulerValue1.getId());
        assertThat(schedulerValue1).isEqualTo(schedulerValue2);
        schedulerValue2.setId(2L);
        assertThat(schedulerValue1).isNotEqualTo(schedulerValue2);
        schedulerValue1.setId(null);
        assertThat(schedulerValue1).isNotEqualTo(schedulerValue2);
    }
}
