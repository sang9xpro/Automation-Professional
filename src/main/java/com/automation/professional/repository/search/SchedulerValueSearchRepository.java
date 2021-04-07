package com.automation.professional.repository.search;

import com.automation.professional.domain.SchedulerValue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SchedulerValue} entity.
 */
public interface SchedulerValueSearchRepository extends ElasticsearchRepository<SchedulerValue, Long> {}
