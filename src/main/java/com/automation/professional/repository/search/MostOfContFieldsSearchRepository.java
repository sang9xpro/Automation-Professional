package com.automation.professional.repository.search;

import com.automation.professional.domain.MostOfContFields;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MostOfContFields} entity.
 */
public interface MostOfContFieldsSearchRepository extends ElasticsearchRepository<MostOfContFields, Long> {}
