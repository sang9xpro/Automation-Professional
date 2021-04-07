package com.automation.professional.repository.search;

import com.automation.professional.domain.LoggersFields;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link LoggersFields} entity.
 */
public interface LoggersFieldsSearchRepository extends ElasticsearchRepository<LoggersFields, Long> {}
