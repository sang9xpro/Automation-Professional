package com.automation.professional.repository.search;

import com.automation.professional.domain.FacebookValues;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FacebookValues} entity.
 */
public interface FacebookValuesSearchRepository extends ElasticsearchRepository<FacebookValues, Long> {}
