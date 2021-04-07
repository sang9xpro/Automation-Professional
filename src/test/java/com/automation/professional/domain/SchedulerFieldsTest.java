package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulerFieldsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulerFields.class);
        SchedulerFields schedulerFields1 = new SchedulerFields();
        schedulerFields1.setId(1L);
        SchedulerFields schedulerFields2 = new SchedulerFields();
        schedulerFields2.setId(schedulerFields1.getId());
        assertThat(schedulerFields1).isEqualTo(schedulerFields2);
        schedulerFields2.setId(2L);
        assertThat(schedulerFields1).isNotEqualTo(schedulerFields2);
        schedulerFields1.setId(null);
        assertThat(schedulerFields1).isNotEqualTo(schedulerFields2);
    }
}
