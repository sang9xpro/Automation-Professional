package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.MostOfContFields;
import com.automation.professional.repository.MostOfContFieldsRepository;
import com.automation.professional.repository.search.MostOfContFieldsSearchRepository;
import com.automation.professional.service.MostOfContFieldsService;
import com.automation.professional.service.dto.MostOfContFieldsDTO;
import com.automation.professional.service.mapper.MostOfContFieldsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MostOfContFields}.
 */
@Service
@Transactional
public class MostOfContFieldsServiceImpl implements MostOfContFieldsService {

    private final Logger log = LoggerFactory.getLogger(MostOfContFieldsServiceImpl.class);

    private final MostOfContFieldsRepository mostOfContFieldsRepository;

    private final MostOfContFieldsMapper mostOfContFieldsMapper;

    private final MostOfContFieldsSearchRepository mostOfContFieldsSearchRepository;

    public MostOfContFieldsServiceImpl(
        MostOfContFieldsRepository mostOfContFieldsRepository,
        MostOfContFieldsMapper mostOfContFieldsMapper,
        MostOfContFieldsSearchRepository mostOfContFieldsSearchRepository
    ) {
        this.mostOfContFieldsRepository = mostOfContFieldsRepository;
        this.mostOfContFieldsMapper = mostOfContFieldsMapper;
        this.mostOfContFieldsSearchRepository = mostOfContFieldsSearchRepository;
    }

    @Override
    public MostOfContFieldsDTO save(MostOfContFieldsDTO mostOfContFieldsDTO) {
        log.debug("Request to save MostOfContFields : {}", mostOfContFieldsDTO);
        MostOfContFields mostOfContFields = mostOfContFieldsMapper.toEntity(mostOfContFieldsDTO);
        mostOfContFields = mostOfContFieldsRepository.save(mostOfContFields);
        MostOfContFieldsDTO result = mostOfContFieldsMapper.toDto(mostOfContFields);
        mostOfContFieldsSearchRepository.save(mostOfContFields);
        return result;
    }

    @Override
    public Optional<MostOfContFieldsDTO> partialUpdate(MostOfContFieldsDTO mostOfContFieldsDTO) {
        log.debug("Request to partially update MostOfContFields : {}", mostOfContFieldsDTO);

        return mostOfContFieldsRepository
            .findById(mostOfContFieldsDTO.getId())
            .map(
                existingMostOfContFields -> {
                    mostOfContFieldsMapper.partialUpdate(existingMostOfContFields, mostOfContFieldsDTO);
                    return existingMostOfContFields;
                }
            )
            .map(mostOfContFieldsRepository::save)
            .map(
                savedMostOfContFields -> {
                    mostOfContFieldsSearchRepository.save(savedMostOfContFields);

                    return savedMostOfContFields;
                }
            )
            .map(mostOfContFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MostOfContFieldsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MostOfContFields");
        return mostOfContFieldsRepository.findAll(pageable).map(mostOfContFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MostOfContFieldsDTO> findOne(Long id) {
        log.debug("Request to get MostOfContFields : {}", id);
        return mostOfContFieldsRepository.findById(id).map(mostOfContFieldsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MostOfContFields : {}", id);
        mostOfContFieldsRepository.deleteById(id);
        mostOfContFieldsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MostOfContFieldsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MostOfContFields for query {}", query);
        return mostOfContFieldsSearchRepository.search(queryStringQuery(query), pageable).map(mostOfContFieldsMapper::toDto);
    }
}
