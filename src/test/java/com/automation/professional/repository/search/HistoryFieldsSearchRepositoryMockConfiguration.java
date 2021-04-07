package com.automation.professional.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link HistoryFieldsSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class HistoryFieldsSearchRepositoryMockConfiguration {

    @MockBean
    private HistoryFieldsSearchRepository mockHistoryFieldsSearchRepository;
}
