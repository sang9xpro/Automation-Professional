package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.LoggersFieldsRepository;
import com.automation.professional.service.LoggersFieldsService;
import com.automation.professional.service.dto.LoggersFieldsDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.LoggersFields}.
 */
@RestController
@RequestMapping("/api")
public class LoggersFieldsResource {

    private final Logger log = LoggerFactory.getLogger(LoggersFieldsResource.class);

    private static final String ENTITY_NAME = "loggersFields";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoggersFieldsService loggersFieldsService;

    private final LoggersFieldsRepository loggersFieldsRepository;

    public LoggersFieldsResource(LoggersFieldsService loggersFieldsService, LoggersFieldsRepository loggersFieldsRepository) {
        this.loggersFieldsService = loggersFieldsService;
        this.loggersFieldsRepository = loggersFieldsRepository;
    }

    /**
     * {@code POST  /loggers-fields} : Create a new loggersFields.
     *
     * @param loggersFieldsDTO the loggersFieldsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loggersFieldsDTO, or with status {@code 400 (Bad Request)} if the loggersFields has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loggers-fields")
    public ResponseEntity<LoggersFieldsDTO> createLoggersFields(@RequestBody LoggersFieldsDTO loggersFieldsDTO) throws URISyntaxException {
        log.debug("REST request to save LoggersFields : {}", loggersFieldsDTO);
        if (loggersFieldsDTO.getId() != null) {
            throw new BadRequestAlertException("A new loggersFields cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoggersFieldsDTO result = loggersFieldsService.save(loggersFieldsDTO);
        return ResponseEntity
            .created(new URI("/api/loggers-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loggers-fields/:id} : Updates an existing loggersFields.
     *
     * @param id the id of the loggersFieldsDTO to save.
     * @param loggersFieldsDTO the loggersFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loggersFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the loggersFieldsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loggersFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loggers-fields/{id}")
    public ResponseEntity<LoggersFieldsDTO> updateLoggersFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoggersFieldsDTO loggersFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoggersFields : {}, {}", id, loggersFieldsDTO);
        if (loggersFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loggersFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loggersFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoggersFieldsDTO result = loggersFieldsService.save(loggersFieldsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loggersFieldsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loggers-fields/:id} : Partial updates given fields of an existing loggersFields, field will ignore if it is null
     *
     * @param id the id of the loggersFieldsDTO to save.
     * @param loggersFieldsDTO the loggersFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loggersFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the loggersFieldsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loggersFieldsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loggersFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loggers-fields/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LoggersFieldsDTO> partialUpdateLoggersFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoggersFieldsDTO loggersFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoggersFields partially : {}, {}", id, loggersFieldsDTO);
        if (loggersFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loggersFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loggersFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoggersFieldsDTO> result = loggersFieldsService.partialUpdate(loggersFieldsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loggersFieldsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loggers-fields} : get all the loggersFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loggersFields in body.
     */
    @GetMapping("/loggers-fields")
    public ResponseEntity<List<LoggersFieldsDTO>> getAllLoggersFields(Pageable pageable) {
        log.debug("REST request to get a page of LoggersFields");
        Page<LoggersFieldsDTO> page = loggersFieldsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loggers-fields/:id} : get the "id" loggersFields.
     *
     * @param id the id of the loggersFieldsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loggersFieldsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loggers-fields/{id}")
    public ResponseEntity<LoggersFieldsDTO> getLoggersFields(@PathVariable Long id) {
        log.debug("REST request to get LoggersFields : {}", id);
        Optional<LoggersFieldsDTO> loggersFieldsDTO = loggersFieldsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loggersFieldsDTO);
    }

    /**
     * {@code DELETE  /loggers-fields/:id} : delete the "id" loggersFields.
     *
     * @param id the id of the loggersFieldsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loggers-fields/{id}")
    public ResponseEntity<Void> deleteLoggersFields(@PathVariable Long id) {
        log.debug("REST request to delete LoggersFields : {}", id);
        loggersFieldsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/loggers-fields?query=:query} : search for the loggersFields corresponding
     * to the query.
     *
     * @param query the query of the loggersFields search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/loggers-fields")
    public ResponseEntity<List<LoggersFieldsDTO>> searchLoggersFields(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LoggersFields for query {}", query);
        Page<LoggersFieldsDTO> page = loggersFieldsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
