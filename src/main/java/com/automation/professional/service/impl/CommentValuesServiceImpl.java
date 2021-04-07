package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.CommentValues;
import com.automation.professional.repository.CommentValuesRepository;
import com.automation.professional.repository.search.CommentValuesSearchRepository;
import com.automation.professional.service.CommentValuesService;
import com.automation.professional.service.dto.CommentValuesDTO;
import com.automation.professional.service.mapper.CommentValuesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommentValues}.
 */
@Service
@Transactional
public class CommentValuesServiceImpl implements CommentValuesService {

    private final Logger log = LoggerFactory.getLogger(CommentValuesServiceImpl.class);

    private final CommentValuesRepository commentValuesRepository;

    private final CommentValuesMapper commentValuesMapper;

    private final CommentValuesSearchRepository commentValuesSearchRepository;

    public CommentValuesServiceImpl(
        CommentValuesRepository commentValuesRepository,
        CommentValuesMapper commentValuesMapper,
        CommentValuesSearchRepository commentValuesSearchRepository
    ) {
        this.commentValuesRepository = commentValuesRepository;
        this.commentValuesMapper = commentValuesMapper;
        this.commentValuesSearchRepository = commentValuesSearchRepository;
    }

    @Override
    public CommentValuesDTO save(CommentValuesDTO commentValuesDTO) {
        log.debug("Request to save CommentValues : {}", commentValuesDTO);
        CommentValues commentValues = commentValuesMapper.toEntity(commentValuesDTO);
        commentValues = commentValuesRepository.save(commentValues);
        CommentValuesDTO result = commentValuesMapper.toDto(commentValues);
        commentValuesSearchRepository.save(commentValues);
        return result;
    }

    @Override
    public Optional<CommentValuesDTO> partialUpdate(CommentValuesDTO commentValuesDTO) {
        log.debug("Request to partially update CommentValues : {}", commentValuesDTO);

        return commentValuesRepository
            .findById(commentValuesDTO.getId())
            .map(
                existingCommentValues -> {
                    commentValuesMapper.partialUpdate(existingCommentValues, commentValuesDTO);
                    return existingCommentValues;
                }
            )
            .map(commentValuesRepository::save)
            .map(
                savedCommentValues -> {
                    commentValuesSearchRepository.save(savedCommentValues);

                    return savedCommentValues;
                }
            )
            .map(commentValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentValuesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CommentValues");
        return commentValuesRepository.findAll(pageable).map(commentValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentValuesDTO> findOne(Long id) {
        log.debug("Request to get CommentValues : {}", id);
        return commentValuesRepository.findById(id).map(commentValuesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CommentValues : {}", id);
        commentValuesRepository.deleteById(id);
        commentValuesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentValuesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CommentValues for query {}", query);
        return commentValuesSearchRepository.search(queryStringQuery(query), pageable).map(commentValuesMapper::toDto);
    }
}
