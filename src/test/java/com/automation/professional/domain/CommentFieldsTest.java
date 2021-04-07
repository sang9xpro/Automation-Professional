package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentFieldsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentFields.class);
        CommentFields commentFields1 = new CommentFields();
        commentFields1.setId(1L);
        CommentFields commentFields2 = new CommentFields();
        commentFields2.setId(commentFields1.getId());
        assertThat(commentFields1).isEqualTo(commentFields2);
        commentFields2.setId(2L);
        assertThat(commentFields1).isNotEqualTo(commentFields2);
        commentFields1.setId(null);
        assertThat(commentFields1).isNotEqualTo(commentFields2);
    }
}
