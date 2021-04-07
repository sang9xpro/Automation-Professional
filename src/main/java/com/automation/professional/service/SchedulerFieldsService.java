package com.automation.professional.service;

import com.automation.professional.service.dto.SchedulerFieldsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.SchedulerFields}.
 */
public interface SchedulerFieldsService {
    /**
     * Save a schedulerFields.
     *
     * @param schedulerFieldsDTO the entity to save.
     * @return the persisted entity.
     */
    SchedulerFieldsDTO save(SchedulerFieldsDTO schedulerFieldsDTO);

    /**
     * Partially updates a schedulerFields.
     *
     * @param schedulerFieldsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SchedulerFieldsDTO> partialUpdate(SchedulerFieldsDTO schedulerFieldsDTO);

    /**
     * Get all the schedulerFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchedulerFieldsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" schedulerFields.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SchedulerFieldsDTO> findOne(Long id);

    /**
     * Delete the "id" schedulerFields.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the schedulerFields corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchedulerFieldsDTO> search(String query, Pageable pageable);
}
