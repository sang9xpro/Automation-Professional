package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.DevicesRepository;
import com.automation.professional.service.DevicesService;
import com.automation.professional.service.dto.DevicesDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.Devices}.
 */
@RestController
@RequestMapping("/api")
public class DevicesResource {

    private final Logger log = LoggerFactory.getLogger(DevicesResource.class);

    private static final String ENTITY_NAME = "devices";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DevicesService devicesService;

    private final DevicesRepository devicesRepository;

    public DevicesResource(DevicesService devicesService, DevicesRepository devicesRepository) {
        this.devicesService = devicesService;
        this.devicesRepository = devicesRepository;
    }

    /**
     * {@code POST  /devices} : Create a new devices.
     *
     * @param devicesDTO the devicesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new devicesDTO, or with status {@code 400 (Bad Request)} if the devices has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/devices")
    public ResponseEntity<DevicesDTO> createDevices(@RequestBody DevicesDTO devicesDTO) throws URISyntaxException {
        log.debug("REST request to save Devices : {}", devicesDTO);
        if (devicesDTO.getId() != null) {
            throw new BadRequestAlertException("A new devices cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DevicesDTO result = devicesService.save(devicesDTO);
        return ResponseEntity
            .created(new URI("/api/devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /devices/:id} : Updates an existing devices.
     *
     * @param id the id of the devicesDTO to save.
     * @param devicesDTO the devicesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated devicesDTO,
     * or with status {@code 400 (Bad Request)} if the devicesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the devicesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/devices/{id}")
    public ResponseEntity<DevicesDTO> updateDevices(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DevicesDTO devicesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Devices : {}, {}", id, devicesDTO);
        if (devicesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, devicesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!devicesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DevicesDTO result = devicesService.save(devicesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, devicesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /devices/:id} : Partial updates given fields of an existing devices, field will ignore if it is null
     *
     * @param id the id of the devicesDTO to save.
     * @param devicesDTO the devicesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated devicesDTO,
     * or with status {@code 400 (Bad Request)} if the devicesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the devicesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the devicesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/devices/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DevicesDTO> partialUpdateDevices(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DevicesDTO devicesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Devices partially : {}, {}", id, devicesDTO);
        if (devicesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, devicesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!devicesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DevicesDTO> result = devicesService.partialUpdate(devicesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, devicesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /devices} : get all the devices.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of devices in body.
     */
    @GetMapping("/devices")
    public ResponseEntity<List<DevicesDTO>> getAllDevices(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Devices");
        Page<DevicesDTO> page;
        if (eagerload) {
            page = devicesService.findAllWithEagerRelationships(pageable);
        } else {
            page = devicesService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /devices/:id} : get the "id" devices.
     *
     * @param id the id of the devicesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the devicesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/devices/{id}")
    public ResponseEntity<DevicesDTO> getDevices(@PathVariable Long id) {
        log.debug("REST request to get Devices : {}", id);
        Optional<DevicesDTO> devicesDTO = devicesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(devicesDTO);
    }

    /**
     * {@code DELETE  /devices/:id} : delete the "id" devices.
     *
     * @param id the id of the devicesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> deleteDevices(@PathVariable Long id) {
        log.debug("REST request to delete Devices : {}", id);
        devicesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/devices?query=:query} : search for the devices corresponding
     * to the query.
     *
     * @param query the query of the devices search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/devices")
    public ResponseEntity<List<DevicesDTO>> searchDevices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Devices for query {}", query);
        Page<DevicesDTO> page = devicesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
