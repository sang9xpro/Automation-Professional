package com.automation.professional.service;

import com.automation.professional.service.dto.LoggersFieldsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.LoggersFields}.
 */
public interface LoggersFieldsService {
    /**
     * Save a loggersFields.
     *
     * @param loggersFieldsDTO the entity to save.
     * @return the persisted entity.
     */
    LoggersFieldsDTO save(LoggersFieldsDTO loggersFieldsDTO);

    /**
     * Partially updates a loggersFields.
     *
     * @param loggersFieldsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoggersFieldsDTO> partialUpdate(LoggersFieldsDTO loggersFieldsDTO);

    /**
     * Get all the loggersFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoggersFieldsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" loggersFields.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoggersFieldsDTO> findOne(Long id);

    /**
     * Delete the "id" loggersFields.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the loggersFields corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoggersFieldsDTO> search(String query, Pageable pageable);
}
