package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.DevicesFieldsRepository;
import com.automation.professional.service.DevicesFieldsService;
import com.automation.professional.service.dto.DevicesFieldsDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.DevicesFields}.
 */
@RestController
@RequestMapping("/api")
public class DevicesFieldsResource {

    private final Logger log = LoggerFactory.getLogger(DevicesFieldsResource.class);

    private static final String ENTITY_NAME = "devicesFields";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DevicesFieldsService devicesFieldsService;

    private final DevicesFieldsRepository devicesFieldsRepository;

    public DevicesFieldsResource(DevicesFieldsService devicesFieldsService, DevicesFieldsRepository devicesFieldsRepository) {
        this.devicesFieldsService = devicesFieldsService;
        this.devicesFieldsRepository = devicesFieldsRepository;
    }

    /**
     * {@code POST  /devices-fields} : Create a new devicesFields.
     *
     * @param devicesFieldsDTO the devicesFieldsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new devicesFieldsDTO, or with status {@code 400 (Bad Request)} if the devicesFields has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/devices-fields")
    public ResponseEntity<DevicesFieldsDTO> createDevicesFields(@RequestBody DevicesFieldsDTO devicesFieldsDTO) throws URISyntaxException {
        log.debug("REST request to save DevicesFields : {}", devicesFieldsDTO);
        if (devicesFieldsDTO.getId() != null) {
            throw new BadRequestAlertException("A new devicesFields cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DevicesFieldsDTO result = devicesFieldsService.save(devicesFieldsDTO);
        return ResponseEntity
            .created(new URI("/api/devices-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /devices-fields/:id} : Updates an existing devicesFields.
     *
     * @param id the id of the devicesFieldsDTO to save.
     * @param devicesFieldsDTO the devicesFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated devicesFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the devicesFieldsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the devicesFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/devices-fields/{id}")
    public ResponseEntity<DevicesFieldsDTO> updateDevicesFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DevicesFieldsDTO devicesFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DevicesFields : {}, {}", id, devicesFieldsDTO);
        if (devicesFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, devicesFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!devicesFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DevicesFieldsDTO result = devicesFieldsService.save(devicesFieldsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, devicesFieldsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /devices-fields/:id} : Partial updates given fields of an existing devicesFields, field will ignore if it is null
     *
     * @param id the id of the devicesFieldsDTO to save.
     * @param devicesFieldsDTO the devicesFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated devicesFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the devicesFieldsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the devicesFieldsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the devicesFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/devices-fields/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DevicesFieldsDTO> partialUpdateDevicesFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DevicesFieldsDTO devicesFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DevicesFields partially : {}, {}", id, devicesFieldsDTO);
        if (devicesFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, devicesFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!devicesFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DevicesFieldsDTO> result = devicesFieldsService.partialUpdate(devicesFieldsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, devicesFieldsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /devices-fields} : get all the devicesFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of devicesFields in body.
     */
    @GetMapping("/devices-fields")
    public ResponseEntity<List<DevicesFieldsDTO>> getAllDevicesFields(Pageable pageable) {
        log.debug("REST request to get a page of DevicesFields");
        Page<DevicesFieldsDTO> page = devicesFieldsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /devices-fields/:id} : get the "id" devicesFields.
     *
     * @param id the id of the devicesFieldsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the devicesFieldsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/devices-fields/{id}")
    public ResponseEntity<DevicesFieldsDTO> getDevicesFields(@PathVariable Long id) {
        log.debug("REST request to get DevicesFields : {}", id);
        Optional<DevicesFieldsDTO> devicesFieldsDTO = devicesFieldsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(devicesFieldsDTO);
    }

    /**
     * {@code DELETE  /devices-fields/:id} : delete the "id" devicesFields.
     *
     * @param id the id of the devicesFieldsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/devices-fields/{id}")
    public ResponseEntity<Void> deleteDevicesFields(@PathVariable Long id) {
        log.debug("REST request to delete DevicesFields : {}", id);
        devicesFieldsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/devices-fields?query=:query} : search for the devicesFields corresponding
     * to the query.
     *
     * @param query the query of the devicesFields search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/devices-fields")
    public ResponseEntity<List<DevicesFieldsDTO>> searchDevicesFields(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DevicesFields for query {}", query);
        Page<DevicesFieldsDTO> page = devicesFieldsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
