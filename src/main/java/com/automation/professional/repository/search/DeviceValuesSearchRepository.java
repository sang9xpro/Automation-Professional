package com.automation.professional.repository.search;

import com.automation.professional.domain.DeviceValues;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DeviceValues} entity.
 */
public interface DeviceValuesSearchRepository extends ElasticsearchRepository<DeviceValues, Long> {}
