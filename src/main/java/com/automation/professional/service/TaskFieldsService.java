package com.automation.professional.service;

import com.automation.professional.service.dto.TaskFieldsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.TaskFields}.
 */
public interface TaskFieldsService {
    /**
     * Save a taskFields.
     *
     * @param taskFieldsDTO the entity to save.
     * @return the persisted entity.
     */
    TaskFieldsDTO save(TaskFieldsDTO taskFieldsDTO);

    /**
     * Partially updates a taskFields.
     *
     * @param taskFieldsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaskFieldsDTO> partialUpdate(TaskFieldsDTO taskFieldsDTO);

    /**
     * Get all the taskFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskFieldsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" taskFields.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskFieldsDTO> findOne(Long id);

    /**
     * Delete the "id" taskFields.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the taskFields corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskFieldsDTO> search(String query, Pageable pageable);
}
