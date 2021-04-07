package com.automation.professional.service;

import com.automation.professional.service.dto.SchedulerTaskDeviceValuesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.SchedulerTaskDeviceValues}.
 */
public interface SchedulerTaskDeviceValuesService {
    /**
     * Save a schedulerTaskDeviceValues.
     *
     * @param schedulerTaskDeviceValuesDTO the entity to save.
     * @return the persisted entity.
     */
    SchedulerTaskDeviceValuesDTO save(SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO);

    /**
     * Partially updates a schedulerTaskDeviceValues.
     *
     * @param schedulerTaskDeviceValuesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SchedulerTaskDeviceValuesDTO> partialUpdate(SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO);

    /**
     * Get all the schedulerTaskDeviceValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchedulerTaskDeviceValuesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" schedulerTaskDeviceValues.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SchedulerTaskDeviceValuesDTO> findOne(Long id);

    /**
     * Delete the "id" schedulerTaskDeviceValues.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the schedulerTaskDeviceValues corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchedulerTaskDeviceValuesDTO> search(String query, Pageable pageable);
}
