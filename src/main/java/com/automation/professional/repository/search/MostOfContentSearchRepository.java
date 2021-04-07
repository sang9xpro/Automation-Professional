package com.automation.professional.repository.search;

import com.automation.professional.domain.MostOfContent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MostOfContent} entity.
 */
public interface MostOfContentSearchRepository extends ElasticsearchRepository<MostOfContent, Long> {}
