package com.automation.professional.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CommentFieldsSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CommentFieldsSearchRepositoryMockConfiguration {

    @MockBean
    private CommentFieldsSearchRepository mockCommentFieldsSearchRepository;
}
