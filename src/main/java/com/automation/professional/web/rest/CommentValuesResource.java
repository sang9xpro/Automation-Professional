package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.CommentValuesRepository;
import com.automation.professional.service.CommentValuesService;
import com.automation.professional.service.dto.CommentValuesDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.CommentValues}.
 */
@RestController
@RequestMapping("/api")
public class CommentValuesResource {

    private final Logger log = LoggerFactory.getLogger(CommentValuesResource.class);

    private static final String ENTITY_NAME = "commentValues";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentValuesService commentValuesService;

    private final CommentValuesRepository commentValuesRepository;

    public CommentValuesResource(CommentValuesService commentValuesService, CommentValuesRepository commentValuesRepository) {
        this.commentValuesService = commentValuesService;
        this.commentValuesRepository = commentValuesRepository;
    }

    /**
     * {@code POST  /comment-values} : Create a new commentValues.
     *
     * @param commentValuesDTO the commentValuesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentValuesDTO, or with status {@code 400 (Bad Request)} if the commentValues has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comment-values")
    public ResponseEntity<CommentValuesDTO> createCommentValues(@RequestBody CommentValuesDTO commentValuesDTO) throws URISyntaxException {
        log.debug("REST request to save CommentValues : {}", commentValuesDTO);
        if (commentValuesDTO.getId() != null) {
            throw new BadRequestAlertException("A new commentValues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentValuesDTO result = commentValuesService.save(commentValuesDTO);
        return ResponseEntity
            .created(new URI("/api/comment-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comment-values/:id} : Updates an existing commentValues.
     *
     * @param id the id of the commentValuesDTO to save.
     * @param commentValuesDTO the commentValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentValuesDTO,
     * or with status {@code 400 (Bad Request)} if the commentValuesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comment-values/{id}")
    public ResponseEntity<CommentValuesDTO> updateCommentValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommentValuesDTO commentValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommentValues : {}, {}", id, commentValuesDTO);
        if (commentValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommentValuesDTO result = commentValuesService.save(commentValuesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentValuesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comment-values/:id} : Partial updates given fields of an existing commentValues, field will ignore if it is null
     *
     * @param id the id of the commentValuesDTO to save.
     * @param commentValuesDTO the commentValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentValuesDTO,
     * or with status {@code 400 (Bad Request)} if the commentValuesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commentValuesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commentValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comment-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CommentValuesDTO> partialUpdateCommentValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommentValuesDTO commentValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommentValues partially : {}, {}", id, commentValuesDTO);
        if (commentValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommentValuesDTO> result = commentValuesService.partialUpdate(commentValuesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentValuesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /comment-values} : get all the commentValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commentValues in body.
     */
    @GetMapping("/comment-values")
    public ResponseEntity<List<CommentValuesDTO>> getAllCommentValues(Pageable pageable) {
        log.debug("REST request to get a page of CommentValues");
        Page<CommentValuesDTO> page = commentValuesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comment-values/:id} : get the "id" commentValues.
     *
     * @param id the id of the commentValuesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commentValuesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comment-values/{id}")
    public ResponseEntity<CommentValuesDTO> getCommentValues(@PathVariable Long id) {
        log.debug("REST request to get CommentValues : {}", id);
        Optional<CommentValuesDTO> commentValuesDTO = commentValuesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentValuesDTO);
    }

    /**
     * {@code DELETE  /comment-values/:id} : delete the "id" commentValues.
     *
     * @param id the id of the commentValuesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comment-values/{id}")
    public ResponseEntity<Void> deleteCommentValues(@PathVariable Long id) {
        log.debug("REST request to delete CommentValues : {}", id);
        commentValuesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/comment-values?query=:query} : search for the commentValues corresponding
     * to the query.
     *
     * @param query the query of the commentValues search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/comment-values")
    public ResponseEntity<List<CommentValuesDTO>> searchCommentValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CommentValues for query {}", query);
        Page<CommentValuesDTO> page = commentValuesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
