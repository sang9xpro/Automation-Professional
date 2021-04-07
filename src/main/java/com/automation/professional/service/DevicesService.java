package com.automation.professional.service;

import com.automation.professional.service.dto.DevicesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.Devices}.
 */
public interface DevicesService {
    /**
     * Save a devices.
     *
     * @param devicesDTO the entity to save.
     * @return the persisted entity.
     */
    DevicesDTO save(DevicesDTO devicesDTO);

    /**
     * Partially updates a devices.
     *
     * @param devicesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DevicesDTO> partialUpdate(DevicesDTO devicesDTO);

    /**
     * Get all the devices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DevicesDTO> findAll(Pageable pageable);

    /**
     * Get all the devices with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DevicesDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" devices.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DevicesDTO> findOne(Long id);

    /**
     * Delete the "id" devices.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the devices corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DevicesDTO> search(String query, Pageable pageable);
}
