package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.HistoryValuesRepository;
import com.automation.professional.service.HistoryValuesService;
import com.automation.professional.service.dto.HistoryValuesDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.HistoryValues}.
 */
@RestController
@RequestMapping("/api")
public class HistoryValuesResource {

    private final Logger log = LoggerFactory.getLogger(HistoryValuesResource.class);

    private static final String ENTITY_NAME = "historyValues";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoryValuesService historyValuesService;

    private final HistoryValuesRepository historyValuesRepository;

    public HistoryValuesResource(HistoryValuesService historyValuesService, HistoryValuesRepository historyValuesRepository) {
        this.historyValuesService = historyValuesService;
        this.historyValuesRepository = historyValuesRepository;
    }

    /**
     * {@code POST  /history-values} : Create a new historyValues.
     *
     * @param historyValuesDTO the historyValuesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historyValuesDTO, or with status {@code 400 (Bad Request)} if the historyValues has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/history-values")
    public ResponseEntity<HistoryValuesDTO> createHistoryValues(@RequestBody HistoryValuesDTO historyValuesDTO) throws URISyntaxException {
        log.debug("REST request to save HistoryValues : {}", historyValuesDTO);
        if (historyValuesDTO.getId() != null) {
            throw new BadRequestAlertException("A new historyValues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoryValuesDTO result = historyValuesService.save(historyValuesDTO);
        return ResponseEntity
            .created(new URI("/api/history-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /history-values/:id} : Updates an existing historyValues.
     *
     * @param id the id of the historyValuesDTO to save.
     * @param historyValuesDTO the historyValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historyValuesDTO,
     * or with status {@code 400 (Bad Request)} if the historyValuesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historyValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/history-values/{id}")
    public ResponseEntity<HistoryValuesDTO> updateHistoryValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HistoryValuesDTO historyValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HistoryValues : {}, {}", id, historyValuesDTO);
        if (historyValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historyValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historyValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HistoryValuesDTO result = historyValuesService.save(historyValuesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historyValuesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /history-values/:id} : Partial updates given fields of an existing historyValues, field will ignore if it is null
     *
     * @param id the id of the historyValuesDTO to save.
     * @param historyValuesDTO the historyValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historyValuesDTO,
     * or with status {@code 400 (Bad Request)} if the historyValuesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historyValuesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historyValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/history-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HistoryValuesDTO> partialUpdateHistoryValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HistoryValuesDTO historyValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HistoryValues partially : {}, {}", id, historyValuesDTO);
        if (historyValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historyValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historyValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoryValuesDTO> result = historyValuesService.partialUpdate(historyValuesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historyValuesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /history-values} : get all the historyValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historyValues in body.
     */
    @GetMapping("/history-values")
    public ResponseEntity<List<HistoryValuesDTO>> getAllHistoryValues(Pageable pageable) {
        log.debug("REST request to get a page of HistoryValues");
        Page<HistoryValuesDTO> page = historyValuesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /history-values/:id} : get the "id" historyValues.
     *
     * @param id the id of the historyValuesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historyValuesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/history-values/{id}")
    public ResponseEntity<HistoryValuesDTO> getHistoryValues(@PathVariable Long id) {
        log.debug("REST request to get HistoryValues : {}", id);
        Optional<HistoryValuesDTO> historyValuesDTO = historyValuesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historyValuesDTO);
    }

    /**
     * {@code DELETE  /history-values/:id} : delete the "id" historyValues.
     *
     * @param id the id of the historyValuesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/history-values/{id}")
    public ResponseEntity<Void> deleteHistoryValues(@PathVariable Long id) {
        log.debug("REST request to delete HistoryValues : {}", id);
        historyValuesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/history-values?query=:query} : search for the historyValues corresponding
     * to the query.
     *
     * @param query the query of the historyValues search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/history-values")
    public ResponseEntity<List<HistoryValuesDTO>> searchHistoryValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of HistoryValues for query {}", query);
        Page<HistoryValuesDTO> page = historyValuesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
