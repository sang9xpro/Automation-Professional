package com.automation.professional.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link DevicesSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DevicesSearchRepositoryMockConfiguration {

    @MockBean
    private DevicesSearchRepository mockDevicesSearchRepository;
}
