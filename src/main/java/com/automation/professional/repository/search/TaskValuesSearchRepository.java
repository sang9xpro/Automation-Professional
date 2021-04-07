package com.automation.professional.repository.search;

import com.automation.professional.domain.TaskValues;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TaskValues} entity.
 */
public interface TaskValuesSearchRepository extends ElasticsearchRepository<TaskValues, Long> {}
