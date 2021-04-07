package com.automation.professional.service;

import com.automation.professional.service.dto.DeviceValuesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.DeviceValues}.
 */
public interface DeviceValuesService {
    /**
     * Save a deviceValues.
     *
     * @param deviceValuesDTO the entity to save.
     * @return the persisted entity.
     */
    DeviceValuesDTO save(DeviceValuesDTO deviceValuesDTO);

    /**
     * Partially updates a deviceValues.
     *
     * @param deviceValuesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeviceValuesDTO> partialUpdate(DeviceValuesDTO deviceValuesDTO);

    /**
     * Get all the deviceValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeviceValuesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" deviceValues.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeviceValuesDTO> findOne(Long id);

    /**
     * Delete the "id" deviceValues.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the deviceValues corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeviceValuesDTO> search(String query, Pageable pageable);
}
