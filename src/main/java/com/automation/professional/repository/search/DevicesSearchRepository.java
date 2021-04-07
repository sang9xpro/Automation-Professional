package com.automation.professional.repository.search;

import com.automation.professional.domain.Devices;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Devices} entity.
 */
public interface DevicesSearchRepository extends ElasticsearchRepository<Devices, Long> {}
