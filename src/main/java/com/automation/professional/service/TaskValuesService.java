package com.automation.professional.service;

import com.automation.professional.service.dto.TaskValuesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.TaskValues}.
 */
public interface TaskValuesService {
    /**
     * Save a taskValues.
     *
     * @param taskValuesDTO the entity to save.
     * @return the persisted entity.
     */
    TaskValuesDTO save(TaskValuesDTO taskValuesDTO);

    /**
     * Partially updates a taskValues.
     *
     * @param taskValuesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaskValuesDTO> partialUpdate(TaskValuesDTO taskValuesDTO);

    /**
     * Get all the taskValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskValuesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" taskValues.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskValuesDTO> findOne(Long id);

    /**
     * Delete the "id" taskValues.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the taskValues corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskValuesDTO> search(String query, Pageable pageable);
}
