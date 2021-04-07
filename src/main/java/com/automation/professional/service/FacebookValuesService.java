package com.automation.professional.service;

import com.automation.professional.service.dto.FacebookValuesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.FacebookValues}.
 */
public interface FacebookValuesService {
    /**
     * Save a facebookValues.
     *
     * @param facebookValuesDTO the entity to save.
     * @return the persisted entity.
     */
    FacebookValuesDTO save(FacebookValuesDTO facebookValuesDTO);

    /**
     * Partially updates a facebookValues.
     *
     * @param facebookValuesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FacebookValuesDTO> partialUpdate(FacebookValuesDTO facebookValuesDTO);

    /**
     * Get all the facebookValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FacebookValuesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" facebookValues.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FacebookValuesDTO> findOne(Long id);

    /**
     * Delete the "id" facebookValues.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the facebookValues corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FacebookValuesDTO> search(String query, Pageable pageable);
}
