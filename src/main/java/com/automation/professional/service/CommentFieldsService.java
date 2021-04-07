package com.automation.professional.service;

import com.automation.professional.service.dto.CommentFieldsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.CommentFields}.
 */
public interface CommentFieldsService {
    /**
     * Save a commentFields.
     *
     * @param commentFieldsDTO the entity to save.
     * @return the persisted entity.
     */
    CommentFieldsDTO save(CommentFieldsDTO commentFieldsDTO);

    /**
     * Partially updates a commentFields.
     *
     * @param commentFieldsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommentFieldsDTO> partialUpdate(CommentFieldsDTO commentFieldsDTO);

    /**
     * Get all the commentFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentFieldsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" commentFields.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommentFieldsDTO> findOne(Long id);

    /**
     * Delete the "id" commentFields.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the commentFields corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentFieldsDTO> search(String query, Pageable pageable);
}
