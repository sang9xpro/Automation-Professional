package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.TaskFields;
import com.automation.professional.repository.TaskFieldsRepository;
import com.automation.professional.repository.search.TaskFieldsSearchRepository;
import com.automation.professional.service.TaskFieldsService;
import com.automation.professional.service.dto.TaskFieldsDTO;
import com.automation.professional.service.mapper.TaskFieldsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TaskFields}.
 */
@Service
@Transactional
public class TaskFieldsServiceImpl implements TaskFieldsService {

    private final Logger log = LoggerFactory.getLogger(TaskFieldsServiceImpl.class);

    private final TaskFieldsRepository taskFieldsRepository;

    private final TaskFieldsMapper taskFieldsMapper;

    private final TaskFieldsSearchRepository taskFieldsSearchRepository;

    public TaskFieldsServiceImpl(
        TaskFieldsRepository taskFieldsRepository,
        TaskFieldsMapper taskFieldsMapper,
        TaskFieldsSearchRepository taskFieldsSearchRepository
    ) {
        this.taskFieldsRepository = taskFieldsRepository;
        this.taskFieldsMapper = taskFieldsMapper;
        this.taskFieldsSearchRepository = taskFieldsSearchRepository;
    }

    @Override
    public TaskFieldsDTO save(TaskFieldsDTO taskFieldsDTO) {
        log.debug("Request to save TaskFields : {}", taskFieldsDTO);
        TaskFields taskFields = taskFieldsMapper.toEntity(taskFieldsDTO);
        taskFields = taskFieldsRepository.save(taskFields);
        TaskFieldsDTO result = taskFieldsMapper.toDto(taskFields);
        taskFieldsSearchRepository.save(taskFields);
        return result;
    }

    @Override
    public Optional<TaskFieldsDTO> partialUpdate(TaskFieldsDTO taskFieldsDTO) {
        log.debug("Request to partially update TaskFields : {}", taskFieldsDTO);

        return taskFieldsRepository
            .findById(taskFieldsDTO.getId())
            .map(
                existingTaskFields -> {
                    taskFieldsMapper.partialUpdate(existingTaskFields, taskFieldsDTO);
                    return existingTaskFields;
                }
            )
            .map(taskFieldsRepository::save)
            .map(
                savedTaskFields -> {
                    taskFieldsSearchRepository.save(savedTaskFields);

                    return savedTaskFields;
                }
            )
            .map(taskFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskFieldsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaskFields");
        return taskFieldsRepository.findAll(pageable).map(taskFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskFieldsDTO> findOne(Long id) {
        log.debug("Request to get TaskFields : {}", id);
        return taskFieldsRepository.findById(id).map(taskFieldsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaskFields : {}", id);
        taskFieldsRepository.deleteById(id);
        taskFieldsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskFieldsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TaskFields for query {}", query);
        return taskFieldsSearchRepository.search(queryStringQuery(query), pageable).map(taskFieldsMapper::toDto);
    }
}
