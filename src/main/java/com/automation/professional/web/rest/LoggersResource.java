package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.LoggersRepository;
import com.automation.professional.service.LoggersService;
import com.automation.professional.service.dto.LoggersDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.Loggers}.
 */
@RestController
@RequestMapping("/api")
public class LoggersResource {

    private final Logger log = LoggerFactory.getLogger(LoggersResource.class);

    private static final String ENTITY_NAME = "loggers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoggersService loggersService;

    private final LoggersRepository loggersRepository;

    public LoggersResource(LoggersService loggersService, LoggersRepository loggersRepository) {
        this.loggersService = loggersService;
        this.loggersRepository = loggersRepository;
    }

    /**
     * {@code POST  /loggers} : Create a new loggers.
     *
     * @param loggersDTO the loggersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loggersDTO, or with status {@code 400 (Bad Request)} if the loggers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loggers")
    public ResponseEntity<LoggersDTO> createLoggers(@RequestBody LoggersDTO loggersDTO) throws URISyntaxException {
        log.debug("REST request to save Loggers : {}", loggersDTO);
        if (loggersDTO.getId() != null) {
            throw new BadRequestAlertException("A new loggers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoggersDTO result = loggersService.save(loggersDTO);
        return ResponseEntity
            .created(new URI("/api/loggers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loggers/:id} : Updates an existing loggers.
     *
     * @param id the id of the loggersDTO to save.
     * @param loggersDTO the loggersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loggersDTO,
     * or with status {@code 400 (Bad Request)} if the loggersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loggersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loggers/{id}")
    public ResponseEntity<LoggersDTO> updateLoggers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoggersDTO loggersDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Loggers : {}, {}", id, loggersDTO);
        if (loggersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loggersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loggersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoggersDTO result = loggersService.save(loggersDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loggersDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loggers/:id} : Partial updates given fields of an existing loggers, field will ignore if it is null
     *
     * @param id the id of the loggersDTO to save.
     * @param loggersDTO the loggersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loggersDTO,
     * or with status {@code 400 (Bad Request)} if the loggersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loggersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loggersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loggers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LoggersDTO> partialUpdateLoggers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoggersDTO loggersDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Loggers partially : {}, {}", id, loggersDTO);
        if (loggersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loggersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loggersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoggersDTO> result = loggersService.partialUpdate(loggersDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loggersDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loggers} : get all the loggers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loggers in body.
     */
    @GetMapping("/loggers")
    public ResponseEntity<List<LoggersDTO>> getAllLoggers(Pageable pageable) {
        log.debug("REST request to get a page of Loggers");
        Page<LoggersDTO> page = loggersService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loggers/:id} : get the "id" loggers.
     *
     * @param id the id of the loggersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loggersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loggers/{id}")
    public ResponseEntity<LoggersDTO> getLoggers(@PathVariable Long id) {
        log.debug("REST request to get Loggers : {}", id);
        Optional<LoggersDTO> loggersDTO = loggersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loggersDTO);
    }

    /**
     * {@code DELETE  /loggers/:id} : delete the "id" loggers.
     *
     * @param id the id of the loggersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loggers/{id}")
    public ResponseEntity<Void> deleteLoggers(@PathVariable Long id) {
        log.debug("REST request to delete Loggers : {}", id);
        loggersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/loggers?query=:query} : search for the loggers corresponding
     * to the query.
     *
     * @param query the query of the loggers search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/loggers")
    public ResponseEntity<List<LoggersDTO>> searchLoggers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Loggers for query {}", query);
        Page<LoggersDTO> page = loggersService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
