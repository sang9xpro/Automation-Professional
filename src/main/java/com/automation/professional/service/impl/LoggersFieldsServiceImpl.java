package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.LoggersFields;
import com.automation.professional.repository.LoggersFieldsRepository;
import com.automation.professional.repository.search.LoggersFieldsSearchRepository;
import com.automation.professional.service.LoggersFieldsService;
import com.automation.professional.service.dto.LoggersFieldsDTO;
import com.automation.professional.service.mapper.LoggersFieldsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoggersFields}.
 */
@Service
@Transactional
public class LoggersFieldsServiceImpl implements LoggersFieldsService {

    private final Logger log = LoggerFactory.getLogger(LoggersFieldsServiceImpl.class);

    private final LoggersFieldsRepository loggersFieldsRepository;

    private final LoggersFieldsMapper loggersFieldsMapper;

    private final LoggersFieldsSearchRepository loggersFieldsSearchRepository;

    public LoggersFieldsServiceImpl(
        LoggersFieldsRepository loggersFieldsRepository,
        LoggersFieldsMapper loggersFieldsMapper,
        LoggersFieldsSearchRepository loggersFieldsSearchRepository
    ) {
        this.loggersFieldsRepository = loggersFieldsRepository;
        this.loggersFieldsMapper = loggersFieldsMapper;
        this.loggersFieldsSearchRepository = loggersFieldsSearchRepository;
    }

    @Override
    public LoggersFieldsDTO save(LoggersFieldsDTO loggersFieldsDTO) {
        log.debug("Request to save LoggersFields : {}", loggersFieldsDTO);
        LoggersFields loggersFields = loggersFieldsMapper.toEntity(loggersFieldsDTO);
        loggersFields = loggersFieldsRepository.save(loggersFields);
        LoggersFieldsDTO result = loggersFieldsMapper.toDto(loggersFields);
        loggersFieldsSearchRepository.save(loggersFields);
        return result;
    }

    @Override
    public Optional<LoggersFieldsDTO> partialUpdate(LoggersFieldsDTO loggersFieldsDTO) {
        log.debug("Request to partially update LoggersFields : {}", loggersFieldsDTO);

        return loggersFieldsRepository
            .findById(loggersFieldsDTO.getId())
            .map(
                existingLoggersFields -> {
                    loggersFieldsMapper.partialUpdate(existingLoggersFields, loggersFieldsDTO);
                    return existingLoggersFields;
                }
            )
            .map(loggersFieldsRepository::save)
            .map(
                savedLoggersFields -> {
                    loggersFieldsSearchRepository.save(savedLoggersFields);

                    return savedLoggersFields;
                }
            )
            .map(loggersFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoggersFieldsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoggersFields");
        return loggersFieldsRepository.findAll(pageable).map(loggersFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoggersFieldsDTO> findOne(Long id) {
        log.debug("Request to get LoggersFields : {}", id);
        return loggersFieldsRepository.findById(id).map(loggersFieldsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoggersFields : {}", id);
        loggersFieldsRepository.deleteById(id);
        loggersFieldsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoggersFieldsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoggersFields for query {}", query);
        return loggersFieldsSearchRepository.search(queryStringQuery(query), pageable).map(loggersFieldsMapper::toDto);
    }
}
