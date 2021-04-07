package com.automation.professional.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link LoggersFieldsSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class LoggersFieldsSearchRepositoryMockConfiguration {

    @MockBean
    private LoggersFieldsSearchRepository mockLoggersFieldsSearchRepository;
}
