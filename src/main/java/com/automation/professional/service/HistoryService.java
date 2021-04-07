package com.automation.professional.service;

import com.automation.professional.service.dto.HistoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.History}.
 */
public interface HistoryService {
    /**
     * Save a history.
     *
     * @param historyDTO the entity to save.
     * @return the persisted entity.
     */
    HistoryDTO save(HistoryDTO historyDTO);

    /**
     * Partially updates a history.
     *
     * @param historyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistoryDTO> partialUpdate(HistoryDTO historyDTO);

    /**
     * Get all the histories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" history.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoryDTO> findOne(Long id);

    /**
     * Delete the "id" history.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the history corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoryDTO> search(String query, Pageable pageable);
}
