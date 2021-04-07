package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoggersFieldsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoggersFieldsDTO.class);
        LoggersFieldsDTO loggersFieldsDTO1 = new LoggersFieldsDTO();
        loggersFieldsDTO1.setId(1L);
        LoggersFieldsDTO loggersFieldsDTO2 = new LoggersFieldsDTO();
        assertThat(loggersFieldsDTO1).isNotEqualTo(loggersFieldsDTO2);
        loggersFieldsDTO2.setId(loggersFieldsDTO1.getId());
        assertThat(loggersFieldsDTO1).isEqualTo(loggersFieldsDTO2);
        loggersFieldsDTO2.setId(2L);
        assertThat(loggersFieldsDTO1).isNotEqualTo(loggersFieldsDTO2);
        loggersFieldsDTO1.setId(null);
        assertThat(loggersFieldsDTO1).isNotEqualTo(loggersFieldsDTO2);
    }
}
