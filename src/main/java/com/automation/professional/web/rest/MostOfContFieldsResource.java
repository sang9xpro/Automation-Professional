package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.MostOfContFieldsRepository;
import com.automation.professional.service.MostOfContFieldsService;
import com.automation.professional.service.dto.MostOfContFieldsDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.MostOfContFields}.
 */
@RestController
@RequestMapping("/api")
public class MostOfContFieldsResource {

    private final Logger log = LoggerFactory.getLogger(MostOfContFieldsResource.class);

    private static final String ENTITY_NAME = "mostOfContFields";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MostOfContFieldsService mostOfContFieldsService;

    private final MostOfContFieldsRepository mostOfContFieldsRepository;

    public MostOfContFieldsResource(
        MostOfContFieldsService mostOfContFieldsService,
        MostOfContFieldsRepository mostOfContFieldsRepository
    ) {
        this.mostOfContFieldsService = mostOfContFieldsService;
        this.mostOfContFieldsRepository = mostOfContFieldsRepository;
    }

    /**
     * {@code POST  /most-of-cont-fields} : Create a new mostOfContFields.
     *
     * @param mostOfContFieldsDTO the mostOfContFieldsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mostOfContFieldsDTO, or with status {@code 400 (Bad Request)} if the mostOfContFields has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/most-of-cont-fields")
    public ResponseEntity<MostOfContFieldsDTO> createMostOfContFields(@RequestBody MostOfContFieldsDTO mostOfContFieldsDTO)
        throws URISyntaxException {
        log.debug("REST request to save MostOfContFields : {}", mostOfContFieldsDTO);
        if (mostOfContFieldsDTO.getId() != null) {
            throw new BadRequestAlertException("A new mostOfContFields cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MostOfContFieldsDTO result = mostOfContFieldsService.save(mostOfContFieldsDTO);
        return ResponseEntity
            .created(new URI("/api/most-of-cont-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /most-of-cont-fields/:id} : Updates an existing mostOfContFields.
     *
     * @param id the id of the mostOfContFieldsDTO to save.
     * @param mostOfContFieldsDTO the mostOfContFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mostOfContFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the mostOfContFieldsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mostOfContFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/most-of-cont-fields/{id}")
    public ResponseEntity<MostOfContFieldsDTO> updateMostOfContFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MostOfContFieldsDTO mostOfContFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MostOfContFields : {}, {}", id, mostOfContFieldsDTO);
        if (mostOfContFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mostOfContFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mostOfContFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MostOfContFieldsDTO result = mostOfContFieldsService.save(mostOfContFieldsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mostOfContFieldsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /most-of-cont-fields/:id} : Partial updates given fields of an existing mostOfContFields, field will ignore if it is null
     *
     * @param id the id of the mostOfContFieldsDTO to save.
     * @param mostOfContFieldsDTO the mostOfContFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mostOfContFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the mostOfContFieldsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mostOfContFieldsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mostOfContFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/most-of-cont-fields/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MostOfContFieldsDTO> partialUpdateMostOfContFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MostOfContFieldsDTO mostOfContFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MostOfContFields partially : {}, {}", id, mostOfContFieldsDTO);
        if (mostOfContFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mostOfContFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mostOfContFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MostOfContFieldsDTO> result = mostOfContFieldsService.partialUpdate(mostOfContFieldsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mostOfContFieldsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /most-of-cont-fields} : get all the mostOfContFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mostOfContFields in body.
     */
    @GetMapping("/most-of-cont-fields")
    public ResponseEntity<List<MostOfContFieldsDTO>> getAllMostOfContFields(Pageable pageable) {
        log.debug("REST request to get a page of MostOfContFields");
        Page<MostOfContFieldsDTO> page = mostOfContFieldsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /most-of-cont-fields/:id} : get the "id" mostOfContFields.
     *
     * @param id the id of the mostOfContFieldsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mostOfContFieldsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/most-of-cont-fields/{id}")
    public ResponseEntity<MostOfContFieldsDTO> getMostOfContFields(@PathVariable Long id) {
        log.debug("REST request to get MostOfContFields : {}", id);
        Optional<MostOfContFieldsDTO> mostOfContFieldsDTO = mostOfContFieldsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mostOfContFieldsDTO);
    }

    /**
     * {@code DELETE  /most-of-cont-fields/:id} : delete the "id" mostOfContFields.
     *
     * @param id the id of the mostOfContFieldsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/most-of-cont-fields/{id}")
    public ResponseEntity<Void> deleteMostOfContFields(@PathVariable Long id) {
        log.debug("REST request to delete MostOfContFields : {}", id);
        mostOfContFieldsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/most-of-cont-fields?query=:query} : search for the mostOfContFields corresponding
     * to the query.
     *
     * @param query the query of the mostOfContFields search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/most-of-cont-fields")
    public ResponseEntity<List<MostOfContFieldsDTO>> searchMostOfContFields(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MostOfContFields for query {}", query);
        Page<MostOfContFieldsDTO> page = mostOfContFieldsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
