package com.automation.professional.repository.search;

import com.automation.professional.domain.HistoryFields;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HistoryFields} entity.
 */
public interface HistoryFieldsSearchRepository extends ElasticsearchRepository<HistoryFields, Long> {}
