package com.automation.professional.repository.search;

import com.automation.professional.domain.SchedulerFields;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SchedulerFields} entity.
 */
public interface SchedulerFieldsSearchRepository extends ElasticsearchRepository<SchedulerFields, Long> {}
