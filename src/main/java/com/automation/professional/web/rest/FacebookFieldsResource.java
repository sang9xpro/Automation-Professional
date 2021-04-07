package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.FacebookFieldsRepository;
import com.automation.professional.service.FacebookFieldsService;
import com.automation.professional.service.dto.FacebookFieldsDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.FacebookFields}.
 */
@RestController
@RequestMapping("/api")
public class FacebookFieldsResource {

    private final Logger log = LoggerFactory.getLogger(FacebookFieldsResource.class);

    private static final String ENTITY_NAME = "facebookFields";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacebookFieldsService facebookFieldsService;

    private final FacebookFieldsRepository facebookFieldsRepository;

    public FacebookFieldsResource(FacebookFieldsService facebookFieldsService, FacebookFieldsRepository facebookFieldsRepository) {
        this.facebookFieldsService = facebookFieldsService;
        this.facebookFieldsRepository = facebookFieldsRepository;
    }

    /**
     * {@code POST  /facebook-fields} : Create a new facebookFields.
     *
     * @param facebookFieldsDTO the facebookFieldsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facebookFieldsDTO, or with status {@code 400 (Bad Request)} if the facebookFields has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facebook-fields")
    public ResponseEntity<FacebookFieldsDTO> createFacebookFields(@RequestBody FacebookFieldsDTO facebookFieldsDTO)
        throws URISyntaxException {
        log.debug("REST request to save FacebookFields : {}", facebookFieldsDTO);
        if (facebookFieldsDTO.getId() != null) {
            throw new BadRequestAlertException("A new facebookFields cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacebookFieldsDTO result = facebookFieldsService.save(facebookFieldsDTO);
        return ResponseEntity
            .created(new URI("/api/facebook-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facebook-fields/:id} : Updates an existing facebookFields.
     *
     * @param id the id of the facebookFieldsDTO to save.
     * @param facebookFieldsDTO the facebookFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facebookFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the facebookFieldsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facebookFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facebook-fields/{id}")
    public ResponseEntity<FacebookFieldsDTO> updateFacebookFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacebookFieldsDTO facebookFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FacebookFields : {}, {}", id, facebookFieldsDTO);
        if (facebookFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facebookFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facebookFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacebookFieldsDTO result = facebookFieldsService.save(facebookFieldsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facebookFieldsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facebook-fields/:id} : Partial updates given fields of an existing facebookFields, field will ignore if it is null
     *
     * @param id the id of the facebookFieldsDTO to save.
     * @param facebookFieldsDTO the facebookFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facebookFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the facebookFieldsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facebookFieldsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facebookFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facebook-fields/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FacebookFieldsDTO> partialUpdateFacebookFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacebookFieldsDTO facebookFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FacebookFields partially : {}, {}", id, facebookFieldsDTO);
        if (facebookFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facebookFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facebookFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacebookFieldsDTO> result = facebookFieldsService.partialUpdate(facebookFieldsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facebookFieldsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facebook-fields} : get all the facebookFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facebookFields in body.
     */
    @GetMapping("/facebook-fields")
    public ResponseEntity<List<FacebookFieldsDTO>> getAllFacebookFields(Pageable pageable) {
        log.debug("REST request to get a page of FacebookFields");
        Page<FacebookFieldsDTO> page = facebookFieldsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facebook-fields/:id} : get the "id" facebookFields.
     *
     * @param id the id of the facebookFieldsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facebookFieldsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facebook-fields/{id}")
    public ResponseEntity<FacebookFieldsDTO> getFacebookFields(@PathVariable Long id) {
        log.debug("REST request to get FacebookFields : {}", id);
        Optional<FacebookFieldsDTO> facebookFieldsDTO = facebookFieldsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facebookFieldsDTO);
    }

    /**
     * {@code DELETE  /facebook-fields/:id} : delete the "id" facebookFields.
     *
     * @param id the id of the facebookFieldsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facebook-fields/{id}")
    public ResponseEntity<Void> deleteFacebookFields(@PathVariable Long id) {
        log.debug("REST request to delete FacebookFields : {}", id);
        facebookFieldsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/facebook-fields?query=:query} : search for the facebookFields corresponding
     * to the query.
     *
     * @param query the query of the facebookFields search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/facebook-fields")
    public ResponseEntity<List<FacebookFieldsDTO>> searchFacebookFields(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FacebookFields for query {}", query);
        Page<FacebookFieldsDTO> page = facebookFieldsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
