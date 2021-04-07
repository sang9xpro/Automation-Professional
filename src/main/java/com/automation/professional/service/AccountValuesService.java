package com.automation.professional.service;

import com.automation.professional.service.dto.AccountValuesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.AccountValues}.
 */
public interface AccountValuesService {
    /**
     * Save a accountValues.
     *
     * @param accountValuesDTO the entity to save.
     * @return the persisted entity.
     */
    AccountValuesDTO save(AccountValuesDTO accountValuesDTO);

    /**
     * Partially updates a accountValues.
     *
     * @param accountValuesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountValuesDTO> partialUpdate(AccountValuesDTO accountValuesDTO);

    /**
     * Get all the accountValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountValuesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" accountValues.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountValuesDTO> findOne(Long id);

    /**
     * Delete the "id" accountValues.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the accountValues corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountValuesDTO> search(String query, Pageable pageable);
}
