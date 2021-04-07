package com.automation.professional.service;

import com.automation.professional.service.dto.CommentValuesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.CommentValues}.
 */
public interface CommentValuesService {
    /**
     * Save a commentValues.
     *
     * @param commentValuesDTO the entity to save.
     * @return the persisted entity.
     */
    CommentValuesDTO save(CommentValuesDTO commentValuesDTO);

    /**
     * Partially updates a commentValues.
     *
     * @param commentValuesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommentValuesDTO> partialUpdate(CommentValuesDTO commentValuesDTO);

    /**
     * Get all the commentValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentValuesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" commentValues.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommentValuesDTO> findOne(Long id);

    /**
     * Delete the "id" commentValues.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the commentValues corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentValuesDTO> search(String query, Pageable pageable);
}
