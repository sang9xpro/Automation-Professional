package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.Facebook;
import com.automation.professional.repository.FacebookRepository;
import com.automation.professional.repository.search.FacebookSearchRepository;
import com.automation.professional.service.FacebookService;
import com.automation.professional.service.dto.FacebookDTO;
import com.automation.professional.service.mapper.FacebookMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Facebook}.
 */
@Service
@Transactional
public class FacebookServiceImpl implements FacebookService {

    private final Logger log = LoggerFactory.getLogger(FacebookServiceImpl.class);

    private final FacebookRepository facebookRepository;

    private final FacebookMapper facebookMapper;

    private final FacebookSearchRepository facebookSearchRepository;

    public FacebookServiceImpl(
        FacebookRepository facebookRepository,
        FacebookMapper facebookMapper,
        FacebookSearchRepository facebookSearchRepository
    ) {
        this.facebookRepository = facebookRepository;
        this.facebookMapper = facebookMapper;
        this.facebookSearchRepository = facebookSearchRepository;
    }

    @Override
    public FacebookDTO save(FacebookDTO facebookDTO) {
        log.debug("Request to save Facebook : {}", facebookDTO);
        Facebook facebook = facebookMapper.toEntity(facebookDTO);
        facebook = facebookRepository.save(facebook);
        FacebookDTO result = facebookMapper.toDto(facebook);
        facebookSearchRepository.save(facebook);
        return result;
    }

    @Override
    public Optional<FacebookDTO> partialUpdate(FacebookDTO facebookDTO) {
        log.debug("Request to partially update Facebook : {}", facebookDTO);

        return facebookRepository
            .findById(facebookDTO.getId())
            .map(
                existingFacebook -> {
                    facebookMapper.partialUpdate(existingFacebook, facebookDTO);
                    return existingFacebook;
                }
            )
            .map(facebookRepository::save)
            .map(
                savedFacebook -> {
                    facebookSearchRepository.save(savedFacebook);

                    return savedFacebook;
                }
            )
            .map(facebookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacebookDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Facebooks");
        return facebookRepository.findAll(pageable).map(facebookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FacebookDTO> findOne(Long id) {
        log.debug("Request to get Facebook : {}", id);
        return facebookRepository.findById(id).map(facebookMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Facebook : {}", id);
        facebookRepository.deleteById(id);
        facebookSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacebookDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Facebooks for query {}", query);
        return facebookSearchRepository.search(queryStringQuery(query), pageable).map(facebookMapper::toDto);
    }
}
