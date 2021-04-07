package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.HistoryFieldsRepository;
import com.automation.professional.service.HistoryFieldsService;
import com.automation.professional.service.dto.HistoryFieldsDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.HistoryFields}.
 */
@RestController
@RequestMapping("/api")
public class HistoryFieldsResource {

    private final Logger log = LoggerFactory.getLogger(HistoryFieldsResource.class);

    private static final String ENTITY_NAME = "historyFields";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoryFieldsService historyFieldsService;

    private final HistoryFieldsRepository historyFieldsRepository;

    public HistoryFieldsResource(HistoryFieldsService historyFieldsService, HistoryFieldsRepository historyFieldsRepository) {
        this.historyFieldsService = historyFieldsService;
        this.historyFieldsRepository = historyFieldsRepository;
    }

    /**
     * {@code POST  /history-fields} : Create a new historyFields.
     *
     * @param historyFieldsDTO the historyFieldsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historyFieldsDTO, or with status {@code 400 (Bad Request)} if the historyFields has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/history-fields")
    public ResponseEntity<HistoryFieldsDTO> createHistoryFields(@RequestBody HistoryFieldsDTO historyFieldsDTO) throws URISyntaxException {
        log.debug("REST request to save HistoryFields : {}", historyFieldsDTO);
        if (historyFieldsDTO.getId() != null) {
            throw new BadRequestAlertException("A new historyFields cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoryFieldsDTO result = historyFieldsService.save(historyFieldsDTO);
        return ResponseEntity
            .created(new URI("/api/history-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /history-fields/:id} : Updates an existing historyFields.
     *
     * @param id the id of the historyFieldsDTO to save.
     * @param historyFieldsDTO the historyFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historyFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the historyFieldsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historyFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/history-fields/{id}")
    public ResponseEntity<HistoryFieldsDTO> updateHistoryFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HistoryFieldsDTO historyFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HistoryFields : {}, {}", id, historyFieldsDTO);
        if (historyFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historyFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historyFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HistoryFieldsDTO result = historyFieldsService.save(historyFieldsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historyFieldsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /history-fields/:id} : Partial updates given fields of an existing historyFields, field will ignore if it is null
     *
     * @param id the id of the historyFieldsDTO to save.
     * @param historyFieldsDTO the historyFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historyFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the historyFieldsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historyFieldsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historyFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/history-fields/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HistoryFieldsDTO> partialUpdateHistoryFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HistoryFieldsDTO historyFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HistoryFields partially : {}, {}", id, historyFieldsDTO);
        if (historyFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historyFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historyFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoryFieldsDTO> result = historyFieldsService.partialUpdate(historyFieldsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historyFieldsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /history-fields} : get all the historyFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historyFields in body.
     */
    @GetMapping("/history-fields")
    public ResponseEntity<List<HistoryFieldsDTO>> getAllHistoryFields(Pageable pageable) {
        log.debug("REST request to get a page of HistoryFields");
        Page<HistoryFieldsDTO> page = historyFieldsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /history-fields/:id} : get the "id" historyFields.
     *
     * @param id the id of the historyFieldsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historyFieldsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/history-fields/{id}")
    public ResponseEntity<HistoryFieldsDTO> getHistoryFields(@PathVariable Long id) {
        log.debug("REST request to get HistoryFields : {}", id);
        Optional<HistoryFieldsDTO> historyFieldsDTO = historyFieldsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historyFieldsDTO);
    }

    /**
     * {@code DELETE  /history-fields/:id} : delete the "id" historyFields.
     *
     * @param id the id of the historyFieldsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/history-fields/{id}")
    public ResponseEntity<Void> deleteHistoryFields(@PathVariable Long id) {
        log.debug("REST request to delete HistoryFields : {}", id);
        historyFieldsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/history-fields?query=:query} : search for the historyFields corresponding
     * to the query.
     *
     * @param query the query of the historyFields search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/history-fields")
    public ResponseEntity<List<HistoryFieldsDTO>> searchHistoryFields(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of HistoryFields for query {}", query);
        Page<HistoryFieldsDTO> page = historyFieldsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
