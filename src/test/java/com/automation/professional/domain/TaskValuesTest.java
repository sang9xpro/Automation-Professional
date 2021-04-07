package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskValuesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskValues.class);
        TaskValues taskValues1 = new TaskValues();
        taskValues1.setId(1L);
        TaskValues taskValues2 = new TaskValues();
        taskValues2.setId(taskValues1.getId());
        assertThat(taskValues1).isEqualTo(taskValues2);
        taskValues2.setId(2L);
        assertThat(taskValues1).isNotEqualTo(taskValues2);
        taskValues1.setId(null);
        assertThat(taskValues1).isNotEqualTo(taskValues2);
    }
}
