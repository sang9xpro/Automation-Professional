package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.SchedulerTaskDeviceFieldsRepository;
import com.automation.professional.service.SchedulerTaskDeviceFieldsService;
import com.automation.professional.service.dto.SchedulerTaskDeviceFieldsDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.SchedulerTaskDeviceFields}.
 */
@RestController
@RequestMapping("/api")
public class SchedulerTaskDeviceFieldsResource {

    private final Logger log = LoggerFactory.getLogger(SchedulerTaskDeviceFieldsResource.class);

    private static final String ENTITY_NAME = "schedulerTaskDeviceFields";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchedulerTaskDeviceFieldsService schedulerTaskDeviceFieldsService;

    private final SchedulerTaskDeviceFieldsRepository schedulerTaskDeviceFieldsRepository;

    public SchedulerTaskDeviceFieldsResource(
        SchedulerTaskDeviceFieldsService schedulerTaskDeviceFieldsService,
        SchedulerTaskDeviceFieldsRepository schedulerTaskDeviceFieldsRepository
    ) {
        this.schedulerTaskDeviceFieldsService = schedulerTaskDeviceFieldsService;
        this.schedulerTaskDeviceFieldsRepository = schedulerTaskDeviceFieldsRepository;
    }

    /**
     * {@code POST  /scheduler-task-device-fields} : Create a new schedulerTaskDeviceFields.
     *
     * @param schedulerTaskDeviceFieldsDTO the schedulerTaskDeviceFieldsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schedulerTaskDeviceFieldsDTO, or with status {@code 400 (Bad Request)} if the schedulerTaskDeviceFields has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scheduler-task-device-fields")
    public ResponseEntity<SchedulerTaskDeviceFieldsDTO> createSchedulerTaskDeviceFields(
        @RequestBody SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SchedulerTaskDeviceFields : {}", schedulerTaskDeviceFieldsDTO);
        if (schedulerTaskDeviceFieldsDTO.getId() != null) {
            throw new BadRequestAlertException("A new schedulerTaskDeviceFields cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchedulerTaskDeviceFieldsDTO result = schedulerTaskDeviceFieldsService.save(schedulerTaskDeviceFieldsDTO);
        return ResponseEntity
            .created(new URI("/api/scheduler-task-device-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scheduler-task-device-fields/:id} : Updates an existing schedulerTaskDeviceFields.
     *
     * @param id the id of the schedulerTaskDeviceFieldsDTO to save.
     * @param schedulerTaskDeviceFieldsDTO the schedulerTaskDeviceFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedulerTaskDeviceFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the schedulerTaskDeviceFieldsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schedulerTaskDeviceFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scheduler-task-device-fields/{id}")
    public ResponseEntity<SchedulerTaskDeviceFieldsDTO> updateSchedulerTaskDeviceFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SchedulerTaskDeviceFields : {}, {}", id, schedulerTaskDeviceFieldsDTO);
        if (schedulerTaskDeviceFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedulerTaskDeviceFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedulerTaskDeviceFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SchedulerTaskDeviceFieldsDTO result = schedulerTaskDeviceFieldsService.save(schedulerTaskDeviceFieldsDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedulerTaskDeviceFieldsDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /scheduler-task-device-fields/:id} : Partial updates given fields of an existing schedulerTaskDeviceFields, field will ignore if it is null
     *
     * @param id the id of the schedulerTaskDeviceFieldsDTO to save.
     * @param schedulerTaskDeviceFieldsDTO the schedulerTaskDeviceFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedulerTaskDeviceFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the schedulerTaskDeviceFieldsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the schedulerTaskDeviceFieldsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the schedulerTaskDeviceFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/scheduler-task-device-fields/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SchedulerTaskDeviceFieldsDTO> partialUpdateSchedulerTaskDeviceFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SchedulerTaskDeviceFields partially : {}, {}", id, schedulerTaskDeviceFieldsDTO);
        if (schedulerTaskDeviceFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedulerTaskDeviceFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedulerTaskDeviceFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchedulerTaskDeviceFieldsDTO> result = schedulerTaskDeviceFieldsService.partialUpdate(schedulerTaskDeviceFieldsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedulerTaskDeviceFieldsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /scheduler-task-device-fields} : get all the schedulerTaskDeviceFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schedulerTaskDeviceFields in body.
     */
    @GetMapping("/scheduler-task-device-fields")
    public ResponseEntity<List<SchedulerTaskDeviceFieldsDTO>> getAllSchedulerTaskDeviceFields(Pageable pageable) {
        log.debug("REST request to get a page of SchedulerTaskDeviceFields");
        Page<SchedulerTaskDeviceFieldsDTO> page = schedulerTaskDeviceFieldsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scheduler-task-device-fields/:id} : get the "id" schedulerTaskDeviceFields.
     *
     * @param id the id of the schedulerTaskDeviceFieldsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schedulerTaskDeviceFieldsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scheduler-task-device-fields/{id}")
    public ResponseEntity<SchedulerTaskDeviceFieldsDTO> getSchedulerTaskDeviceFields(@PathVariable Long id) {
        log.debug("REST request to get SchedulerTaskDeviceFields : {}", id);
        Optional<SchedulerTaskDeviceFieldsDTO> schedulerTaskDeviceFieldsDTO = schedulerTaskDeviceFieldsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schedulerTaskDeviceFieldsDTO);
    }

    /**
     * {@code DELETE  /scheduler-task-device-fields/:id} : delete the "id" schedulerTaskDeviceFields.
     *
     * @param id the id of the schedulerTaskDeviceFieldsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scheduler-task-device-fields/{id}")
    public ResponseEntity<Void> deleteSchedulerTaskDeviceFields(@PathVariable Long id) {
        log.debug("REST request to delete SchedulerTaskDeviceFields : {}", id);
        schedulerTaskDeviceFieldsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/scheduler-task-device-fields?query=:query} : search for the schedulerTaskDeviceFields corresponding
     * to the query.
     *
     * @param query the query of the schedulerTaskDeviceFields search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/scheduler-task-device-fields")
    public ResponseEntity<List<SchedulerTaskDeviceFieldsDTO>> searchSchedulerTaskDeviceFields(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of SchedulerTaskDeviceFields for query {}", query);
        Page<SchedulerTaskDeviceFieldsDTO> page = schedulerTaskDeviceFieldsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
