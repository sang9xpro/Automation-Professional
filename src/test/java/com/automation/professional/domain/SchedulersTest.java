package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Schedulers.class);
        Schedulers schedulers1 = new Schedulers();
        schedulers1.setId(1L);
        Schedulers schedulers2 = new Schedulers();
        schedulers2.setId(schedulers1.getId());
        assertThat(schedulers1).isEqualTo(schedulers2);
        schedulers2.setId(2L);
        assertThat(schedulers1).isNotEqualTo(schedulers2);
        schedulers1.setId(null);
        assertThat(schedulers1).isNotEqualTo(schedulers2);
    }
}
