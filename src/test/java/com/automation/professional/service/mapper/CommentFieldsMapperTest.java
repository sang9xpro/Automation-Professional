package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentFieldsMapperTest {

    private CommentFieldsMapper commentFieldsMapper;

    @BeforeEach
    public void setUp() {
        commentFieldsMapper = new CommentFieldsMapperImpl();
    }
}
