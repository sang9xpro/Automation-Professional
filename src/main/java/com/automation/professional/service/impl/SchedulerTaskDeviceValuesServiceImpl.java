package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.SchedulerTaskDeviceValues;
import com.automation.professional.repository.SchedulerTaskDeviceValuesRepository;
import com.automation.professional.repository.search.SchedulerTaskDeviceValuesSearchRepository;
import com.automation.professional.service.SchedulerTaskDeviceValuesService;
import com.automation.professional.service.dto.SchedulerTaskDeviceValuesDTO;
import com.automation.professional.service.mapper.SchedulerTaskDeviceValuesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SchedulerTaskDeviceValues}.
 */
@Service
@Transactional
public class SchedulerTaskDeviceValuesServiceImpl implements SchedulerTaskDeviceValuesService {

    private final Logger log = LoggerFactory.getLogger(SchedulerTaskDeviceValuesServiceImpl.class);

    private final SchedulerTaskDeviceValuesRepository schedulerTaskDeviceValuesRepository;

    private final SchedulerTaskDeviceValuesMapper schedulerTaskDeviceValuesMapper;

    private final SchedulerTaskDeviceValuesSearchRepository schedulerTaskDeviceValuesSearchRepository;

    public SchedulerTaskDeviceValuesServiceImpl(
        SchedulerTaskDeviceValuesRepository schedulerTaskDeviceValuesRepository,
        SchedulerTaskDeviceValuesMapper schedulerTaskDeviceValuesMapper,
        SchedulerTaskDeviceValuesSearchRepository schedulerTaskDeviceValuesSearchRepository
    ) {
        this.schedulerTaskDeviceValuesRepository = schedulerTaskDeviceValuesRepository;
        this.schedulerTaskDeviceValuesMapper = schedulerTaskDeviceValuesMapper;
        this.schedulerTaskDeviceValuesSearchRepository = schedulerTaskDeviceValuesSearchRepository;
    }

    @Override
    public SchedulerTaskDeviceValuesDTO save(SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO) {
        log.debug("Request to save SchedulerTaskDeviceValues : {}", schedulerTaskDeviceValuesDTO);
        SchedulerTaskDeviceValues schedulerTaskDeviceValues = schedulerTaskDeviceValuesMapper.toEntity(schedulerTaskDeviceValuesDTO);
        schedulerTaskDeviceValues = schedulerTaskDeviceValuesRepository.save(schedulerTaskDeviceValues);
        SchedulerTaskDeviceValuesDTO result = schedulerTaskDeviceValuesMapper.toDto(schedulerTaskDeviceValues);
        schedulerTaskDeviceValuesSearchRepository.save(schedulerTaskDeviceValues);
        return result;
    }

    @Override
    public Optional<SchedulerTaskDeviceValuesDTO> partialUpdate(SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO) {
        log.debug("Request to partially update SchedulerTaskDeviceValues : {}", schedulerTaskDeviceValuesDTO);

        return schedulerTaskDeviceValuesRepository
            .findById(schedulerTaskDeviceValuesDTO.getId())
            .map(
                existingSchedulerTaskDeviceValues -> {
                    schedulerTaskDeviceValuesMapper.partialUpdate(existingSchedulerTaskDeviceValues, schedulerTaskDeviceValuesDTO);
                    return existingSchedulerTaskDeviceValues;
                }
            )
            .map(schedulerTaskDeviceValuesRepository::save)
            .map(
                savedSchedulerTaskDeviceValues -> {
                    schedulerTaskDeviceValuesSearchRepository.save(savedSchedulerTaskDeviceValues);

                    return savedSchedulerTaskDeviceValues;
                }
            )
            .map(schedulerTaskDeviceValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerTaskDeviceValuesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SchedulerTaskDeviceValues");
        return schedulerTaskDeviceValuesRepository.findAll(pageable).map(schedulerTaskDeviceValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SchedulerTaskDeviceValuesDTO> findOne(Long id) {
        log.debug("Request to get SchedulerTaskDeviceValues : {}", id);
        return schedulerTaskDeviceValuesRepository.findById(id).map(schedulerTaskDeviceValuesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SchedulerTaskDeviceValues : {}", id);
        schedulerTaskDeviceValuesRepository.deleteById(id);
        schedulerTaskDeviceValuesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerTaskDeviceValuesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SchedulerTaskDeviceValues for query {}", query);
        return schedulerTaskDeviceValuesSearchRepository
            .search(queryStringQuery(query), pageable)
            .map(schedulerTaskDeviceValuesMapper::toDto);
    }
}
