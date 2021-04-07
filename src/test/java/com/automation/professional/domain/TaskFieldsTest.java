package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskFieldsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskFields.class);
        TaskFields taskFields1 = new TaskFields();
        taskFields1.setId(1L);
        TaskFields taskFields2 = new TaskFields();
        taskFields2.setId(taskFields1.getId());
        assertThat(taskFields1).isEqualTo(taskFields2);
        taskFields2.setId(2L);
        assertThat(taskFields1).isNotEqualTo(taskFields2);
        taskFields1.setId(null);
        assertThat(taskFields1).isNotEqualTo(taskFields2);
    }
}
