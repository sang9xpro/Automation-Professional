package com.automation.professional.service;

import com.automation.professional.service.dto.CountryDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.Country}.
 */
public interface CountryService {
    /**
     * Save a country.
     *
     * @param countryDTO the entity to save.
     * @return the persisted entity.
     */
    CountryDTO save(CountryDTO countryDTO);

    /**
     * Partially updates a country.
     *
     * @param countryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CountryDTO> partialUpdate(CountryDTO countryDTO);

    /**
     * Get all the countries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CountryDTO> findAll(Pageable pageable);
    /**
     * Get all the CountryDTO where Devices is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CountryDTO> findAllWhereDevicesIsNull();
    /**
     * Get all the CountryDTO where Accounts is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CountryDTO> findAllWhereAccountsIsNull();
    /**
     * Get all the CountryDTO where Facebook is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CountryDTO> findAllWhereFacebookIsNull();

    /**
     * Get the "id" country.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CountryDTO> findOne(Long id);

    /**
     * Delete the "id" country.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the country corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CountryDTO> search(String query, Pageable pageable);
}
