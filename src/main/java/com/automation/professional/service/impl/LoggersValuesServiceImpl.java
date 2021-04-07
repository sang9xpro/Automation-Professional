package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.LoggersValues;
import com.automation.professional.repository.LoggersValuesRepository;
import com.automation.professional.repository.search.LoggersValuesSearchRepository;
import com.automation.professional.service.LoggersValuesService;
import com.automation.professional.service.dto.LoggersValuesDTO;
import com.automation.professional.service.mapper.LoggersValuesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoggersValues}.
 */
@Service
@Transactional
public class LoggersValuesServiceImpl implements LoggersValuesService {

    private final Logger log = LoggerFactory.getLogger(LoggersValuesServiceImpl.class);

    private final LoggersValuesRepository loggersValuesRepository;

    private final LoggersValuesMapper loggersValuesMapper;

    private final LoggersValuesSearchRepository loggersValuesSearchRepository;

    public LoggersValuesServiceImpl(
        LoggersValuesRepository loggersValuesRepository,
        LoggersValuesMapper loggersValuesMapper,
        LoggersValuesSearchRepository loggersValuesSearchRepository
    ) {
        this.loggersValuesRepository = loggersValuesRepository;
        this.loggersValuesMapper = loggersValuesMapper;
        this.loggersValuesSearchRepository = loggersValuesSearchRepository;
    }

    @Override
    public LoggersValuesDTO save(LoggersValuesDTO loggersValuesDTO) {
        log.debug("Request to save LoggersValues : {}", loggersValuesDTO);
        LoggersValues loggersValues = loggersValuesMapper.toEntity(loggersValuesDTO);
        loggersValues = loggersValuesRepository.save(loggersValues);
        LoggersValuesDTO result = loggersValuesMapper.toDto(loggersValues);
        loggersValuesSearchRepository.save(loggersValues);
        return result;
    }

    @Override
    public Optional<LoggersValuesDTO> partialUpdate(LoggersValuesDTO loggersValuesDTO) {
        log.debug("Request to partially update LoggersValues : {}", loggersValuesDTO);

        return loggersValuesRepository
            .findById(loggersValuesDTO.getId())
            .map(
                existingLoggersValues -> {
                    loggersValuesMapper.partialUpdate(existingLoggersValues, loggersValuesDTO);
                    return existingLoggersValues;
                }
            )
            .map(loggersValuesRepository::save)
            .map(
                savedLoggersValues -> {
                    loggersValuesSearchRepository.save(savedLoggersValues);

                    return savedLoggersValues;
                }
            )
            .map(loggersValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoggersValuesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoggersValues");
        return loggersValuesRepository.findAll(pageable).map(loggersValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoggersValuesDTO> findOne(Long id) {
        log.debug("Request to get LoggersValues : {}", id);
        return loggersValuesRepository.findById(id).map(loggersValuesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoggersValues : {}", id);
        loggersValuesRepository.deleteById(id);
        loggersValuesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoggersValuesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoggersValues for query {}", query);
        return loggersValuesSearchRepository.search(queryStringQuery(query), pageable).map(loggersValuesMapper::toDto);
    }
}
