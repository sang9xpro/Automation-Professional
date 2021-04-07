package com.automation.professional.repository.search;

import com.automation.professional.domain.CommentValues;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CommentValues} entity.
 */
public interface CommentValuesSearchRepository extends ElasticsearchRepository<CommentValues, Long> {}
