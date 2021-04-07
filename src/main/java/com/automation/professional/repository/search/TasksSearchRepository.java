package com.automation.professional.repository.search;

import com.automation.professional.domain.Tasks;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Tasks} entity.
 */
public interface TasksSearchRepository extends ElasticsearchRepository<Tasks, Long> {}
