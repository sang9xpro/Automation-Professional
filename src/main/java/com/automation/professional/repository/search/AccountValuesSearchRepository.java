package com.automation.professional.repository.search;

import com.automation.professional.domain.AccountValues;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AccountValues} entity.
 */
public interface AccountValuesSearchRepository extends ElasticsearchRepository<AccountValues, Long> {}
