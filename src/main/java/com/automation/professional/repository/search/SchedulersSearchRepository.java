package com.automation.professional.repository.search;

import com.automation.professional.domain.Schedulers;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Schedulers} entity.
 */
public interface SchedulersSearchRepository extends ElasticsearchRepository<Schedulers, Long> {}
