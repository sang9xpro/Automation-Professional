package com.automation.professional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentValuesMapperTest {

    private CommentValuesMapper commentValuesMapper;

    @BeforeEach
    public void setUp() {
        commentValuesMapper = new CommentValuesMapperImpl();
    }
}
