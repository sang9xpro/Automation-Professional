package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.SchedulerTaskDeviceRepository;
import com.automation.professional.service.SchedulerTaskDeviceService;
import com.automation.professional.service.dto.SchedulerTaskDeviceDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.SchedulerTaskDevice}.
 */
@RestController
@RequestMapping("/api")
public class SchedulerTaskDeviceResource {

    private final Logger log = LoggerFactory.getLogger(SchedulerTaskDeviceResource.class);

    private static final String ENTITY_NAME = "schedulerTaskDevice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchedulerTaskDeviceService schedulerTaskDeviceService;

    private final SchedulerTaskDeviceRepository schedulerTaskDeviceRepository;

    public SchedulerTaskDeviceResource(
        SchedulerTaskDeviceService schedulerTaskDeviceService,
        SchedulerTaskDeviceRepository schedulerTaskDeviceRepository
    ) {
        this.schedulerTaskDeviceService = schedulerTaskDeviceService;
        this.schedulerTaskDeviceRepository = schedulerTaskDeviceRepository;
    }

    /**
     * {@code POST  /scheduler-task-devices} : Create a new schedulerTaskDevice.
     *
     * @param schedulerTaskDeviceDTO the schedulerTaskDeviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schedulerTaskDeviceDTO, or with status {@code 400 (Bad Request)} if the schedulerTaskDevice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scheduler-task-devices")
    public ResponseEntity<SchedulerTaskDeviceDTO> createSchedulerTaskDevice(@RequestBody SchedulerTaskDeviceDTO schedulerTaskDeviceDTO)
        throws URISyntaxException {
        log.debug("REST request to save SchedulerTaskDevice : {}", schedulerTaskDeviceDTO);
        if (schedulerTaskDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new schedulerTaskDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchedulerTaskDeviceDTO result = schedulerTaskDeviceService.save(schedulerTaskDeviceDTO);
        return ResponseEntity
            .created(new URI("/api/scheduler-task-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scheduler-task-devices/:id} : Updates an existing schedulerTaskDevice.
     *
     * @param id the id of the schedulerTaskDeviceDTO to save.
     * @param schedulerTaskDeviceDTO the schedulerTaskDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedulerTaskDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the schedulerTaskDeviceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schedulerTaskDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scheduler-task-devices/{id}")
    public ResponseEntity<SchedulerTaskDeviceDTO> updateSchedulerTaskDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchedulerTaskDeviceDTO schedulerTaskDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SchedulerTaskDevice : {}, {}", id, schedulerTaskDeviceDTO);
        if (schedulerTaskDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedulerTaskDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedulerTaskDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SchedulerTaskDeviceDTO result = schedulerTaskDeviceService.save(schedulerTaskDeviceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedulerTaskDeviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /scheduler-task-devices/:id} : Partial updates given fields of an existing schedulerTaskDevice, field will ignore if it is null
     *
     * @param id the id of the schedulerTaskDeviceDTO to save.
     * @param schedulerTaskDeviceDTO the schedulerTaskDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedulerTaskDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the schedulerTaskDeviceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the schedulerTaskDeviceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the schedulerTaskDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/scheduler-task-devices/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SchedulerTaskDeviceDTO> partialUpdateSchedulerTaskDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchedulerTaskDeviceDTO schedulerTaskDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SchedulerTaskDevice partially : {}, {}", id, schedulerTaskDeviceDTO);
        if (schedulerTaskDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedulerTaskDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedulerTaskDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchedulerTaskDeviceDTO> result = schedulerTaskDeviceService.partialUpdate(schedulerTaskDeviceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedulerTaskDeviceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /scheduler-task-devices} : get all the schedulerTaskDevices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schedulerTaskDevices in body.
     */
    @GetMapping("/scheduler-task-devices")
    public ResponseEntity<List<SchedulerTaskDeviceDTO>> getAllSchedulerTaskDevices(Pageable pageable) {
        log.debug("REST request to get a page of SchedulerTaskDevices");
        Page<SchedulerTaskDeviceDTO> page = schedulerTaskDeviceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scheduler-task-devices/:id} : get the "id" schedulerTaskDevice.
     *
     * @param id the id of the schedulerTaskDeviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schedulerTaskDeviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scheduler-task-devices/{id}")
    public ResponseEntity<SchedulerTaskDeviceDTO> getSchedulerTaskDevice(@PathVariable Long id) {
        log.debug("REST request to get SchedulerTaskDevice : {}", id);
        Optional<SchedulerTaskDeviceDTO> schedulerTaskDeviceDTO = schedulerTaskDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schedulerTaskDeviceDTO);
    }

    /**
     * {@code DELETE  /scheduler-task-devices/:id} : delete the "id" schedulerTaskDevice.
     *
     * @param id the id of the schedulerTaskDeviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scheduler-task-devices/{id}")
    public ResponseEntity<Void> deleteSchedulerTaskDevice(@PathVariable Long id) {
        log.debug("REST request to delete SchedulerTaskDevice : {}", id);
        schedulerTaskDeviceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/scheduler-task-devices?query=:query} : search for the schedulerTaskDevice corresponding
     * to the query.
     *
     * @param query the query of the schedulerTaskDevice search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/scheduler-task-devices")
    public ResponseEntity<List<SchedulerTaskDeviceDTO>> searchSchedulerTaskDevices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SchedulerTaskDevices for query {}", query);
        Page<SchedulerTaskDeviceDTO> page = schedulerTaskDeviceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
