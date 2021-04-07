package com.automation.professional.service;

import com.automation.professional.service.dto.SchedulerTaskDeviceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.SchedulerTaskDevice}.
 */
public interface SchedulerTaskDeviceService {
    /**
     * Save a schedulerTaskDevice.
     *
     * @param schedulerTaskDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    SchedulerTaskDeviceDTO save(SchedulerTaskDeviceDTO schedulerTaskDeviceDTO);

    /**
     * Partially updates a schedulerTaskDevice.
     *
     * @param schedulerTaskDeviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SchedulerTaskDeviceDTO> partialUpdate(SchedulerTaskDeviceDTO schedulerTaskDeviceDTO);

    /**
     * Get all the schedulerTaskDevices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchedulerTaskDeviceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" schedulerTaskDevice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SchedulerTaskDeviceDTO> findOne(Long id);

    /**
     * Delete the "id" schedulerTaskDevice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the schedulerTaskDevice corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchedulerTaskDeviceDTO> search(String query, Pageable pageable);
}
