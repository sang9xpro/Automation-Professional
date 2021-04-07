package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoggersValuesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoggersValuesDTO.class);
        LoggersValuesDTO loggersValuesDTO1 = new LoggersValuesDTO();
        loggersValuesDTO1.setId(1L);
        LoggersValuesDTO loggersValuesDTO2 = new LoggersValuesDTO();
        assertThat(loggersValuesDTO1).isNotEqualTo(loggersValuesDTO2);
        loggersValuesDTO2.setId(loggersValuesDTO1.getId());
        assertThat(loggersValuesDTO1).isEqualTo(loggersValuesDTO2);
        loggersValuesDTO2.setId(2L);
        assertThat(loggersValuesDTO1).isNotEqualTo(loggersValuesDTO2);
        loggersValuesDTO1.setId(null);
        assertThat(loggersValuesDTO1).isNotEqualTo(loggersValuesDTO2);
    }
}
