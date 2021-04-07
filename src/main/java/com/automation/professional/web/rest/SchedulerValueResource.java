package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.SchedulerValueRepository;
import com.automation.professional.service.SchedulerValueService;
import com.automation.professional.service.dto.SchedulerValueDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.SchedulerValue}.
 */
@RestController
@RequestMapping("/api")
public class SchedulerValueResource {

    private final Logger log = LoggerFactory.getLogger(SchedulerValueResource.class);

    private static final String ENTITY_NAME = "schedulerValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchedulerValueService schedulerValueService;

    private final SchedulerValueRepository schedulerValueRepository;

    public SchedulerValueResource(SchedulerValueService schedulerValueService, SchedulerValueRepository schedulerValueRepository) {
        this.schedulerValueService = schedulerValueService;
        this.schedulerValueRepository = schedulerValueRepository;
    }

    /**
     * {@code POST  /scheduler-values} : Create a new schedulerValue.
     *
     * @param schedulerValueDTO the schedulerValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schedulerValueDTO, or with status {@code 400 (Bad Request)} if the schedulerValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scheduler-values")
    public ResponseEntity<SchedulerValueDTO> createSchedulerValue(@RequestBody SchedulerValueDTO schedulerValueDTO)
        throws URISyntaxException {
        log.debug("REST request to save SchedulerValue : {}", schedulerValueDTO);
        if (schedulerValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new schedulerValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchedulerValueDTO result = schedulerValueService.save(schedulerValueDTO);
        return ResponseEntity
            .created(new URI("/api/scheduler-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scheduler-values/:id} : Updates an existing schedulerValue.
     *
     * @param id the id of the schedulerValueDTO to save.
     * @param schedulerValueDTO the schedulerValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedulerValueDTO,
     * or with status {@code 400 (Bad Request)} if the schedulerValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schedulerValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scheduler-values/{id}")
    public ResponseEntity<SchedulerValueDTO> updateSchedulerValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchedulerValueDTO schedulerValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SchedulerValue : {}, {}", id, schedulerValueDTO);
        if (schedulerValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedulerValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedulerValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SchedulerValueDTO result = schedulerValueService.save(schedulerValueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedulerValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /scheduler-values/:id} : Partial updates given fields of an existing schedulerValue, field will ignore if it is null
     *
     * @param id the id of the schedulerValueDTO to save.
     * @param schedulerValueDTO the schedulerValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedulerValueDTO,
     * or with status {@code 400 (Bad Request)} if the schedulerValueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the schedulerValueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the schedulerValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/scheduler-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SchedulerValueDTO> partialUpdateSchedulerValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchedulerValueDTO schedulerValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SchedulerValue partially : {}, {}", id, schedulerValueDTO);
        if (schedulerValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedulerValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedulerValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchedulerValueDTO> result = schedulerValueService.partialUpdate(schedulerValueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedulerValueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /scheduler-values} : get all the schedulerValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schedulerValues in body.
     */
    @GetMapping("/scheduler-values")
    public ResponseEntity<List<SchedulerValueDTO>> getAllSchedulerValues(Pageable pageable) {
        log.debug("REST request to get a page of SchedulerValues");
        Page<SchedulerValueDTO> page = schedulerValueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scheduler-values/:id} : get the "id" schedulerValue.
     *
     * @param id the id of the schedulerValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schedulerValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scheduler-values/{id}")
    public ResponseEntity<SchedulerValueDTO> getSchedulerValue(@PathVariable Long id) {
        log.debug("REST request to get SchedulerValue : {}", id);
        Optional<SchedulerValueDTO> schedulerValueDTO = schedulerValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schedulerValueDTO);
    }

    /**
     * {@code DELETE  /scheduler-values/:id} : delete the "id" schedulerValue.
     *
     * @param id the id of the schedulerValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scheduler-values/{id}")
    public ResponseEntity<Void> deleteSchedulerValue(@PathVariable Long id) {
        log.debug("REST request to delete SchedulerValue : {}", id);
        schedulerValueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/scheduler-values?query=:query} : search for the schedulerValue corresponding
     * to the query.
     *
     * @param query the query of the schedulerValue search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/scheduler-values")
    public ResponseEntity<List<SchedulerValueDTO>> searchSchedulerValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SchedulerValues for query {}", query);
        Page<SchedulerValueDTO> page = schedulerValueService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
