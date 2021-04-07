package com.automation.professional.service;

import com.automation.professional.service.dto.AccountsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.Accounts}.
 */
public interface AccountsService {
    /**
     * Save a accounts.
     *
     * @param accountsDTO the entity to save.
     * @return the persisted entity.
     */
    AccountsDTO save(AccountsDTO accountsDTO);

    /**
     * Partially updates a accounts.
     *
     * @param accountsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountsDTO> partialUpdate(AccountsDTO accountsDTO);

    /**
     * Get all the accounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" accounts.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountsDTO> findOne(Long id);

    /**
     * Delete the "id" accounts.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the accounts corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountsDTO> search(String query, Pageable pageable);
}
