package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.FacebookValues;
import com.automation.professional.repository.FacebookValuesRepository;
import com.automation.professional.repository.search.FacebookValuesSearchRepository;
import com.automation.professional.service.FacebookValuesService;
import com.automation.professional.service.dto.FacebookValuesDTO;
import com.automation.professional.service.mapper.FacebookValuesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FacebookValues}.
 */
@Service
@Transactional
public class FacebookValuesServiceImpl implements FacebookValuesService {

    private final Logger log = LoggerFactory.getLogger(FacebookValuesServiceImpl.class);

    private final FacebookValuesRepository facebookValuesRepository;

    private final FacebookValuesMapper facebookValuesMapper;

    private final FacebookValuesSearchRepository facebookValuesSearchRepository;

    public FacebookValuesServiceImpl(
        FacebookValuesRepository facebookValuesRepository,
        FacebookValuesMapper facebookValuesMapper,
        FacebookValuesSearchRepository facebookValuesSearchRepository
    ) {
        this.facebookValuesRepository = facebookValuesRepository;
        this.facebookValuesMapper = facebookValuesMapper;
        this.facebookValuesSearchRepository = facebookValuesSearchRepository;
    }

    @Override
    public FacebookValuesDTO save(FacebookValuesDTO facebookValuesDTO) {
        log.debug("Request to save FacebookValues : {}", facebookValuesDTO);
        FacebookValues facebookValues = facebookValuesMapper.toEntity(facebookValuesDTO);
        facebookValues = facebookValuesRepository.save(facebookValues);
        FacebookValuesDTO result = facebookValuesMapper.toDto(facebookValues);
        facebookValuesSearchRepository.save(facebookValues);
        return result;
    }

    @Override
    public Optional<FacebookValuesDTO> partialUpdate(FacebookValuesDTO facebookValuesDTO) {
        log.debug("Request to partially update FacebookValues : {}", facebookValuesDTO);

        return facebookValuesRepository
            .findById(facebookValuesDTO.getId())
            .map(
                existingFacebookValues -> {
                    facebookValuesMapper.partialUpdate(existingFacebookValues, facebookValuesDTO);
                    return existingFacebookValues;
                }
            )
            .map(facebookValuesRepository::save)
            .map(
                savedFacebookValues -> {
                    facebookValuesSearchRepository.save(savedFacebookValues);

                    return savedFacebookValues;
                }
            )
            .map(facebookValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacebookValuesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FacebookValues");
        return facebookValuesRepository.findAll(pageable).map(facebookValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FacebookValuesDTO> findOne(Long id) {
        log.debug("Request to get FacebookValues : {}", id);
        return facebookValuesRepository.findById(id).map(facebookValuesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FacebookValues : {}", id);
        facebookValuesRepository.deleteById(id);
        facebookValuesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacebookValuesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FacebookValues for query {}", query);
        return facebookValuesSearchRepository.search(queryStringQuery(query), pageable).map(facebookValuesMapper::toDto);
    }
}
