package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.CommentFieldsRepository;
import com.automation.professional.service.CommentFieldsService;
import com.automation.professional.service.dto.CommentFieldsDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.CommentFields}.
 */
@RestController
@RequestMapping("/api")
public class CommentFieldsResource {

    private final Logger log = LoggerFactory.getLogger(CommentFieldsResource.class);

    private static final String ENTITY_NAME = "commentFields";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentFieldsService commentFieldsService;

    private final CommentFieldsRepository commentFieldsRepository;

    public CommentFieldsResource(CommentFieldsService commentFieldsService, CommentFieldsRepository commentFieldsRepository) {
        this.commentFieldsService = commentFieldsService;
        this.commentFieldsRepository = commentFieldsRepository;
    }

    /**
     * {@code POST  /comment-fields} : Create a new commentFields.
     *
     * @param commentFieldsDTO the commentFieldsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentFieldsDTO, or with status {@code 400 (Bad Request)} if the commentFields has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comment-fields")
    public ResponseEntity<CommentFieldsDTO> createCommentFields(@RequestBody CommentFieldsDTO commentFieldsDTO) throws URISyntaxException {
        log.debug("REST request to save CommentFields : {}", commentFieldsDTO);
        if (commentFieldsDTO.getId() != null) {
            throw new BadRequestAlertException("A new commentFields cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentFieldsDTO result = commentFieldsService.save(commentFieldsDTO);
        return ResponseEntity
            .created(new URI("/api/comment-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comment-fields/:id} : Updates an existing commentFields.
     *
     * @param id the id of the commentFieldsDTO to save.
     * @param commentFieldsDTO the commentFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the commentFieldsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comment-fields/{id}")
    public ResponseEntity<CommentFieldsDTO> updateCommentFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommentFieldsDTO commentFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommentFields : {}, {}", id, commentFieldsDTO);
        if (commentFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommentFieldsDTO result = commentFieldsService.save(commentFieldsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentFieldsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comment-fields/:id} : Partial updates given fields of an existing commentFields, field will ignore if it is null
     *
     * @param id the id of the commentFieldsDTO to save.
     * @param commentFieldsDTO the commentFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the commentFieldsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commentFieldsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commentFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comment-fields/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CommentFieldsDTO> partialUpdateCommentFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommentFieldsDTO commentFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommentFields partially : {}, {}", id, commentFieldsDTO);
        if (commentFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommentFieldsDTO> result = commentFieldsService.partialUpdate(commentFieldsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentFieldsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /comment-fields} : get all the commentFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commentFields in body.
     */
    @GetMapping("/comment-fields")
    public ResponseEntity<List<CommentFieldsDTO>> getAllCommentFields(Pageable pageable) {
        log.debug("REST request to get a page of CommentFields");
        Page<CommentFieldsDTO> page = commentFieldsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comment-fields/:id} : get the "id" commentFields.
     *
     * @param id the id of the commentFieldsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commentFieldsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comment-fields/{id}")
    public ResponseEntity<CommentFieldsDTO> getCommentFields(@PathVariable Long id) {
        log.debug("REST request to get CommentFields : {}", id);
        Optional<CommentFieldsDTO> commentFieldsDTO = commentFieldsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentFieldsDTO);
    }

    /**
     * {@code DELETE  /comment-fields/:id} : delete the "id" commentFields.
     *
     * @param id the id of the commentFieldsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comment-fields/{id}")
    public ResponseEntity<Void> deleteCommentFields(@PathVariable Long id) {
        log.debug("REST request to delete CommentFields : {}", id);
        commentFieldsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/comment-fields?query=:query} : search for the commentFields corresponding
     * to the query.
     *
     * @param query the query of the commentFields search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/comment-fields")
    public ResponseEntity<List<CommentFieldsDTO>> searchCommentFields(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CommentFields for query {}", query);
        Page<CommentFieldsDTO> page = commentFieldsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
