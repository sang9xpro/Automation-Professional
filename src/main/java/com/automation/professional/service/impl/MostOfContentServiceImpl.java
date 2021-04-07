package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.MostOfContent;
import com.automation.professional.repository.MostOfContentRepository;
import com.automation.professional.repository.search.MostOfContentSearchRepository;
import com.automation.professional.service.MostOfContentService;
import com.automation.professional.service.dto.MostOfContentDTO;
import com.automation.professional.service.mapper.MostOfContentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MostOfContent}.
 */
@Service
@Transactional
public class MostOfContentServiceImpl implements MostOfContentService {

    private final Logger log = LoggerFactory.getLogger(MostOfContentServiceImpl.class);

    private final MostOfContentRepository mostOfContentRepository;

    private final MostOfContentMapper mostOfContentMapper;

    private final MostOfContentSearchRepository mostOfContentSearchRepository;

    public MostOfContentServiceImpl(
        MostOfContentRepository mostOfContentRepository,
        MostOfContentMapper mostOfContentMapper,
        MostOfContentSearchRepository mostOfContentSearchRepository
    ) {
        this.mostOfContentRepository = mostOfContentRepository;
        this.mostOfContentMapper = mostOfContentMapper;
        this.mostOfContentSearchRepository = mostOfContentSearchRepository;
    }

    @Override
    public MostOfContentDTO save(MostOfContentDTO mostOfContentDTO) {
        log.debug("Request to save MostOfContent : {}", mostOfContentDTO);
        MostOfContent mostOfContent = mostOfContentMapper.toEntity(mostOfContentDTO);
        mostOfContent = mostOfContentRepository.save(mostOfContent);
        MostOfContentDTO result = mostOfContentMapper.toDto(mostOfContent);
        mostOfContentSearchRepository.save(mostOfContent);
        return result;
    }

    @Override
    public Optional<MostOfContentDTO> partialUpdate(MostOfContentDTO mostOfContentDTO) {
        log.debug("Request to partially update MostOfContent : {}", mostOfContentDTO);

        return mostOfContentRepository
            .findById(mostOfContentDTO.getId())
            .map(
                existingMostOfContent -> {
                    mostOfContentMapper.partialUpdate(existingMostOfContent, mostOfContentDTO);
                    return existingMostOfContent;
                }
            )
            .map(mostOfContentRepository::save)
            .map(
                savedMostOfContent -> {
                    mostOfContentSearchRepository.save(savedMostOfContent);

                    return savedMostOfContent;
                }
            )
            .map(mostOfContentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MostOfContentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MostOfContents");
        return mostOfContentRepository.findAll(pageable).map(mostOfContentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MostOfContentDTO> findOne(Long id) {
        log.debug("Request to get MostOfContent : {}", id);
        return mostOfContentRepository.findById(id).map(mostOfContentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MostOfContent : {}", id);
        mostOfContentRepository.deleteById(id);
        mostOfContentSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MostOfContentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MostOfContents for query {}", query);
        return mostOfContentSearchRepository.search(queryStringQuery(query), pageable).map(mostOfContentMapper::toDto);
    }
}
