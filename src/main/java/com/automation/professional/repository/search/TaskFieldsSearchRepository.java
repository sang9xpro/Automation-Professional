package com.automation.professional.repository.search;

import com.automation.professional.domain.TaskFields;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TaskFields} entity.
 */
public interface TaskFieldsSearchRepository extends ElasticsearchRepository<TaskFields, Long> {}
