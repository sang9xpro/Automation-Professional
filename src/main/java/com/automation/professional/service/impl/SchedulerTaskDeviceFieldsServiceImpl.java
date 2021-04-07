package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.SchedulerTaskDeviceFields;
import com.automation.professional.repository.SchedulerTaskDeviceFieldsRepository;
import com.automation.professional.repository.search.SchedulerTaskDeviceFieldsSearchRepository;
import com.automation.professional.service.SchedulerTaskDeviceFieldsService;
import com.automation.professional.service.dto.SchedulerTaskDeviceFieldsDTO;
import com.automation.professional.service.mapper.SchedulerTaskDeviceFieldsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SchedulerTaskDeviceFields}.
 */
@Service
@Transactional
public class SchedulerTaskDeviceFieldsServiceImpl implements SchedulerTaskDeviceFieldsService {

    private final Logger log = LoggerFactory.getLogger(SchedulerTaskDeviceFieldsServiceImpl.class);

    private final SchedulerTaskDeviceFieldsRepository schedulerTaskDeviceFieldsRepository;

    private final SchedulerTaskDeviceFieldsMapper schedulerTaskDeviceFieldsMapper;

    private final SchedulerTaskDeviceFieldsSearchRepository schedulerTaskDeviceFieldsSearchRepository;

    public SchedulerTaskDeviceFieldsServiceImpl(
        SchedulerTaskDeviceFieldsRepository schedulerTaskDeviceFieldsRepository,
        SchedulerTaskDeviceFieldsMapper schedulerTaskDeviceFieldsMapper,
        SchedulerTaskDeviceFieldsSearchRepository schedulerTaskDeviceFieldsSearchRepository
    ) {
        this.schedulerTaskDeviceFieldsRepository = schedulerTaskDeviceFieldsRepository;
        this.schedulerTaskDeviceFieldsMapper = schedulerTaskDeviceFieldsMapper;
        this.schedulerTaskDeviceFieldsSearchRepository = schedulerTaskDeviceFieldsSearchRepository;
    }

    @Override
    public SchedulerTaskDeviceFieldsDTO save(SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO) {
        log.debug("Request to save SchedulerTaskDeviceFields : {}", schedulerTaskDeviceFieldsDTO);
        SchedulerTaskDeviceFields schedulerTaskDeviceFields = schedulerTaskDeviceFieldsMapper.toEntity(schedulerTaskDeviceFieldsDTO);
        schedulerTaskDeviceFields = schedulerTaskDeviceFieldsRepository.save(schedulerTaskDeviceFields);
        SchedulerTaskDeviceFieldsDTO result = schedulerTaskDeviceFieldsMapper.toDto(schedulerTaskDeviceFields);
        schedulerTaskDeviceFieldsSearchRepository.save(schedulerTaskDeviceFields);
        return result;
    }

    @Override
    public Optional<SchedulerTaskDeviceFieldsDTO> partialUpdate(SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO) {
        log.debug("Request to partially update SchedulerTaskDeviceFields : {}", schedulerTaskDeviceFieldsDTO);

        return schedulerTaskDeviceFieldsRepository
            .findById(schedulerTaskDeviceFieldsDTO.getId())
            .map(
                existingSchedulerTaskDeviceFields -> {
                    schedulerTaskDeviceFieldsMapper.partialUpdate(existingSchedulerTaskDeviceFields, schedulerTaskDeviceFieldsDTO);
                    return existingSchedulerTaskDeviceFields;
                }
            )
            .map(schedulerTaskDeviceFieldsRepository::save)
            .map(
                savedSchedulerTaskDeviceFields -> {
                    schedulerTaskDeviceFieldsSearchRepository.save(savedSchedulerTaskDeviceFields);

                    return savedSchedulerTaskDeviceFields;
                }
            )
            .map(schedulerTaskDeviceFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerTaskDeviceFieldsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SchedulerTaskDeviceFields");
        return schedulerTaskDeviceFieldsRepository.findAll(pageable).map(schedulerTaskDeviceFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SchedulerTaskDeviceFieldsDTO> findOne(Long id) {
        log.debug("Request to get SchedulerTaskDeviceFields : {}", id);
        return schedulerTaskDeviceFieldsRepository.findById(id).map(schedulerTaskDeviceFieldsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SchedulerTaskDeviceFields : {}", id);
        schedulerTaskDeviceFieldsRepository.deleteById(id);
        schedulerTaskDeviceFieldsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerTaskDeviceFieldsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SchedulerTaskDeviceFields for query {}", query);
        return schedulerTaskDeviceFieldsSearchRepository
            .search(queryStringQuery(query), pageable)
            .map(schedulerTaskDeviceFieldsMapper::toDto);
    }
}
