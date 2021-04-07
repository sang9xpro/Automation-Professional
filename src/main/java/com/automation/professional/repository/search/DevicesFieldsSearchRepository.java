package com.automation.professional.repository.search;

import com.automation.professional.domain.DevicesFields;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DevicesFields} entity.
 */
public interface DevicesFieldsSearchRepository extends ElasticsearchRepository<DevicesFields, Long> {}
