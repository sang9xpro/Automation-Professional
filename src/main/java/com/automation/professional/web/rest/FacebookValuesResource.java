package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.FacebookValuesRepository;
import com.automation.professional.service.FacebookValuesService;
import com.automation.professional.service.dto.FacebookValuesDTO;
import com.automation.professional.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.automation.professional.domain.FacebookValues}.
 */
@RestController
@RequestMapping("/api")
public class FacebookValuesResource {

    private final Logger log = LoggerFactory.getLogger(FacebookValuesResource.class);

    private static final String ENTITY_NAME = "facebookValues";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacebookValuesService facebookValuesService;

    private final FacebookValuesRepository facebookValuesRepository;

    public FacebookValuesResource(FacebookValuesService facebookValuesService, FacebookValuesRepository facebookValuesRepository) {
        this.facebookValuesService = facebookValuesService;
        this.facebookValuesRepository = facebookValuesRepository;
    }

    /**
     * {@code POST  /facebook-values} : Create a new facebookValues.
     *
     * @param facebookValuesDTO the facebookValuesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facebookValuesDTO, or with status {@code 400 (Bad Request)} if the facebookValues has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facebook-values")
    public ResponseEntity<FacebookValuesDTO> createFacebookValues(@RequestBody FacebookValuesDTO facebookValuesDTO)
        throws URISyntaxException {
        log.debug("REST request to save FacebookValues : {}", facebookValuesDTO);
        if (facebookValuesDTO.getId() != null) {
            throw new BadRequestAlertException("A new facebookValues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacebookValuesDTO result = facebookValuesService.save(facebookValuesDTO);
        return ResponseEntity
            .created(new URI("/api/facebook-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facebook-values/:id} : Updates an existing facebookValues.
     *
     * @param id the id of the facebookValuesDTO to save.
     * @param facebookValuesDTO the facebookValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facebookValuesDTO,
     * or with status {@code 400 (Bad Request)} if the facebookValuesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facebookValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facebook-values/{id}")
    public ResponseEntity<FacebookValuesDTO> updateFacebookValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacebookValuesDTO facebookValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FacebookValues : {}, {}", id, facebookValuesDTO);
        if (facebookValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facebookValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facebookValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacebookValuesDTO result = facebookValuesService.save(facebookValuesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facebookValuesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facebook-values/:id} : Partial updates given fields of an existing facebookValues, field will ignore if it is null
     *
     * @param id the id of the facebookValuesDTO to save.
     * @param facebookValuesDTO the facebookValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facebookValuesDTO,
     * or with status {@code 400 (Bad Request)} if the facebookValuesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facebookValuesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facebookValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facebook-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FacebookValuesDTO> partialUpdateFacebookValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacebookValuesDTO facebookValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FacebookValues partially : {}, {}", id, facebookValuesDTO);
        if (facebookValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facebookValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facebookValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacebookValuesDTO> result = facebookValuesService.partialUpdate(facebookValuesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facebookValuesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facebook-values} : get all the facebookValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facebookValues in body.
     */
    @GetMapping("/facebook-values")
    public ResponseEntity<List<FacebookValuesDTO>> getAllFacebookValues(Pageable pageable) {
        log.debug("REST request to get a page of FacebookValues");
        Page<FacebookValuesDTO> page = facebookValuesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facebook-values/:id} : get the "id" facebookValues.
     *
     * @param id the id of the facebookValuesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facebookValuesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facebook-values/{id}")
    public ResponseEntity<FacebookValuesDTO> getFacebookValues(@PathVariable Long id) {
        log.debug("REST request to get FacebookValues : {}", id);
        Optional<FacebookValuesDTO> facebookValuesDTO = facebookValuesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facebookValuesDTO);
    }

    /**
     * {@code DELETE  /facebook-values/:id} : delete the "id" facebookValues.
     *
     * @param id the id of the facebookValuesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facebook-values/{id}")
    public ResponseEntity<Void> deleteFacebookValues(@PathVariable Long id) {
        log.debug("REST request to delete FacebookValues : {}", id);
        facebookValuesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/facebook-values?query=:query} : search for the facebookValues corresponding
     * to the query.
     *
     * @param query the query of the facebookValues search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/facebook-values")
    public ResponseEntity<List<FacebookValuesDTO>> searchFacebookValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FacebookValues for query {}", query);
        Page<FacebookValuesDTO> page = facebookValuesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
