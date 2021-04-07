package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.TaskValues;
import com.automation.professional.repository.TaskValuesRepository;
import com.automation.professional.repository.search.TaskValuesSearchRepository;
import com.automation.professional.service.TaskValuesService;
import com.automation.professional.service.dto.TaskValuesDTO;
import com.automation.professional.service.mapper.TaskValuesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TaskValues}.
 */
@Service
@Transactional
public class TaskValuesServiceImpl implements TaskValuesService {

    private final Logger log = LoggerFactory.getLogger(TaskValuesServiceImpl.class);

    private final TaskValuesRepository taskValuesRepository;

    private final TaskValuesMapper taskValuesMapper;

    private final TaskValuesSearchRepository taskValuesSearchRepository;

    public TaskValuesServiceImpl(
        TaskValuesRepository taskValuesRepository,
        TaskValuesMapper taskValuesMapper,
        TaskValuesSearchRepository taskValuesSearchRepository
    ) {
        this.taskValuesRepository = taskValuesRepository;
        this.taskValuesMapper = taskValuesMapper;
        this.taskValuesSearchRepository = taskValuesSearchRepository;
    }

    @Override
    public TaskValuesDTO save(TaskValuesDTO taskValuesDTO) {
        log.debug("Request to save TaskValues : {}", taskValuesDTO);
        TaskValues taskValues = taskValuesMapper.toEntity(taskValuesDTO);
        taskValues = taskValuesRepository.save(taskValues);
        TaskValuesDTO result = taskValuesMapper.toDto(taskValues);
        taskValuesSearchRepository.save(taskValues);
        return result;
    }

    @Override
    public Optional<TaskValuesDTO> partialUpdate(TaskValuesDTO taskValuesDTO) {
        log.debug("Request to partially update TaskValues : {}", taskValuesDTO);

        return taskValuesRepository
            .findById(taskValuesDTO.getId())
            .map(
                existingTaskValues -> {
                    taskValuesMapper.partialUpdate(existingTaskValues, taskValuesDTO);
                    return existingTaskValues;
                }
            )
            .map(taskValuesRepository::save)
            .map(
                savedTaskValues -> {
                    taskValuesSearchRepository.save(savedTaskValues);

                    return savedTaskValues;
                }
            )
            .map(taskValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskValuesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaskValues");
        return taskValuesRepository.findAll(pageable).map(taskValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskValuesDTO> findOne(Long id) {
        log.debug("Request to get TaskValues : {}", id);
        return taskValuesRepository.findById(id).map(taskValuesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaskValues : {}", id);
        taskValuesRepository.deleteById(id);
        taskValuesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskValuesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TaskValues for query {}", query);
        return taskValuesSearchRepository.search(queryStringQuery(query), pageable).map(taskValuesMapper::toDto);
    }
}
