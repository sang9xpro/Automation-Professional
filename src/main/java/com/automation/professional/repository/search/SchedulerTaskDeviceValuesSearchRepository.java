package com.automation.professional.repository.search;

import com.automation.professional.domain.SchedulerTaskDeviceValues;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SchedulerTaskDeviceValues} entity.
 */
public interface SchedulerTaskDeviceValuesSearchRepository extends ElasticsearchRepository<SchedulerTaskDeviceValues, Long> {}
