package com.automation.professional.service;

import com.automation.professional.service.dto.MostOfContValuesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.MostOfContValues}.
 */
public interface MostOfContValuesService {
    /**
     * Save a mostOfContValues.
     *
     * @param mostOfContValuesDTO the entity to save.
     * @return the persisted entity.
     */
    MostOfContValuesDTO save(MostOfContValuesDTO mostOfContValuesDTO);

    /**
     * Partially updates a mostOfContValues.
     *
     * @param mostOfContValuesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MostOfContValuesDTO> partialUpdate(MostOfContValuesDTO mostOfContValuesDTO);

    /**
     * Get all the mostOfContValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MostOfContValuesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mostOfContValues.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MostOfContValuesDTO> findOne(Long id);

    /**
     * Delete the "id" mostOfContValues.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the mostOfContValues corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MostOfContValuesDTO> search(String query, Pageable pageable);
}
