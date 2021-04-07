package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.Loggers;
import com.automation.professional.repository.LoggersRepository;
import com.automation.professional.repository.search.LoggersSearchRepository;
import com.automation.professional.service.LoggersService;
import com.automation.professional.service.dto.LoggersDTO;
import com.automation.professional.service.mapper.LoggersMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Loggers}.
 */
@Service
@Transactional
public class LoggersServiceImpl implements LoggersService {

    private final Logger log = LoggerFactory.getLogger(LoggersServiceImpl.class);

    private final LoggersRepository loggersRepository;

    private final LoggersMapper loggersMapper;

    private final LoggersSearchRepository loggersSearchRepository;

    public LoggersServiceImpl(
        LoggersRepository loggersRepository,
        LoggersMapper loggersMapper,
        LoggersSearchRepository loggersSearchRepository
    ) {
        this.loggersRepository = loggersRepository;
        this.loggersMapper = loggersMapper;
        this.loggersSearchRepository = loggersSearchRepository;
    }

    @Override
    public LoggersDTO save(LoggersDTO loggersDTO) {
        log.debug("Request to save Loggers : {}", loggersDTO);
        Loggers loggers = loggersMapper.toEntity(loggersDTO);
        loggers = loggersRepository.save(loggers);
        LoggersDTO result = loggersMapper.toDto(loggers);
        loggersSearchRepository.save(loggers);
        return result;
    }

    @Override
    public Optional<LoggersDTO> partialUpdate(LoggersDTO loggersDTO) {
        log.debug("Request to partially update Loggers : {}", loggersDTO);

        return loggersRepository
            .findById(loggersDTO.getId())
            .map(
                existingLoggers -> {
                    loggersMapper.partialUpdate(existingLoggers, loggersDTO);
                    return existingLoggers;
                }
            )
            .map(loggersRepository::save)
            .map(
                savedLoggers -> {
                    loggersSearchRepository.save(savedLoggers);

                    return savedLoggers;
                }
            )
            .map(loggersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoggersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Loggers");
        return loggersRepository.findAll(pageable).map(loggersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoggersDTO> findOne(Long id) {
        log.debug("Request to get Loggers : {}", id);
        return loggersRepository.findById(id).map(loggersMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Loggers : {}", id);
        loggersRepository.deleteById(id);
        loggersSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoggersDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Loggers for query {}", query);
        return loggersSearchRepository.search(queryStringQuery(query), pageable).map(loggersMapper::toDto);
    }
}
