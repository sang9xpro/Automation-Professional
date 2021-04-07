package com.automation.professional.repository.search;

import com.automation.professional.domain.HistoryValues;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HistoryValues} entity.
 */
public interface HistoryValuesSearchRepository extends ElasticsearchRepository<HistoryValues, Long> {}
