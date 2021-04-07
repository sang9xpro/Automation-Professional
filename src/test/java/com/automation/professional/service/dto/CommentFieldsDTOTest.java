package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentFieldsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentFieldsDTO.class);
        CommentFieldsDTO commentFieldsDTO1 = new CommentFieldsDTO();
        commentFieldsDTO1.setId(1L);
        CommentFieldsDTO commentFieldsDTO2 = new CommentFieldsDTO();
        assertThat(commentFieldsDTO1).isNotEqualTo(commentFieldsDTO2);
        commentFieldsDTO2.setId(commentFieldsDTO1.getId());
        assertThat(commentFieldsDTO1).isEqualTo(commentFieldsDTO2);
        commentFieldsDTO2.setId(2L);
        assertThat(commentFieldsDTO1).isNotEqualTo(commentFieldsDTO2);
        commentFieldsDTO1.setId(null);
        assertThat(commentFieldsDTO1).isNotEqualTo(commentFieldsDTO2);
    }
}
