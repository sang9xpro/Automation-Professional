package com.automation.professional.service;

import com.automation.professional.service.dto.DevicesFieldsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.DevicesFields}.
 */
public interface DevicesFieldsService {
    /**
     * Save a devicesFields.
     *
     * @param devicesFieldsDTO the entity to save.
     * @return the persisted entity.
     */
    DevicesFieldsDTO save(DevicesFieldsDTO devicesFieldsDTO);

    /**
     * Partially updates a devicesFields.
     *
     * @param devicesFieldsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DevicesFieldsDTO> partialUpdate(DevicesFieldsDTO devicesFieldsDTO);

    /**
     * Get all the devicesFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DevicesFieldsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" devicesFields.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DevicesFieldsDTO> findOne(Long id);

    /**
     * Delete the "id" devicesFields.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the devicesFields corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DevicesFieldsDTO> search(String query, Pageable pageable);
}
