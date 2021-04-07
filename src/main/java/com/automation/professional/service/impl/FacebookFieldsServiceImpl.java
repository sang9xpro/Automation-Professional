package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.FacebookFields;
import com.automation.professional.repository.FacebookFieldsRepository;
import com.automation.professional.repository.search.FacebookFieldsSearchRepository;
import com.automation.professional.service.FacebookFieldsService;
import com.automation.professional.service.dto.FacebookFieldsDTO;
import com.automation.professional.service.mapper.FacebookFieldsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FacebookFields}.
 */
@Service
@Transactional
public class FacebookFieldsServiceImpl implements FacebookFieldsService {

    private final Logger log = LoggerFactory.getLogger(FacebookFieldsServiceImpl.class);

    private final FacebookFieldsRepository facebookFieldsRepository;

    private final FacebookFieldsMapper facebookFieldsMapper;

    private final FacebookFieldsSearchRepository facebookFieldsSearchRepository;

    public FacebookFieldsServiceImpl(
        FacebookFieldsRepository facebookFieldsRepository,
        FacebookFieldsMapper facebookFieldsMapper,
        FacebookFieldsSearchRepository facebookFieldsSearchRepository
    ) {
        this.facebookFieldsRepository = facebookFieldsRepository;
        this.facebookFieldsMapper = facebookFieldsMapper;
        this.facebookFieldsSearchRepository = facebookFieldsSearchRepository;
    }

    @Override
    public FacebookFieldsDTO save(FacebookFieldsDTO facebookFieldsDTO) {
        log.debug("Request to save FacebookFields : {}", facebookFieldsDTO);
        FacebookFields facebookFields = facebookFieldsMapper.toEntity(facebookFieldsDTO);
        facebookFields = facebookFieldsRepository.save(facebookFields);
        FacebookFieldsDTO result = facebookFieldsMapper.toDto(facebookFields);
        facebookFieldsSearchRepository.save(facebookFields);
        return result;
    }

    @Override
    public Optional<FacebookFieldsDTO> partialUpdate(FacebookFieldsDTO facebookFieldsDTO) {
        log.debug("Request to partially update FacebookFields : {}", facebookFieldsDTO);

        return facebookFieldsRepository
            .findById(facebookFieldsDTO.getId())
            .map(
                existingFacebookFields -> {
                    facebookFieldsMapper.partialUpdate(existingFacebookFields, facebookFieldsDTO);
                    return existingFacebookFields;
                }
            )
            .map(facebookFieldsRepository::save)
            .map(
                savedFacebookFields -> {
                    facebookFieldsSearchRepository.save(savedFacebookFields);

                    return savedFacebookFields;
                }
            )
            .map(facebookFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacebookFieldsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FacebookFields");
        return facebookFieldsRepository.findAll(pageable).map(facebookFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FacebookFieldsDTO> findOne(Long id) {
        log.debug("Request to get FacebookFields : {}", id);
        return facebookFieldsRepository.findById(id).map(facebookFieldsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FacebookFields : {}", id);
        facebookFieldsRepository.deleteById(id);
        facebookFieldsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacebookFieldsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FacebookFields for query {}", query);
        return facebookFieldsSearchRepository.search(queryStringQuery(query), pageable).map(facebookFieldsMapper::toDto);
    }
}
