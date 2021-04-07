package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoryValuesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoryValues.class);
        HistoryValues historyValues1 = new HistoryValues();
        historyValues1.setId(1L);
        HistoryValues historyValues2 = new HistoryValues();
        historyValues2.setId(historyValues1.getId());
        assertThat(historyValues1).isEqualTo(historyValues2);
        historyValues2.setId(2L);
        assertThat(historyValues1).isNotEqualTo(historyValues2);
        historyValues1.setId(null);
        assertThat(historyValues1).isNotEqualTo(historyValues2);
    }
}
