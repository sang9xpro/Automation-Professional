package com.automation.professional.service;

import com.automation.professional.service.dto.SchedulersDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.Schedulers}.
 */
public interface SchedulersService {
    /**
     * Save a schedulers.
     *
     * @param schedulersDTO the entity to save.
     * @return the persisted entity.
     */
    SchedulersDTO save(SchedulersDTO schedulersDTO);

    /**
     * Partially updates a schedulers.
     *
     * @param schedulersDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SchedulersDTO> partialUpdate(SchedulersDTO schedulersDTO);

    /**
     * Get all the schedulers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchedulersDTO> findAll(Pageable pageable);

    /**
     * Get the "id" schedulers.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SchedulersDTO> findOne(Long id);

    /**
     * Delete the "id" schedulers.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the schedulers corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchedulersDTO> search(String query, Pageable pageable);
}
