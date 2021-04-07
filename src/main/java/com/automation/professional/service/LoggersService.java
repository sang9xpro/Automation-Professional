package com.automation.professional.service;

import com.automation.professional.service.dto.LoggersDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.Loggers}.
 */
public interface LoggersService {
    /**
     * Save a loggers.
     *
     * @param loggersDTO the entity to save.
     * @return the persisted entity.
     */
    LoggersDTO save(LoggersDTO loggersDTO);

    /**
     * Partially updates a loggers.
     *
     * @param loggersDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoggersDTO> partialUpdate(LoggersDTO loggersDTO);

    /**
     * Get all the loggers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoggersDTO> findAll(Pageable pageable);

    /**
     * Get the "id" loggers.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoggersDTO> findOne(Long id);

    /**
     * Delete the "id" loggers.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the loggers corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoggersDTO> search(String query, Pageable pageable);
}
