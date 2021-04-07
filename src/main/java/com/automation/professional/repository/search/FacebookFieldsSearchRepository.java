package com.automation.professional.repository.search;

import com.automation.professional.domain.FacebookFields;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FacebookFields} entity.
 */
public interface FacebookFieldsSearchRepository extends ElasticsearchRepository<FacebookFields, Long> {}
