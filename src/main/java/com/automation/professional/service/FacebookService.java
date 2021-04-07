package com.automation.professional.service;

import com.automation.professional.service.dto.FacebookDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.Facebook}.
 */
public interface FacebookService {
    /**
     * Save a facebook.
     *
     * @param facebookDTO the entity to save.
     * @return the persisted entity.
     */
    FacebookDTO save(FacebookDTO facebookDTO);

    /**
     * Partially updates a facebook.
     *
     * @param facebookDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FacebookDTO> partialUpdate(FacebookDTO facebookDTO);

    /**
     * Get all the facebooks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FacebookDTO> findAll(Pageable pageable);

    /**
     * Get the "id" facebook.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FacebookDTO> findOne(Long id);

    /**
     * Delete the "id" facebook.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the facebook corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FacebookDTO> search(String query, Pageable pageable);
}
