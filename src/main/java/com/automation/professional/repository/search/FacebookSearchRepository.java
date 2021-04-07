package com.automation.professional.repository.search;

import com.automation.professional.domain.Facebook;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Facebook} entity.
 */
public interface FacebookSearchRepository extends ElasticsearchRepository<Facebook, Long> {}
