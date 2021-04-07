package com.automation.professional.repository.search;

import com.automation.professional.domain.Loggers;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Loggers} entity.
 */
public interface LoggersSearchRepository extends ElasticsearchRepository<Loggers, Long> {}
