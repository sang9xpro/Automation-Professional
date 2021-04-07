package com.automation.professional.repository.search;

import com.automation.professional.domain.CommentFields;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CommentFields} entity.
 */
public interface CommentFieldsSearchRepository extends ElasticsearchRepository<CommentFields, Long> {}
