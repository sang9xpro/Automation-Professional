package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.MostOfContValuesRepository;
import com.automation.professional.service.MostOfContValuesService;
import com.automation.professional.service.dto.MostOfContValuesDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.MostOfContValues}.
 */
@RestController
@RequestMapping("/api")
public class MostOfContValuesResource {

    private final Logger log = LoggerFactory.getLogger(MostOfContValuesResource.class);

    private static final String ENTITY_NAME = "mostOfContValues";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MostOfContValuesService mostOfContValuesService;

    private final MostOfContValuesRepository mostOfContValuesRepository;

    public MostOfContValuesResource(
        MostOfContValuesService mostOfContValuesService,
        MostOfContValuesRepository mostOfContValuesRepository
    ) {
        this.mostOfContValuesService = mostOfContValuesService;
        this.mostOfContValuesRepository = mostOfContValuesRepository;
    }

    /**
     * {@code POST  /most-of-cont-values} : Create a new mostOfContValues.
     *
     * @param mostOfContValuesDTO the mostOfContValuesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mostOfContValuesDTO, or with status {@code 400 (Bad Request)} if the mostOfContValues has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/most-of-cont-values")
    public ResponseEntity<MostOfContValuesDTO> createMostOfContValues(@RequestBody MostOfContValuesDTO mostOfContValuesDTO)
        throws URISyntaxException {
        log.debug("REST request to save MostOfContValues : {}", mostOfContValuesDTO);
        if (mostOfContValuesDTO.getId() != null) {
            throw new BadRequestAlertException("A new mostOfContValues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MostOfContValuesDTO result = mostOfContValuesService.save(mostOfContValuesDTO);
        return ResponseEntity
            .created(new URI("/api/most-of-cont-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /most-of-cont-values/:id} : Updates an existing mostOfContValues.
     *
     * @param id the id of the mostOfContValuesDTO to save.
     * @param mostOfContValuesDTO the mostOfContValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mostOfContValuesDTO,
     * or with status {@code 400 (Bad Request)} if the mostOfContValuesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mostOfContValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/most-of-cont-values/{id}")
    public ResponseEntity<MostOfContValuesDTO> updateMostOfContValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MostOfContValuesDTO mostOfContValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MostOfContValues : {}, {}", id, mostOfContValuesDTO);
        if (mostOfContValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mostOfContValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mostOfContValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MostOfContValuesDTO result = mostOfContValuesService.save(mostOfContValuesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mostOfContValuesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /most-of-cont-values/:id} : Partial updates given fields of an existing mostOfContValues, field will ignore if it is null
     *
     * @param id the id of the mostOfContValuesDTO to save.
     * @param mostOfContValuesDTO the mostOfContValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mostOfContValuesDTO,
     * or with status {@code 400 (Bad Request)} if the mostOfContValuesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mostOfContValuesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mostOfContValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/most-of-cont-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MostOfContValuesDTO> partialUpdateMostOfContValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MostOfContValuesDTO mostOfContValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MostOfContValues partially : {}, {}", id, mostOfContValuesDTO);
        if (mostOfContValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mostOfContValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mostOfContValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MostOfContValuesDTO> result = mostOfContValuesService.partialUpdate(mostOfContValuesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mostOfContValuesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /most-of-cont-values} : get all the mostOfContValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mostOfContValues in body.
     */
    @GetMapping("/most-of-cont-values")
    public ResponseEntity<List<MostOfContValuesDTO>> getAllMostOfContValues(Pageable pageable) {
        log.debug("REST request to get a page of MostOfContValues");
        Page<MostOfContValuesDTO> page = mostOfContValuesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /most-of-cont-values/:id} : get the "id" mostOfContValues.
     *
     * @param id the id of the mostOfContValuesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mostOfContValuesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/most-of-cont-values/{id}")
    public ResponseEntity<MostOfContValuesDTO> getMostOfContValues(@PathVariable Long id) {
        log.debug("REST request to get MostOfContValues : {}", id);
        Optional<MostOfContValuesDTO> mostOfContValuesDTO = mostOfContValuesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mostOfContValuesDTO);
    }

    /**
     * {@code DELETE  /most-of-cont-values/:id} : delete the "id" mostOfContValues.
     *
     * @param id the id of the mostOfContValuesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/most-of-cont-values/{id}")
    public ResponseEntity<Void> deleteMostOfContValues(@PathVariable Long id) {
        log.debug("REST request to delete MostOfContValues : {}", id);
        mostOfContValuesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/most-of-cont-values?query=:query} : search for the mostOfContValues corresponding
     * to the query.
     *
     * @param query the query of the mostOfContValues search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/most-of-cont-values")
    public ResponseEntity<List<MostOfContValuesDTO>> searchMostOfContValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MostOfContValues for query {}", query);
        Page<MostOfContValuesDTO> page = mostOfContValuesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
