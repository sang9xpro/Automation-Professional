package com.automation.professional.repository.search;

import com.automation.professional.domain.SchedulerTaskDeviceFields;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SchedulerTaskDeviceFields} entity.
 */
public interface SchedulerTaskDeviceFieldsSearchRepository extends ElasticsearchRepository<SchedulerTaskDeviceFields, Long> {}
