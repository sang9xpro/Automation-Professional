package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.DeviceValuesRepository;
import com.automation.professional.service.DeviceValuesService;
import com.automation.professional.service.dto.DeviceValuesDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.DeviceValues}.
 */
@RestController
@RequestMapping("/api")
public class DeviceValuesResource {

    private final Logger log = LoggerFactory.getLogger(DeviceValuesResource.class);

    private static final String ENTITY_NAME = "deviceValues";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceValuesService deviceValuesService;

    private final DeviceValuesRepository deviceValuesRepository;

    public DeviceValuesResource(DeviceValuesService deviceValuesService, DeviceValuesRepository deviceValuesRepository) {
        this.deviceValuesService = deviceValuesService;
        this.deviceValuesRepository = deviceValuesRepository;
    }

    /**
     * {@code POST  /device-values} : Create a new deviceValues.
     *
     * @param deviceValuesDTO the deviceValuesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceValuesDTO, or with status {@code 400 (Bad Request)} if the deviceValues has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/device-values")
    public ResponseEntity<DeviceValuesDTO> createDeviceValues(@RequestBody DeviceValuesDTO deviceValuesDTO) throws URISyntaxException {
        log.debug("REST request to save DeviceValues : {}", deviceValuesDTO);
        if (deviceValuesDTO.getId() != null) {
            throw new BadRequestAlertException("A new deviceValues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceValuesDTO result = deviceValuesService.save(deviceValuesDTO);
        return ResponseEntity
            .created(new URI("/api/device-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /device-values/:id} : Updates an existing deviceValues.
     *
     * @param id the id of the deviceValuesDTO to save.
     * @param deviceValuesDTO the deviceValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceValuesDTO,
     * or with status {@code 400 (Bad Request)} if the deviceValuesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/device-values/{id}")
    public ResponseEntity<DeviceValuesDTO> updateDeviceValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeviceValuesDTO deviceValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeviceValues : {}, {}", id, deviceValuesDTO);
        if (deviceValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeviceValuesDTO result = deviceValuesService.save(deviceValuesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceValuesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /device-values/:id} : Partial updates given fields of an existing deviceValues, field will ignore if it is null
     *
     * @param id the id of the deviceValuesDTO to save.
     * @param deviceValuesDTO the deviceValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceValuesDTO,
     * or with status {@code 400 (Bad Request)} if the deviceValuesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deviceValuesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deviceValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/device-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DeviceValuesDTO> partialUpdateDeviceValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeviceValuesDTO deviceValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeviceValues partially : {}, {}", id, deviceValuesDTO);
        if (deviceValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeviceValuesDTO> result = deviceValuesService.partialUpdate(deviceValuesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceValuesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /device-values} : get all the deviceValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceValues in body.
     */
    @GetMapping("/device-values")
    public ResponseEntity<List<DeviceValuesDTO>> getAllDeviceValues(Pageable pageable) {
        log.debug("REST request to get a page of DeviceValues");
        Page<DeviceValuesDTO> page = deviceValuesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /device-values/:id} : get the "id" deviceValues.
     *
     * @param id the id of the deviceValuesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceValuesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/device-values/{id}")
    public ResponseEntity<DeviceValuesDTO> getDeviceValues(@PathVariable Long id) {
        log.debug("REST request to get DeviceValues : {}", id);
        Optional<DeviceValuesDTO> deviceValuesDTO = deviceValuesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deviceValuesDTO);
    }

    /**
     * {@code DELETE  /device-values/:id} : delete the "id" deviceValues.
     *
     * @param id the id of the deviceValuesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/device-values/{id}")
    public ResponseEntity<Void> deleteDeviceValues(@PathVariable Long id) {
        log.debug("REST request to delete DeviceValues : {}", id);
        deviceValuesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/device-values?query=:query} : search for the deviceValues corresponding
     * to the query.
     *
     * @param query the query of the deviceValues search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/device-values")
    public ResponseEntity<List<DeviceValuesDTO>> searchDeviceValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DeviceValues for query {}", query);
        Page<DeviceValuesDTO> page = deviceValuesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
