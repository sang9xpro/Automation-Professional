package com.automation.professional.service;

import com.automation.professional.service.dto.AccountFieldsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.AccountFields}.
 */
public interface AccountFieldsService {
    /**
     * Save a accountFields.
     *
     * @param accountFieldsDTO the entity to save.
     * @return the persisted entity.
     */
    AccountFieldsDTO save(AccountFieldsDTO accountFieldsDTO);

    /**
     * Partially updates a accountFields.
     *
     * @param accountFieldsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountFieldsDTO> partialUpdate(AccountFieldsDTO accountFieldsDTO);

    /**
     * Get all the accountFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountFieldsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" accountFields.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountFieldsDTO> findOne(Long id);

    /**
     * Delete the "id" accountFields.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the accountFields corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountFieldsDTO> search(String query, Pageable pageable);
}
