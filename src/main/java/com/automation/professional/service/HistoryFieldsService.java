package com.automation.professional.service;

import com.automation.professional.service.dto.HistoryFieldsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.HistoryFields}.
 */
public interface HistoryFieldsService {
    /**
     * Save a historyFields.
     *
     * @param historyFieldsDTO the entity to save.
     * @return the persisted entity.
     */
    HistoryFieldsDTO save(HistoryFieldsDTO historyFieldsDTO);

    /**
     * Partially updates a historyFields.
     *
     * @param historyFieldsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistoryFieldsDTO> partialUpdate(HistoryFieldsDTO historyFieldsDTO);

    /**
     * Get all the historyFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoryFieldsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" historyFields.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoryFieldsDTO> findOne(Long id);

    /**
     * Delete the "id" historyFields.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the historyFields corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoryFieldsDTO> search(String query, Pageable pageable);
}
