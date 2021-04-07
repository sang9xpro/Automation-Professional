package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.HistoryFields;
import com.automation.professional.repository.HistoryFieldsRepository;
import com.automation.professional.repository.search.HistoryFieldsSearchRepository;
import com.automation.professional.service.HistoryFieldsService;
import com.automation.professional.service.dto.HistoryFieldsDTO;
import com.automation.professional.service.mapper.HistoryFieldsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HistoryFields}.
 */
@Service
@Transactional
public class HistoryFieldsServiceImpl implements HistoryFieldsService {

    private final Logger log = LoggerFactory.getLogger(HistoryFieldsServiceImpl.class);

    private final HistoryFieldsRepository historyFieldsRepository;

    private final HistoryFieldsMapper historyFieldsMapper;

    private final HistoryFieldsSearchRepository historyFieldsSearchRepository;

    public HistoryFieldsServiceImpl(
        HistoryFieldsRepository historyFieldsRepository,
        HistoryFieldsMapper historyFieldsMapper,
        HistoryFieldsSearchRepository historyFieldsSearchRepository
    ) {
        this.historyFieldsRepository = historyFieldsRepository;
        this.historyFieldsMapper = historyFieldsMapper;
        this.historyFieldsSearchRepository = historyFieldsSearchRepository;
    }

    @Override
    public HistoryFieldsDTO save(HistoryFieldsDTO historyFieldsDTO) {
        log.debug("Request to save HistoryFields : {}", historyFieldsDTO);
        HistoryFields historyFields = historyFieldsMapper.toEntity(historyFieldsDTO);
        historyFields = historyFieldsRepository.save(historyFields);
        HistoryFieldsDTO result = historyFieldsMapper.toDto(historyFields);
        historyFieldsSearchRepository.save(historyFields);
        return result;
    }

    @Override
    public Optional<HistoryFieldsDTO> partialUpdate(HistoryFieldsDTO historyFieldsDTO) {
        log.debug("Request to partially update HistoryFields : {}", historyFieldsDTO);

        return historyFieldsRepository
            .findById(historyFieldsDTO.getId())
            .map(
                existingHistoryFields -> {
                    historyFieldsMapper.partialUpdate(existingHistoryFields, historyFieldsDTO);
                    return existingHistoryFields;
                }
            )
            .map(historyFieldsRepository::save)
            .map(
                savedHistoryFields -> {
                    historyFieldsSearchRepository.save(savedHistoryFields);

                    return savedHistoryFields;
                }
            )
            .map(historyFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoryFieldsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HistoryFields");
        return historyFieldsRepository.findAll(pageable).map(historyFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoryFieldsDTO> findOne(Long id) {
        log.debug("Request to get HistoryFields : {}", id);
        return historyFieldsRepository.findById(id).map(historyFieldsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HistoryFields : {}", id);
        historyFieldsRepository.deleteById(id);
        historyFieldsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoryFieldsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HistoryFields for query {}", query);
        return historyFieldsSearchRepository.search(queryStringQuery(query), pageable).map(historyFieldsMapper::toDto);
    }
}
