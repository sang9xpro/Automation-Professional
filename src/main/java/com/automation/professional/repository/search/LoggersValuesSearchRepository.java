package com.automation.professional.repository.search;

import com.automation.professional.domain.LoggersValues;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link LoggersValues} entity.
 */
public interface LoggersValuesSearchRepository extends ElasticsearchRepository<LoggersValues, Long> {}
