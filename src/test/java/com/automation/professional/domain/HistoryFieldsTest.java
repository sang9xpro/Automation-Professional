package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoryFieldsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoryFields.class);
        HistoryFields historyFields1 = new HistoryFields();
        historyFields1.setId(1L);
        HistoryFields historyFields2 = new HistoryFields();
        historyFields2.setId(historyFields1.getId());
        assertThat(historyFields1).isEqualTo(historyFields2);
        historyFields2.setId(2L);
        assertThat(historyFields1).isNotEqualTo(historyFields2);
        historyFields1.setId(null);
        assertThat(historyFields1).isNotEqualTo(historyFields2);
    }
}
