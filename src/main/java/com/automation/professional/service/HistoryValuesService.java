package com.automation.professional.service;

import com.automation.professional.service.dto.HistoryValuesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.HistoryValues}.
 */
public interface HistoryValuesService {
    /**
     * Save a historyValues.
     *
     * @param historyValuesDTO the entity to save.
     * @return the persisted entity.
     */
    HistoryValuesDTO save(HistoryValuesDTO historyValuesDTO);

    /**
     * Partially updates a historyValues.
     *
     * @param historyValuesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistoryValuesDTO> partialUpdate(HistoryValuesDTO historyValuesDTO);

    /**
     * Get all the historyValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoryValuesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" historyValues.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoryValuesDTO> findOne(Long id);

    /**
     * Delete the "id" historyValues.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the historyValues corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoryValuesDTO> search(String query, Pageable pageable);
}
