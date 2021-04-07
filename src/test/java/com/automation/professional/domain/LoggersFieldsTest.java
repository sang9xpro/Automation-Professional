package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoggersFieldsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoggersFields.class);
        LoggersFields loggersFields1 = new LoggersFields();
        loggersFields1.setId(1L);
        LoggersFields loggersFields2 = new LoggersFields();
        loggersFields2.setId(loggersFields1.getId());
        assertThat(loggersFields1).isEqualTo(loggersFields2);
        loggersFields2.setId(2L);
        assertThat(loggersFields1).isNotEqualTo(loggersFields2);
        loggersFields1.setId(null);
        assertThat(loggersFields1).isNotEqualTo(loggersFields2);
    }
}
