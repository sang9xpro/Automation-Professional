package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.LoggersValuesRepository;
import com.automation.professional.service.LoggersValuesService;
import com.automation.professional.service.dto.LoggersValuesDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.LoggersValues}.
 */
@RestController
@RequestMapping("/api")
public class LoggersValuesResource {

    private final Logger log = LoggerFactory.getLogger(LoggersValuesResource.class);

    private static final String ENTITY_NAME = "loggersValues";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoggersValuesService loggersValuesService;

    private final LoggersValuesRepository loggersValuesRepository;

    public LoggersValuesResource(LoggersValuesService loggersValuesService, LoggersValuesRepository loggersValuesRepository) {
        this.loggersValuesService = loggersValuesService;
        this.loggersValuesRepository = loggersValuesRepository;
    }

    /**
     * {@code POST  /loggers-values} : Create a new loggersValues.
     *
     * @param loggersValuesDTO the loggersValuesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loggersValuesDTO, or with status {@code 400 (Bad Request)} if the loggersValues has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loggers-values")
    public ResponseEntity<LoggersValuesDTO> createLoggersValues(@RequestBody LoggersValuesDTO loggersValuesDTO) throws URISyntaxException {
        log.debug("REST request to save LoggersValues : {}", loggersValuesDTO);
        if (loggersValuesDTO.getId() != null) {
            throw new BadRequestAlertException("A new loggersValues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoggersValuesDTO result = loggersValuesService.save(loggersValuesDTO);
        return ResponseEntity
            .created(new URI("/api/loggers-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loggers-values/:id} : Updates an existing loggersValues.
     *
     * @param id the id of the loggersValuesDTO to save.
     * @param loggersValuesDTO the loggersValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loggersValuesDTO,
     * or with status {@code 400 (Bad Request)} if the loggersValuesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loggersValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loggers-values/{id}")
    public ResponseEntity<LoggersValuesDTO> updateLoggersValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoggersValuesDTO loggersValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoggersValues : {}, {}", id, loggersValuesDTO);
        if (loggersValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loggersValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loggersValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoggersValuesDTO result = loggersValuesService.save(loggersValuesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loggersValuesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loggers-values/:id} : Partial updates given fields of an existing loggersValues, field will ignore if it is null
     *
     * @param id the id of the loggersValuesDTO to save.
     * @param loggersValuesDTO the loggersValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loggersValuesDTO,
     * or with status {@code 400 (Bad Request)} if the loggersValuesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loggersValuesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loggersValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loggers-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LoggersValuesDTO> partialUpdateLoggersValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoggersValuesDTO loggersValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoggersValues partially : {}, {}", id, loggersValuesDTO);
        if (loggersValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loggersValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loggersValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoggersValuesDTO> result = loggersValuesService.partialUpdate(loggersValuesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loggersValuesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loggers-values} : get all the loggersValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loggersValues in body.
     */
    @GetMapping("/loggers-values")
    public ResponseEntity<List<LoggersValuesDTO>> getAllLoggersValues(Pageable pageable) {
        log.debug("REST request to get a page of LoggersValues");
        Page<LoggersValuesDTO> page = loggersValuesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loggers-values/:id} : get the "id" loggersValues.
     *
     * @param id the id of the loggersValuesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loggersValuesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loggers-values/{id}")
    public ResponseEntity<LoggersValuesDTO> getLoggersValues(@PathVariable Long id) {
        log.debug("REST request to get LoggersValues : {}", id);
        Optional<LoggersValuesDTO> loggersValuesDTO = loggersValuesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loggersValuesDTO);
    }

    /**
     * {@code DELETE  /loggers-values/:id} : delete the "id" loggersValues.
     *
     * @param id the id of the loggersValuesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loggers-values/{id}")
    public ResponseEntity<Void> deleteLoggersValues(@PathVariable Long id) {
        log.debug("REST request to delete LoggersValues : {}", id);
        loggersValuesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/loggers-values?query=:query} : search for the loggersValues corresponding
     * to the query.
     *
     * @param query the query of the loggersValues search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/loggers-values")
    public ResponseEntity<List<LoggersValuesDTO>> searchLoggersValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LoggersValues for query {}", query);
        Page<LoggersValuesDTO> page = loggersValuesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
