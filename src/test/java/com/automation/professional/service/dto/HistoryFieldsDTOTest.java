package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoryFieldsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoryFieldsDTO.class);
        HistoryFieldsDTO historyFieldsDTO1 = new HistoryFieldsDTO();
        historyFieldsDTO1.setId(1L);
        HistoryFieldsDTO historyFieldsDTO2 = new HistoryFieldsDTO();
        assertThat(historyFieldsDTO1).isNotEqualTo(historyFieldsDTO2);
        historyFieldsDTO2.setId(historyFieldsDTO1.getId());
        assertThat(historyFieldsDTO1).isEqualTo(historyFieldsDTO2);
        historyFieldsDTO2.setId(2L);
        assertThat(historyFieldsDTO1).isNotEqualTo(historyFieldsDTO2);
        historyFieldsDTO1.setId(null);
        assertThat(historyFieldsDTO1).isNotEqualTo(historyFieldsDTO2);
    }
}
