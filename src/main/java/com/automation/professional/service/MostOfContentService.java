package com.automation.professional.service;

import com.automation.professional.service.dto.MostOfContentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.automation.professional.domain.MostOfContent}.
 */
public interface MostOfContentService {
    /**
     * Save a mostOfContent.
     *
     * @param mostOfContentDTO the entity to save.
     * @return the persisted entity.
     */
    MostOfContentDTO save(MostOfContentDTO mostOfContentDTO);

    /**
     * Partially updates a mostOfContent.
     *
     * @param mostOfContentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MostOfContentDTO> partialUpdate(MostOfContentDTO mostOfContentDTO);

    /**
     * Get all the mostOfContents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MostOfContentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mostOfContent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MostOfContentDTO> findOne(Long id);

    /**
     * Delete the "id" mostOfContent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the mostOfContent corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MostOfContentDTO> search(String query, Pageable pageable);
}
