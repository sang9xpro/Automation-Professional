package com.automation.professional.service;

import com.automation.professional.service.dto.FacebookFieldsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.FacebookFields}.
 */
public interface FacebookFieldsService {
    /**
     * Save a facebookFields.
     *
     * @param facebookFieldsDTO the entity to save.
     * @return the persisted entity.
     */
    FacebookFieldsDTO save(FacebookFieldsDTO facebookFieldsDTO);

    /**
     * Partially updates a facebookFields.
     *
     * @param facebookFieldsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FacebookFieldsDTO> partialUpdate(FacebookFieldsDTO facebookFieldsDTO);

    /**
     * Get all the facebookFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FacebookFieldsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" facebookFields.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FacebookFieldsDTO> findOne(Long id);

    /**
     * Delete the "id" facebookFields.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the facebookFields corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FacebookFieldsDTO> search(String query, Pageable pageable);
}
