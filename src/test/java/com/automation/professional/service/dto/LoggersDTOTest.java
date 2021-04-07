package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoggersDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoggersDTO.class);
        LoggersDTO loggersDTO1 = new LoggersDTO();
        loggersDTO1.setId(1L);
        LoggersDTO loggersDTO2 = new LoggersDTO();
        assertThat(loggersDTO1).isNotEqualTo(loggersDTO2);
        loggersDTO2.setId(loggersDTO1.getId());
        assertThat(loggersDTO1).isEqualTo(loggersDTO2);
        loggersDTO2.setId(2L);
        assertThat(loggersDTO1).isNotEqualTo(loggersDTO2);
        loggersDTO1.setId(null);
        assertThat(loggersDTO1).isNotEqualTo(loggersDTO2);
    }
}
