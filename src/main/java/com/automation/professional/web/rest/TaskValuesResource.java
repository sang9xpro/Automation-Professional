package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.TaskValuesRepository;
import com.automation.professional.service.TaskValuesService;
import com.automation.professional.service.dto.TaskValuesDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.TaskValues}.
 */
@RestController
@RequestMapping("/api")
public class TaskValuesResource {

    private final Logger log = LoggerFactory.getLogger(TaskValuesResource.class);

    private static final String ENTITY_NAME = "taskValues";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskValuesService taskValuesService;

    private final TaskValuesRepository taskValuesRepository;

    public TaskValuesResource(TaskValuesService taskValuesService, TaskValuesRepository taskValuesRepository) {
        this.taskValuesService = taskValuesService;
        this.taskValuesRepository = taskValuesRepository;
    }

    /**
     * {@code POST  /task-values} : Create a new taskValues.
     *
     * @param taskValuesDTO the taskValuesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskValuesDTO, or with status {@code 400 (Bad Request)} if the taskValues has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/task-values")
    public ResponseEntity<TaskValuesDTO> createTaskValues(@RequestBody TaskValuesDTO taskValuesDTO) throws URISyntaxException {
        log.debug("REST request to save TaskValues : {}", taskValuesDTO);
        if (taskValuesDTO.getId() != null) {
            throw new BadRequestAlertException("A new taskValues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskValuesDTO result = taskValuesService.save(taskValuesDTO);
        return ResponseEntity
            .created(new URI("/api/task-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /task-values/:id} : Updates an existing taskValues.
     *
     * @param id the id of the taskValuesDTO to save.
     * @param taskValuesDTO the taskValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskValuesDTO,
     * or with status {@code 400 (Bad Request)} if the taskValuesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/task-values/{id}")
    public ResponseEntity<TaskValuesDTO> updateTaskValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskValuesDTO taskValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaskValues : {}, {}", id, taskValuesDTO);
        if (taskValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaskValuesDTO result = taskValuesService.save(taskValuesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskValuesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /task-values/:id} : Partial updates given fields of an existing taskValues, field will ignore if it is null
     *
     * @param id the id of the taskValuesDTO to save.
     * @param taskValuesDTO the taskValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskValuesDTO,
     * or with status {@code 400 (Bad Request)} if the taskValuesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taskValuesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/task-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TaskValuesDTO> partialUpdateTaskValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskValuesDTO taskValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaskValues partially : {}, {}", id, taskValuesDTO);
        if (taskValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskValuesDTO> result = taskValuesService.partialUpdate(taskValuesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskValuesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /task-values} : get all the taskValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskValues in body.
     */
    @GetMapping("/task-values")
    public ResponseEntity<List<TaskValuesDTO>> getAllTaskValues(Pageable pageable) {
        log.debug("REST request to get a page of TaskValues");
        Page<TaskValuesDTO> page = taskValuesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /task-values/:id} : get the "id" taskValues.
     *
     * @param id the id of the taskValuesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskValuesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/task-values/{id}")
    public ResponseEntity<TaskValuesDTO> getTaskValues(@PathVariable Long id) {
        log.debug("REST request to get TaskValues : {}", id);
        Optional<TaskValuesDTO> taskValuesDTO = taskValuesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskValuesDTO);
    }

    /**
     * {@code DELETE  /task-values/:id} : delete the "id" taskValues.
     *
     * @param id the id of the taskValuesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/task-values/{id}")
    public ResponseEntity<Void> deleteTaskValues(@PathVariable Long id) {
        log.debug("REST request to delete TaskValues : {}", id);
        taskValuesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/task-values?query=:query} : search for the taskValues corresponding
     * to the query.
     *
     * @param query the query of the taskValues search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/task-values")
    public ResponseEntity<List<TaskValuesDTO>> searchTaskValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TaskValues for query {}", query);
        Page<TaskValuesDTO> page = taskValuesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
