package com.automation.professional.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link SchedulerTaskDeviceValuesSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SchedulerTaskDeviceValuesSearchRepositoryMockConfiguration {

    @MockBean
    private SchedulerTaskDeviceValuesSearchRepository mockSchedulerTaskDeviceValuesSearchRepository;
}
