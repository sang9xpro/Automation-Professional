package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.FacebookRepository;
import com.automation.professional.service.FacebookService;
import com.automation.professional.service.dto.FacebookDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.Facebook}.
 */
@RestController
@RequestMapping("/api")
public class FacebookResource {

    private final Logger log = LoggerFactory.getLogger(FacebookResource.class);

    private static final String ENTITY_NAME = "facebook";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacebookService facebookService;

    private final FacebookRepository facebookRepository;

    public FacebookResource(FacebookService facebookService, FacebookRepository facebookRepository) {
        this.facebookService = facebookService;
        this.facebookRepository = facebookRepository;
    }

    /**
     * {@code POST  /facebooks} : Create a new facebook.
     *
     * @param facebookDTO the facebookDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facebookDTO, or with status {@code 400 (Bad Request)} if the facebook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facebooks")
    public ResponseEntity<FacebookDTO> createFacebook(@RequestBody FacebookDTO facebookDTO) throws URISyntaxException {
        log.debug("REST request to save Facebook : {}", facebookDTO);
        if (facebookDTO.getId() != null) {
            throw new BadRequestAlertException("A new facebook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacebookDTO result = facebookService.save(facebookDTO);
        return ResponseEntity
            .created(new URI("/api/facebooks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facebooks/:id} : Updates an existing facebook.
     *
     * @param id the id of the facebookDTO to save.
     * @param facebookDTO the facebookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facebookDTO,
     * or with status {@code 400 (Bad Request)} if the facebookDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facebookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facebooks/{id}")
    public ResponseEntity<FacebookDTO> updateFacebook(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacebookDTO facebookDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Facebook : {}, {}", id, facebookDTO);
        if (facebookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facebookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facebookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacebookDTO result = facebookService.save(facebookDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facebookDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facebooks/:id} : Partial updates given fields of an existing facebook, field will ignore if it is null
     *
     * @param id the id of the facebookDTO to save.
     * @param facebookDTO the facebookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facebookDTO,
     * or with status {@code 400 (Bad Request)} if the facebookDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facebookDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facebookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facebooks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FacebookDTO> partialUpdateFacebook(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacebookDTO facebookDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Facebook partially : {}, {}", id, facebookDTO);
        if (facebookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facebookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facebookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacebookDTO> result = facebookService.partialUpdate(facebookDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facebookDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facebooks} : get all the facebooks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facebooks in body.
     */
    @GetMapping("/facebooks")
    public ResponseEntity<List<FacebookDTO>> getAllFacebooks(Pageable pageable) {
        log.debug("REST request to get a page of Facebooks");
        Page<FacebookDTO> page = facebookService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facebooks/:id} : get the "id" facebook.
     *
     * @param id the id of the facebookDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facebookDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facebooks/{id}")
    public ResponseEntity<FacebookDTO> getFacebook(@PathVariable Long id) {
        log.debug("REST request to get Facebook : {}", id);
        Optional<FacebookDTO> facebookDTO = facebookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facebookDTO);
    }

    /**
     * {@code DELETE  /facebooks/:id} : delete the "id" facebook.
     *
     * @param id the id of the facebookDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facebooks/{id}")
    public ResponseEntity<Void> deleteFacebook(@PathVariable Long id) {
        log.debug("REST request to delete Facebook : {}", id);
        facebookService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/facebooks?query=:query} : search for the facebook corresponding
     * to the query.
     *
     * @param query the query of the facebook search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/facebooks")
    public ResponseEntity<List<FacebookDTO>> searchFacebooks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Facebooks for query {}", query);
        Page<FacebookDTO> page = facebookService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
