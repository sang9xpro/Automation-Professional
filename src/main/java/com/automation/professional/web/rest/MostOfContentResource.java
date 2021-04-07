package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.MostOfContentRepository;
import com.automation.professional.service.MostOfContentService;
import com.automation.professional.service.dto.MostOfContentDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.MostOfContent}.
 */
@RestController
@RequestMapping("/api")
public class MostOfContentResource {

    private final Logger log = LoggerFactory.getLogger(MostOfContentResource.class);

    private static final String ENTITY_NAME = "mostOfContent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MostOfContentService mostOfContentService;

    private final MostOfContentRepository mostOfContentRepository;

    public MostOfContentResource(MostOfContentService mostOfContentService, MostOfContentRepository mostOfContentRepository) {
        this.mostOfContentService = mostOfContentService;
        this.mostOfContentRepository = mostOfContentRepository;
    }

    /**
     * {@code POST  /most-of-contents} : Create a new mostOfContent.
     *
     * @param mostOfContentDTO the mostOfContentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mostOfContentDTO, or with status {@code 400 (Bad Request)} if the mostOfContent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/most-of-contents")
    public ResponseEntity<MostOfContentDTO> createMostOfContent(@RequestBody MostOfContentDTO mostOfContentDTO) throws URISyntaxException {
        log.debug("REST request to save MostOfContent : {}", mostOfContentDTO);
        if (mostOfContentDTO.getId() != null) {
            throw new BadRequestAlertException("A new mostOfContent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MostOfContentDTO result = mostOfContentService.save(mostOfContentDTO);
        return ResponseEntity
            .created(new URI("/api/most-of-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /most-of-contents/:id} : Updates an existing mostOfContent.
     *
     * @param id the id of the mostOfContentDTO to save.
     * @param mostOfContentDTO the mostOfContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mostOfContentDTO,
     * or with status {@code 400 (Bad Request)} if the mostOfContentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mostOfContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/most-of-contents/{id}")
    public ResponseEntity<MostOfContentDTO> updateMostOfContent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MostOfContentDTO mostOfContentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MostOfContent : {}, {}", id, mostOfContentDTO);
        if (mostOfContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mostOfContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mostOfContentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MostOfContentDTO result = mostOfContentService.save(mostOfContentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mostOfContentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /most-of-contents/:id} : Partial updates given fields of an existing mostOfContent, field will ignore if it is null
     *
     * @param id the id of the mostOfContentDTO to save.
     * @param mostOfContentDTO the mostOfContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mostOfContentDTO,
     * or with status {@code 400 (Bad Request)} if the mostOfContentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mostOfContentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mostOfContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/most-of-contents/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MostOfContentDTO> partialUpdateMostOfContent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MostOfContentDTO mostOfContentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MostOfContent partially : {}, {}", id, mostOfContentDTO);
        if (mostOfContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mostOfContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mostOfContentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MostOfContentDTO> result = mostOfContentService.partialUpdate(mostOfContentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mostOfContentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /most-of-contents} : get all the mostOfContents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mostOfContents in body.
     */
    @GetMapping("/most-of-contents")
    public ResponseEntity<List<MostOfContentDTO>> getAllMostOfContents(Pageable pageable) {
        log.debug("REST request to get a page of MostOfContents");
        Page<MostOfContentDTO> page = mostOfContentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /most-of-contents/:id} : get the "id" mostOfContent.
     *
     * @param id the id of the mostOfContentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mostOfContentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/most-of-contents/{id}")
    public ResponseEntity<MostOfContentDTO> getMostOfContent(@PathVariable Long id) {
        log.debug("REST request to get MostOfContent : {}", id);
        Optional<MostOfContentDTO> mostOfContentDTO = mostOfContentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mostOfContentDTO);
    }

    /**
     * {@code DELETE  /most-of-contents/:id} : delete the "id" mostOfContent.
     *
     * @param id the id of the mostOfContentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/most-of-contents/{id}")
    public ResponseEntity<Void> deleteMostOfContent(@PathVariable Long id) {
        log.debug("REST request to delete MostOfContent : {}", id);
        mostOfContentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/most-of-contents?query=:query} : search for the mostOfContent corresponding
     * to the query.
     *
     * @param query the query of the mostOfContent search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/most-of-contents")
    public ResponseEntity<List<MostOfContentDTO>> searchMostOfContents(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MostOfContents for query {}", query);
        Page<MostOfContentDTO> page = mostOfContentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
