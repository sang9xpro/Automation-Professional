package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentValuesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentValuesDTO.class);
        CommentValuesDTO commentValuesDTO1 = new CommentValuesDTO();
        commentValuesDTO1.setId(1L);
        CommentValuesDTO commentValuesDTO2 = new CommentValuesDTO();
        assertThat(commentValuesDTO1).isNotEqualTo(commentValuesDTO2);
        commentValuesDTO2.setId(commentValuesDTO1.getId());
        assertThat(commentValuesDTO1).isEqualTo(commentValuesDTO2);
        commentValuesDTO2.setId(2L);
        assertThat(commentValuesDTO1).isNotEqualTo(commentValuesDTO2);
        commentValuesDTO1.setId(null);
        assertThat(commentValuesDTO1).isNotEqualTo(commentValuesDTO2);
    }
}
