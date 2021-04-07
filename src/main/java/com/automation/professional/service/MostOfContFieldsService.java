package com.automation.professional.service;

import com.automation.professional.service.dto.MostOfContFieldsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.MostOfContFields}.
 */
public interface MostOfContFieldsService {
    /**
     * Save a mostOfContFields.
     *
     * @param mostOfContFieldsDTO the entity to save.
     * @return the persisted entity.
     */
    MostOfContFieldsDTO save(MostOfContFieldsDTO mostOfContFieldsDTO);

    /**
     * Partially updates a mostOfContFields.
     *
     * @param mostOfContFieldsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MostOfContFieldsDTO> partialUpdate(MostOfContFieldsDTO mostOfContFieldsDTO);

    /**
     * Get all the mostOfContFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MostOfContFieldsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mostOfContFields.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MostOfContFieldsDTO> findOne(Long id);

    /**
     * Delete the "id" mostOfContFields.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the mostOfContFields corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MostOfContFieldsDTO> search(String query, Pageable pageable);
}
