package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.SchedulerFieldsRepository;
import com.automation.professional.service.SchedulerFieldsService;
import com.automation.professional.service.dto.SchedulerFieldsDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.SchedulerFields}.
 */
@RestController
@RequestMapping("/api")
public class SchedulerFieldsResource {

    private final Logger log = LoggerFactory.getLogger(SchedulerFieldsResource.class);

    private static final String ENTITY_NAME = "schedulerFields";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchedulerFieldsService schedulerFieldsService;

    private final SchedulerFieldsRepository schedulerFieldsRepository;

    public SchedulerFieldsResource(SchedulerFieldsService schedulerFieldsService, SchedulerFieldsRepository schedulerFieldsRepository) {
        this.schedulerFieldsService = schedulerFieldsService;
        this.schedulerFieldsRepository = schedulerFieldsRepository;
    }

    /**
     * {@code POST  /scheduler-fields} : Create a new schedulerFields.
     *
     * @param schedulerFieldsDTO the schedulerFieldsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schedulerFieldsDTO, or with status {@code 400 (Bad Request)} if the schedulerFields has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scheduler-fields")
    public ResponseEntity<SchedulerFieldsDTO> createSchedulerFields(@RequestBody SchedulerFieldsDTO schedulerFieldsDTO)
        throws URISyntaxException {
        log.debug("REST request to save SchedulerFields : {}", schedulerFieldsDTO);
        if (schedulerFieldsDTO.getId() != null) {
            throw new BadRequestAlertException("A new schedulerFields cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchedulerFieldsDTO result = schedulerFieldsService.save(schedulerFieldsDTO);
        return ResponseEntity
            .created(new URI("/api/scheduler-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scheduler-fields/:id} : Updates an existing schedulerFields.
     *
     * @param id the id of the schedulerFieldsDTO to save.
     * @param schedulerFieldsDTO the schedulerFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedulerFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the schedulerFieldsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schedulerFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scheduler-fields/{id}")
    public ResponseEntity<SchedulerFieldsDTO> updateSchedulerFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchedulerFieldsDTO schedulerFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SchedulerFields : {}, {}", id, schedulerFieldsDTO);
        if (schedulerFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedulerFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedulerFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SchedulerFieldsDTO result = schedulerFieldsService.save(schedulerFieldsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedulerFieldsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /scheduler-fields/:id} : Partial updates given fields of an existing schedulerFields, field will ignore if it is null
     *
     * @param id the id of the schedulerFieldsDTO to save.
     * @param schedulerFieldsDTO the schedulerFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedulerFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the schedulerFieldsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the schedulerFieldsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the schedulerFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/scheduler-fields/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SchedulerFieldsDTO> partialUpdateSchedulerFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchedulerFieldsDTO schedulerFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SchedulerFields partially : {}, {}", id, schedulerFieldsDTO);
        if (schedulerFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedulerFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedulerFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchedulerFieldsDTO> result = schedulerFieldsService.partialUpdate(schedulerFieldsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedulerFieldsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /scheduler-fields} : get all the schedulerFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schedulerFields in body.
     */
    @GetMapping("/scheduler-fields")
    public ResponseEntity<List<SchedulerFieldsDTO>> getAllSchedulerFields(Pageable pageable) {
        log.debug("REST request to get a page of SchedulerFields");
        Page<SchedulerFieldsDTO> page = schedulerFieldsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scheduler-fields/:id} : get the "id" schedulerFields.
     *
     * @param id the id of the schedulerFieldsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schedulerFieldsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scheduler-fields/{id}")
    public ResponseEntity<SchedulerFieldsDTO> getSchedulerFields(@PathVariable Long id) {
        log.debug("REST request to get SchedulerFields : {}", id);
        Optional<SchedulerFieldsDTO> schedulerFieldsDTO = schedulerFieldsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schedulerFieldsDTO);
    }

    /**
     * {@code DELETE  /scheduler-fields/:id} : delete the "id" schedulerFields.
     *
     * @param id the id of the schedulerFieldsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scheduler-fields/{id}")
    public ResponseEntity<Void> deleteSchedulerFields(@PathVariable Long id) {
        log.debug("REST request to delete SchedulerFields : {}", id);
        schedulerFieldsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/scheduler-fields?query=:query} : search for the schedulerFields corresponding
     * to the query.
     *
     * @param query the query of the schedulerFields search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/scheduler-fields")
    public ResponseEntity<List<SchedulerFieldsDTO>> searchSchedulerFields(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SchedulerFields for query {}", query);
        Page<SchedulerFieldsDTO> page = schedulerFieldsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
