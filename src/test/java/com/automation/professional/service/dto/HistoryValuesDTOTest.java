package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoryValuesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoryValuesDTO.class);
        HistoryValuesDTO historyValuesDTO1 = new HistoryValuesDTO();
        historyValuesDTO1.setId(1L);
        HistoryValuesDTO historyValuesDTO2 = new HistoryValuesDTO();
        assertThat(historyValuesDTO1).isNotEqualTo(historyValuesDTO2);
        historyValuesDTO2.setId(historyValuesDTO1.getId());
        assertThat(historyValuesDTO1).isEqualTo(historyValuesDTO2);
        historyValuesDTO2.setId(2L);
        assertThat(historyValuesDTO1).isNotEqualTo(historyValuesDTO2);
        historyValuesDTO1.setId(null);
        assertThat(historyValuesDTO1).isNotEqualTo(historyValuesDTO2);
    }
}
