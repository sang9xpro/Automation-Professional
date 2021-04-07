package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.HistoryValues;
import com.automation.professional.repository.HistoryValuesRepository;
import com.automation.professional.repository.search.HistoryValuesSearchRepository;
import com.automation.professional.service.HistoryValuesService;
import com.automation.professional.service.dto.HistoryValuesDTO;
import com.automation.professional.service.mapper.HistoryValuesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HistoryValues}.
 */
@Service
@Transactional
public class HistoryValuesServiceImpl implements HistoryValuesService {

    private final Logger log = LoggerFactory.getLogger(HistoryValuesServiceImpl.class);

    private final HistoryValuesRepository historyValuesRepository;

    private final HistoryValuesMapper historyValuesMapper;

    private final HistoryValuesSearchRepository historyValuesSearchRepository;

    public HistoryValuesServiceImpl(
        HistoryValuesRepository historyValuesRepository,
        HistoryValuesMapper historyValuesMapper,
        HistoryValuesSearchRepository historyValuesSearchRepository
    ) {
        this.historyValuesRepository = historyValuesRepository;
        this.historyValuesMapper = historyValuesMapper;
        this.historyValuesSearchRepository = historyValuesSearchRepository;
    }

    @Override
    public HistoryValuesDTO save(HistoryValuesDTO historyValuesDTO) {
        log.debug("Request to save HistoryValues : {}", historyValuesDTO);
        HistoryValues historyValues = historyValuesMapper.toEntity(historyValuesDTO);
        historyValues = historyValuesRepository.save(historyValues);
        HistoryValuesDTO result = historyValuesMapper.toDto(historyValues);
        historyValuesSearchRepository.save(historyValues);
        return result;
    }

    @Override
    public Optional<HistoryValuesDTO> partialUpdate(HistoryValuesDTO historyValuesDTO) {
        log.debug("Request to partially update HistoryValues : {}", historyValuesDTO);

        return historyValuesRepository
            .findById(historyValuesDTO.getId())
            .map(
                existingHistoryValues -> {
                    historyValuesMapper.partialUpdate(existingHistoryValues, historyValuesDTO);
                    return existingHistoryValues;
                }
            )
            .map(historyValuesRepository::save)
            .map(
                savedHistoryValues -> {
                    historyValuesSearchRepository.save(savedHistoryValues);

                    return savedHistoryValues;
                }
            )
            .map(historyValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoryValuesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HistoryValues");
        return historyValuesRepository.findAll(pageable).map(historyValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoryValuesDTO> findOne(Long id) {
        log.debug("Request to get HistoryValues : {}", id);
        return historyValuesRepository.findById(id).map(historyValuesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HistoryValues : {}", id);
        historyValuesRepository.deleteById(id);
        historyValuesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoryValuesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HistoryValues for query {}", query);
        return historyValuesSearchRepository.search(queryStringQuery(query), pageable).map(historyValuesMapper::toDto);
    }
}
