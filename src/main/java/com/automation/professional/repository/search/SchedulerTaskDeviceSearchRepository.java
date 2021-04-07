package com.automation.professional.repository.search;

import com.automation.professional.domain.SchedulerTaskDevice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SchedulerTaskDevice} entity.
 */
public interface SchedulerTaskDeviceSearchRepository extends ElasticsearchRepository<SchedulerTaskDevice, Long> {}
