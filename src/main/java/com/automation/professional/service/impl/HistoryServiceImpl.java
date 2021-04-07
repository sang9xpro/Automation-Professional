package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.History;
import com.automation.professional.repository.HistoryRepository;
import com.automation.professional.repository.search.HistorySearchRepository;
import com.automation.professional.service.HistoryService;
import com.automation.professional.service.dto.HistoryDTO;
import com.automation.professional.service.mapper.HistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link History}.
 */
@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {

    private final Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);

    private final HistoryRepository historyRepository;

    private final HistoryMapper historyMapper;

    private final HistorySearchRepository historySearchRepository;

    public HistoryServiceImpl(
        HistoryRepository historyRepository,
        HistoryMapper historyMapper,
        HistorySearchRepository historySearchRepository
    ) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
        this.historySearchRepository = historySearchRepository;
    }

    @Override
    public HistoryDTO save(HistoryDTO historyDTO) {
        log.debug("Request to save History : {}", historyDTO);
        History history = historyMapper.toEntity(historyDTO);
        history = historyRepository.save(history);
        HistoryDTO result = historyMapper.toDto(history);
        historySearchRepository.save(history);
        return result;
    }

    @Override
    public Optional<HistoryDTO> partialUpdate(HistoryDTO historyDTO) {
        log.debug("Request to partially update History : {}", historyDTO);

        return historyRepository
            .findById(historyDTO.getId())
            .map(
                existingHistory -> {
                    historyMapper.partialUpdate(existingHistory, historyDTO);
                    return existingHistory;
                }
            )
            .map(historyRepository::save)
            .map(
                savedHistory -> {
                    historySearchRepository.save(savedHistory);

                    return savedHistory;
                }
            )
            .map(historyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Histories");
        return historyRepository.findAll(pageable).map(historyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoryDTO> findOne(Long id) {
        log.debug("Request to get History : {}", id);
        return historyRepository.findById(id).map(historyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete History : {}", id);
        historyRepository.deleteById(id);
        historySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Histories for query {}", query);
        return historySearchRepository.search(queryStringQuery(query), pageable).map(historyMapper::toDto);
    }
}
