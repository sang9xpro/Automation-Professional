package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoggersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Loggers.class);
        Loggers loggers1 = new Loggers();
        loggers1.setId(1L);
        Loggers loggers2 = new Loggers();
        loggers2.setId(loggers1.getId());
        assertThat(loggers1).isEqualTo(loggers2);
        loggers2.setId(2L);
        assertThat(loggers1).isNotEqualTo(loggers2);
        loggers1.setId(null);
        assertThat(loggers1).isNotEqualTo(loggers2);
    }
}
