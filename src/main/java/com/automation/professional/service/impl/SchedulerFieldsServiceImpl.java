package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.SchedulerFields;
import com.automation.professional.repository.SchedulerFieldsRepository;
import com.automation.professional.repository.search.SchedulerFieldsSearchRepository;
import com.automation.professional.service.SchedulerFieldsService;
import com.automation.professional.service.dto.SchedulerFieldsDTO;
import com.automation.professional.service.mapper.SchedulerFieldsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SchedulerFields}.
 */
@Service
@Transactional
public class SchedulerFieldsServiceImpl implements SchedulerFieldsService {

    private final Logger log = LoggerFactory.getLogger(SchedulerFieldsServiceImpl.class);

    private final SchedulerFieldsRepository schedulerFieldsRepository;

    private final SchedulerFieldsMapper schedulerFieldsMapper;

    private final SchedulerFieldsSearchRepository schedulerFieldsSearchRepository;

    public SchedulerFieldsServiceImpl(
        SchedulerFieldsRepository schedulerFieldsRepository,
        SchedulerFieldsMapper schedulerFieldsMapper,
        SchedulerFieldsSearchRepository schedulerFieldsSearchRepository
    ) {
        this.schedulerFieldsRepository = schedulerFieldsRepository;
        this.schedulerFieldsMapper = schedulerFieldsMapper;
        this.schedulerFieldsSearchRepository = schedulerFieldsSearchRepository;
    }

    @Override
    public SchedulerFieldsDTO save(SchedulerFieldsDTO schedulerFieldsDTO) {
        log.debug("Request to save SchedulerFields : {}", schedulerFieldsDTO);
        SchedulerFields schedulerFields = schedulerFieldsMapper.toEntity(schedulerFieldsDTO);
        schedulerFields = schedulerFieldsRepository.save(schedulerFields);
        SchedulerFieldsDTO result = schedulerFieldsMapper.toDto(schedulerFields);
        schedulerFieldsSearchRepository.save(schedulerFields);
        return result;
    }

    @Override
    public Optional<SchedulerFieldsDTO> partialUpdate(SchedulerFieldsDTO schedulerFieldsDTO) {
        log.debug("Request to partially update SchedulerFields : {}", schedulerFieldsDTO);

        return schedulerFieldsRepository
            .findById(schedulerFieldsDTO.getId())
            .map(
                existingSchedulerFields -> {
                    schedulerFieldsMapper.partialUpdate(existingSchedulerFields, schedulerFieldsDTO);
                    return existingSchedulerFields;
                }
            )
            .map(schedulerFieldsRepository::save)
            .map(
                savedSchedulerFields -> {
                    schedulerFieldsSearchRepository.save(savedSchedulerFields);

                    return savedSchedulerFields;
                }
            )
            .map(schedulerFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerFieldsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SchedulerFields");
        return schedulerFieldsRepository.findAll(pageable).map(schedulerFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SchedulerFieldsDTO> findOne(Long id) {
        log.debug("Request to get SchedulerFields : {}", id);
        return schedulerFieldsRepository.findById(id).map(schedulerFieldsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SchedulerFields : {}", id);
        schedulerFieldsRepository.deleteById(id);
        schedulerFieldsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerFieldsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SchedulerFields for query {}", query);
        return schedulerFieldsSearchRepository.search(queryStringQuery(query), pageable).map(schedulerFieldsMapper::toDto);
    }
}
