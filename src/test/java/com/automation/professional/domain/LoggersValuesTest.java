package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoggersValuesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoggersValues.class);
        LoggersValues loggersValues1 = new LoggersValues();
        loggersValues1.setId(1L);
        LoggersValues loggersValues2 = new LoggersValues();
        loggersValues2.setId(loggersValues1.getId());
        assertThat(loggersValues1).isEqualTo(loggersValues2);
        loggersValues2.setId(2L);
        assertThat(loggersValues1).isNotEqualTo(loggersValues2);
        loggersValues1.setId(null);
        assertThat(loggersValues1).isNotEqualTo(loggersValues2);
    }
}
