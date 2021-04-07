package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskFieldsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskFieldsDTO.class);
        TaskFieldsDTO taskFieldsDTO1 = new TaskFieldsDTO();
        taskFieldsDTO1.setId(1L);
        TaskFieldsDTO taskFieldsDTO2 = new TaskFieldsDTO();
        assertThat(taskFieldsDTO1).isNotEqualTo(taskFieldsDTO2);
        taskFieldsDTO2.setId(taskFieldsDTO1.getId());
        assertThat(taskFieldsDTO1).isEqualTo(taskFieldsDTO2);
        taskFieldsDTO2.setId(2L);
        assertThat(taskFieldsDTO1).isNotEqualTo(taskFieldsDTO2);
        taskFieldsDTO1.setId(null);
        assertThat(taskFieldsDTO1).isNotEqualTo(taskFieldsDTO2);
    }
}
