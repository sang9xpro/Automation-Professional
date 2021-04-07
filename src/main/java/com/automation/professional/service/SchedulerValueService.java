package com.automation.professional.service;

import com.automation.professional.service.dto.SchedulerValueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.SchedulerValue}.
 */
public interface SchedulerValueService {
    /**
     * Save a schedulerValue.
     *
     * @param schedulerValueDTO the entity to save.
     * @return the persisted entity.
     */
    SchedulerValueDTO save(SchedulerValueDTO schedulerValueDTO);

    /**
     * Partially updates a schedulerValue.
     *
     * @param schedulerValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SchedulerValueDTO> partialUpdate(SchedulerValueDTO schedulerValueDTO);

    /**
     * Get all the schedulerValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchedulerValueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" schedulerValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SchedulerValueDTO> findOne(Long id);

    /**
     * Delete the "id" schedulerValue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the schedulerValue corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchedulerValueDTO> search(String query, Pageable pageable);
}
