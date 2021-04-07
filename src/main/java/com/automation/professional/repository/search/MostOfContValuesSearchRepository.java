package com.automation.professional.repository.search;

import com.automation.professional.domain.MostOfContValues;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MostOfContValues} entity.
 */
public interface MostOfContValuesSearchRepository extends ElasticsearchRepository<MostOfContValues, Long> {}
