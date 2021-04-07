package com.automation.professional.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link SchedulerTaskDeviceSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SchedulerTaskDeviceSearchRepositoryMockConfiguration {

    @MockBean
    private SchedulerTaskDeviceSearchRepository mockSchedulerTaskDeviceSearchRepository;
}
