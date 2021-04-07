package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.SchedulerValue;
import com.automation.professional.repository.SchedulerValueRepository;
import com.automation.professional.repository.search.SchedulerValueSearchRepository;
import com.automation.professional.service.SchedulerValueService;
import com.automation.professional.service.dto.SchedulerValueDTO;
import com.automation.professional.service.mapper.SchedulerValueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SchedulerValue}.
 */
@Service
@Transactional
public class SchedulerValueServiceImpl implements SchedulerValueService {

    private final Logger log = LoggerFactory.getLogger(SchedulerValueServiceImpl.class);

    private final SchedulerValueRepository schedulerValueRepository;

    private final SchedulerValueMapper schedulerValueMapper;

    private final SchedulerValueSearchRepository schedulerValueSearchRepository;

    public SchedulerValueServiceImpl(
        SchedulerValueRepository schedulerValueRepository,
        SchedulerValueMapper schedulerValueMapper,
        SchedulerValueSearchRepository schedulerValueSearchRepository
    ) {
        this.schedulerValueRepository = schedulerValueRepository;
        this.schedulerValueMapper = schedulerValueMapper;
        this.schedulerValueSearchRepository = schedulerValueSearchRepository;
    }

    @Override
    public SchedulerValueDTO save(SchedulerValueDTO schedulerValueDTO) {
        log.debug("Request to save SchedulerValue : {}", schedulerValueDTO);
        SchedulerValue schedulerValue = schedulerValueMapper.toEntity(schedulerValueDTO);
        schedulerValue = schedulerValueRepository.save(schedulerValue);
        SchedulerValueDTO result = schedulerValueMapper.toDto(schedulerValue);
        schedulerValueSearchRepository.save(schedulerValue);
        return result;
    }

    @Override
    public Optional<SchedulerValueDTO> partialUpdate(SchedulerValueDTO schedulerValueDTO) {
        log.debug("Request to partially update SchedulerValue : {}", schedulerValueDTO);

        return schedulerValueRepository
            .findById(schedulerValueDTO.getId())
            .map(
                existingSchedulerValue -> {
                    schedulerValueMapper.partialUpdate(existingSchedulerValue, schedulerValueDTO);
                    return existingSchedulerValue;
                }
            )
            .map(schedulerValueRepository::save)
            .map(
                savedSchedulerValue -> {
                    schedulerValueSearchRepository.save(savedSchedulerValue);

                    return savedSchedulerValue;
                }
            )
            .map(schedulerValueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SchedulerValues");
        return schedulerValueRepository.findAll(pageable).map(schedulerValueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SchedulerValueDTO> findOne(Long id) {
        log.debug("Request to get SchedulerValue : {}", id);
        return schedulerValueRepository.findById(id).map(schedulerValueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SchedulerValue : {}", id);
        schedulerValueRepository.deleteById(id);
        schedulerValueSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerValueDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SchedulerValues for query {}", query);
        return schedulerValueSearchRepository.search(queryStringQuery(query), pageable).map(schedulerValueMapper::toDto);
    }
}
