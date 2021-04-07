package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.MostOfContValues;
import com.automation.professional.repository.MostOfContValuesRepository;
import com.automation.professional.repository.search.MostOfContValuesSearchRepository;
import com.automation.professional.service.MostOfContValuesService;
import com.automation.professional.service.dto.MostOfContValuesDTO;
import com.automation.professional.service.mapper.MostOfContValuesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MostOfContValues}.
 */
@Service
@Transactional
public class MostOfContValuesServiceImpl implements MostOfContValuesService {

    private final Logger log = LoggerFactory.getLogger(MostOfContValuesServiceImpl.class);

    private final MostOfContValuesRepository mostOfContValuesRepository;

    private final MostOfContValuesMapper mostOfContValuesMapper;

    private final MostOfContValuesSearchRepository mostOfContValuesSearchRepository;

    public MostOfContValuesServiceImpl(
        MostOfContValuesRepository mostOfContValuesRepository,
        MostOfContValuesMapper mostOfContValuesMapper,
        MostOfContValuesSearchRepository mostOfContValuesSearchRepository
    ) {
        this.mostOfContValuesRepository = mostOfContValuesRepository;
        this.mostOfContValuesMapper = mostOfContValuesMapper;
        this.mostOfContValuesSearchRepository = mostOfContValuesSearchRepository;
    }

    @Override
    public MostOfContValuesDTO save(MostOfContValuesDTO mostOfContValuesDTO) {
        log.debug("Request to save MostOfContValues : {}", mostOfContValuesDTO);
        MostOfContValues mostOfContValues = mostOfContValuesMapper.toEntity(mostOfContValuesDTO);
        mostOfContValues = mostOfContValuesRepository.save(mostOfContValues);
        MostOfContValuesDTO result = mostOfContValuesMapper.toDto(mostOfContValues);
        mostOfContValuesSearchRepository.save(mostOfContValues);
        return result;
    }

    @Override
    public Optional<MostOfContValuesDTO> partialUpdate(MostOfContValuesDTO mostOfContValuesDTO) {
        log.debug("Request to partially update MostOfContValues : {}", mostOfContValuesDTO);

        return mostOfContValuesRepository
            .findById(mostOfContValuesDTO.getId())
            .map(
                existingMostOfContValues -> {
                    mostOfContValuesMapper.partialUpdate(existingMostOfContValues, mostOfContValuesDTO);
                    return existingMostOfContValues;
                }
            )
            .map(mostOfContValuesRepository::save)
            .map(
                savedMostOfContValues -> {
                    mostOfContValuesSearchRepository.save(savedMostOfContValues);

                    return savedMostOfContValues;
                }
            )
            .map(mostOfContValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MostOfContValuesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MostOfContValues");
        return mostOfContValuesRepository.findAll(pageable).map(mostOfContValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MostOfContValuesDTO> findOne(Long id) {
        log.debug("Request to get MostOfContValues : {}", id);
        return mostOfContValuesRepository.findById(id).map(mostOfContValuesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MostOfContValues : {}", id);
        mostOfContValuesRepository.deleteById(id);
        mostOfContValuesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MostOfContValuesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MostOfContValues for query {}", query);
        return mostOfContValuesSearchRepository.search(queryStringQuery(query), pageable).map(mostOfContValuesMapper::toDto);
    }
}
