package com.automation.professional.service;

import com.automation.professional.service.dto.LoggersValuesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.LoggersValues}.
 */
public interface LoggersValuesService {
    /**
     * Save a loggersValues.
     *
     * @param loggersValuesDTO the entity to save.
     * @return the persisted entity.
     */
    LoggersValuesDTO save(LoggersValuesDTO loggersValuesDTO);

    /**
     * Partially updates a loggersValues.
     *
     * @param loggersValuesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoggersValuesDTO> partialUpdate(LoggersValuesDTO loggersValuesDTO);

    /**
     * Get all the loggersValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoggersValuesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" loggersValues.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoggersValuesDTO> findOne(Long id);

    /**
     * Delete the "id" loggersValues.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the loggersValues corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoggersValuesDTO> search(String query, Pageable pageable);
}
