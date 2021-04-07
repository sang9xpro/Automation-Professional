package com.automation.professional.service;

import com.automation.professional.service.dto.SchedulerTaskDeviceFieldsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.SchedulerTaskDeviceFields}.
 */
public interface SchedulerTaskDeviceFieldsService {
    /**
     * Save a schedulerTaskDeviceFields.
     *
     * @param schedulerTaskDeviceFieldsDTO the entity to save.
     * @return the persisted entity.
     */
    SchedulerTaskDeviceFieldsDTO save(SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO);

    /**
     * Partially updates a schedulerTaskDeviceFields.
     *
     * @param schedulerTaskDeviceFieldsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SchedulerTaskDeviceFieldsDTO> partialUpdate(SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO);

    /**
     * Get all the schedulerTaskDeviceFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchedulerTaskDeviceFieldsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" schedulerTaskDeviceFields.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SchedulerTaskDeviceFieldsDTO> findOne(Long id);

    /**
     * Delete the "id" schedulerTaskDeviceFields.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the schedulerTaskDeviceFields corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchedulerTaskDeviceFieldsDTO> search(String query, Pageable pageable);
}
