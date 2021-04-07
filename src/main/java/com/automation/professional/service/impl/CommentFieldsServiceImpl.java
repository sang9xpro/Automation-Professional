package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.CommentFields;
import com.automation.professional.repository.CommentFieldsRepository;
import com.automation.professional.repository.search.CommentFieldsSearchRepository;
import com.automation.professional.service.CommentFieldsService;
import com.automation.professional.service.dto.CommentFieldsDTO;
import com.automation.professional.service.mapper.CommentFieldsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommentFields}.
 */
@Service
@Transactional
public class CommentFieldsServiceImpl implements CommentFieldsService {

    private final Logger log = LoggerFactory.getLogger(CommentFieldsServiceImpl.class);

    private final CommentFieldsRepository commentFieldsRepository;

    private final CommentFieldsMapper commentFieldsMapper;

    private final CommentFieldsSearchRepository commentFieldsSearchRepository;

    public CommentFieldsServiceImpl(
        CommentFieldsRepository commentFieldsRepository,
        CommentFieldsMapper commentFieldsMapper,
        CommentFieldsSearchRepository commentFieldsSearchRepository
    ) {
        this.commentFieldsRepository = commentFieldsRepository;
        this.commentFieldsMapper = commentFieldsMapper;
        this.commentFieldsSearchRepository = commentFieldsSearchRepository;
    }

    @Override
    public CommentFieldsDTO save(CommentFieldsDTO commentFieldsDTO) {
        log.debug("Request to save CommentFields : {}", commentFieldsDTO);
        CommentFields commentFields = commentFieldsMapper.toEntity(commentFieldsDTO);
        commentFields = commentFieldsRepository.save(commentFields);
        CommentFieldsDTO result = commentFieldsMapper.toDto(commentFields);
        commentFieldsSearchRepository.save(commentFields);
        return result;
    }

    @Override
    public Optional<CommentFieldsDTO> partialUpdate(CommentFieldsDTO commentFieldsDTO) {
        log.debug("Request to partially update CommentFields : {}", commentFieldsDTO);

        return commentFieldsRepository
            .findById(commentFieldsDTO.getId())
            .map(
                existingCommentFields -> {
                    commentFieldsMapper.partialUpdate(existingCommentFields, commentFieldsDTO);
                    return existingCommentFields;
                }
            )
            .map(commentFieldsRepository::save)
            .map(
                savedCommentFields -> {
                    commentFieldsSearchRepository.save(savedCommentFields);

                    return savedCommentFields;
                }
            )
            .map(commentFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentFieldsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CommentFields");
        return commentFieldsRepository.findAll(pageable).map(commentFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentFieldsDTO> findOne(Long id) {
        log.debug("Request to get CommentFields : {}", id);
        return commentFieldsRepository.findById(id).map(commentFieldsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CommentFields : {}", id);
        commentFieldsRepository.deleteById(id);
        commentFieldsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentFieldsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CommentFields for query {}", query);
        return commentFieldsSearchRepository.search(queryStringQuery(query), pageable).map(commentFieldsMapper::toDto);
    }
}
