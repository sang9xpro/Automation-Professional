package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.SchedulerTaskDeviceValuesRepository;
import com.automation.professional.service.SchedulerTaskDeviceValuesService;
import com.automation.professional.service.dto.SchedulerTaskDeviceValuesDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.SchedulerTaskDeviceValues}.
 */
@RestController
@RequestMapping("/api")
public class SchedulerTaskDeviceValuesResource {

    private final Logger log = LoggerFactory.getLogger(SchedulerTaskDeviceValuesResource.class);

    private static final String ENTITY_NAME = "schedulerTaskDeviceValues";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchedulerTaskDeviceValuesService schedulerTaskDeviceValuesService;

    private final SchedulerTaskDeviceValuesRepository schedulerTaskDeviceValuesRepository;

    public SchedulerTaskDeviceValuesResource(
        SchedulerTaskDeviceValuesService schedulerTaskDeviceValuesService,
        SchedulerTaskDeviceValuesRepository schedulerTaskDeviceValuesRepository
    ) {
        this.schedulerTaskDeviceValuesService = schedulerTaskDeviceValuesService;
        this.schedulerTaskDeviceValuesRepository = schedulerTaskDeviceValuesRepository;
    }

    /**
     * {@code POST  /scheduler-task-device-values} : Create a new schedulerTaskDeviceValues.
     *
     * @param schedulerTaskDeviceValuesDTO the schedulerTaskDeviceValuesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schedulerTaskDeviceValuesDTO, or with status {@code 400 (Bad Request)} if the schedulerTaskDeviceValues has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scheduler-task-device-values")
    public ResponseEntity<SchedulerTaskDeviceValuesDTO> createSchedulerTaskDeviceValues(
        @RequestBody SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SchedulerTaskDeviceValues : {}", schedulerTaskDeviceValuesDTO);
        if (schedulerTaskDeviceValuesDTO.getId() != null) {
            throw new BadRequestAlertException("A new schedulerTaskDeviceValues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchedulerTaskDeviceValuesDTO result = schedulerTaskDeviceValuesService.save(schedulerTaskDeviceValuesDTO);
        return ResponseEntity
            .created(new URI("/api/scheduler-task-device-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scheduler-task-device-values/:id} : Updates an existing schedulerTaskDeviceValues.
     *
     * @param id the id of the schedulerTaskDeviceValuesDTO to save.
     * @param schedulerTaskDeviceValuesDTO the schedulerTaskDeviceValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedulerTaskDeviceValuesDTO,
     * or with status {@code 400 (Bad Request)} if the schedulerTaskDeviceValuesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schedulerTaskDeviceValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scheduler-task-device-values/{id}")
    public ResponseEntity<SchedulerTaskDeviceValuesDTO> updateSchedulerTaskDeviceValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SchedulerTaskDeviceValues : {}, {}", id, schedulerTaskDeviceValuesDTO);
        if (schedulerTaskDeviceValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedulerTaskDeviceValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedulerTaskDeviceValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SchedulerTaskDeviceValuesDTO result = schedulerTaskDeviceValuesService.save(schedulerTaskDeviceValuesDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedulerTaskDeviceValuesDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /scheduler-task-device-values/:id} : Partial updates given fields of an existing schedulerTaskDeviceValues, field will ignore if it is null
     *
     * @param id the id of the schedulerTaskDeviceValuesDTO to save.
     * @param schedulerTaskDeviceValuesDTO the schedulerTaskDeviceValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedulerTaskDeviceValuesDTO,
     * or with status {@code 400 (Bad Request)} if the schedulerTaskDeviceValuesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the schedulerTaskDeviceValuesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the schedulerTaskDeviceValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/scheduler-task-device-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SchedulerTaskDeviceValuesDTO> partialUpdateSchedulerTaskDeviceValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SchedulerTaskDeviceValues partially : {}, {}", id, schedulerTaskDeviceValuesDTO);
        if (schedulerTaskDeviceValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedulerTaskDeviceValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedulerTaskDeviceValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchedulerTaskDeviceValuesDTO> result = schedulerTaskDeviceValuesService.partialUpdate(schedulerTaskDeviceValuesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedulerTaskDeviceValuesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /scheduler-task-device-values} : get all the schedulerTaskDeviceValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schedulerTaskDeviceValues in body.
     */
    @GetMapping("/scheduler-task-device-values")
    public ResponseEntity<List<SchedulerTaskDeviceValuesDTO>> getAllSchedulerTaskDeviceValues(Pageable pageable) {
        log.debug("REST request to get a page of SchedulerTaskDeviceValues");
        Page<SchedulerTaskDeviceValuesDTO> page = schedulerTaskDeviceValuesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scheduler-task-device-values/:id} : get the "id" schedulerTaskDeviceValues.
     *
     * @param id the id of the schedulerTaskDeviceValuesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schedulerTaskDeviceValuesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scheduler-task-device-values/{id}")
    public ResponseEntity<SchedulerTaskDeviceValuesDTO> getSchedulerTaskDeviceValues(@PathVariable Long id) {
        log.debug("REST request to get SchedulerTaskDeviceValues : {}", id);
        Optional<SchedulerTaskDeviceValuesDTO> schedulerTaskDeviceValuesDTO = schedulerTaskDeviceValuesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schedulerTaskDeviceValuesDTO);
    }

    /**
     * {@code DELETE  /scheduler-task-device-values/:id} : delete the "id" schedulerTaskDeviceValues.
     *
     * @param id the id of the schedulerTaskDeviceValuesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scheduler-task-device-values/{id}")
    public ResponseEntity<Void> deleteSchedulerTaskDeviceValues(@PathVariable Long id) {
        log.debug("REST request to delete SchedulerTaskDeviceValues : {}", id);
        schedulerTaskDeviceValuesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/scheduler-task-device-values?query=:query} : search for the schedulerTaskDeviceValues corresponding
     * to the query.
     *
     * @param query the query of the schedulerTaskDeviceValues search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/scheduler-task-device-values")
    public ResponseEntity<List<SchedulerTaskDeviceValuesDTO>> searchSchedulerTaskDeviceValues(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of SchedulerTaskDeviceValues for query {}", query);
        Page<SchedulerTaskDeviceValuesDTO> page = schedulerTaskDeviceValuesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
