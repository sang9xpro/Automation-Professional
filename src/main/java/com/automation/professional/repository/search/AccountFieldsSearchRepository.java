package com.automation.professional.repository.search;

import com.automation.professional.domain.AccountFields;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AccountFields} entity.
 */
public interface AccountFieldsSearchRepository extends ElasticsearchRepository<AccountFields, Long> {}
