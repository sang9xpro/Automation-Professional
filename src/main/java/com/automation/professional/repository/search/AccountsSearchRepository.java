package com.automation.professional.repository.search;

import com.automation.professional.domain.Accounts;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Accounts} entity.
 */
public interface AccountsSearchRepository extends ElasticsearchRepository<Accounts, Long> {}
