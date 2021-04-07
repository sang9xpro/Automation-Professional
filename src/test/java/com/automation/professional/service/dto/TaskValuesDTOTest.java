package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskValuesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskValuesDTO.class);
        TaskValuesDTO taskValuesDTO1 = new TaskValuesDTO();
        taskValuesDTO1.setId(1L);
        TaskValuesDTO taskValuesDTO2 = new TaskValuesDTO();
        assertThat(taskValuesDTO1).isNotEqualTo(taskValuesDTO2);
        taskValuesDTO2.setId(taskValuesDTO1.getId());
        assertThat(taskValuesDTO1).isEqualTo(taskValuesDTO2);
        taskValuesDTO2.setId(2L);
        assertThat(taskValuesDTO1).isNotEqualTo(taskValuesDTO2);
        taskValuesDTO1.setId(null);
        assertThat(taskValuesDTO1).isNotEqualTo(taskValuesDTO2);
    }
}
