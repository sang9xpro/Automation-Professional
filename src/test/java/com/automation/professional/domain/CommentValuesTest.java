package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentValuesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentValues.class);
        CommentValues commentValues1 = new CommentValues();
        commentValues1.setId(1L);
        CommentValues commentValues2 = new CommentValues();
        commentValues2.setId(commentValues1.getId());
        assertThat(commentValues1).isEqualTo(commentValues2);
        commentValues2.setId(2L);
        assertThat(commentValues1).isNotEqualTo(commentValues2);
        commentValues1.setId(null);
        assertThat(commentValues1).isNotEqualTo(commentValues2);
    }
}
