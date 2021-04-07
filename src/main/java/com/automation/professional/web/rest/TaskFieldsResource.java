package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.TaskFieldsRepository;
import com.automation.professional.service.TaskFieldsService;
import com.automation.professional.service.dto.TaskFieldsDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.TaskFields}.
 */
@RestController
@RequestMapping("/api")
public class TaskFieldsResource {

    private final Logger log = LoggerFactory.getLogger(TaskFieldsResource.class);

    private static final String ENTITY_NAME = "taskFields";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskFieldsService taskFieldsService;

    private final TaskFieldsRepository taskFieldsRepository;

    public TaskFieldsResource(TaskFieldsService taskFieldsService, TaskFieldsRepository taskFieldsRepository) {
        this.taskFieldsService = taskFieldsService;
        this.taskFieldsRepository = taskFieldsRepository;
    }

    /**
     * {@code POST  /task-fields} : Create a new taskFields.
     *
     * @param taskFieldsDTO the taskFieldsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskFieldsDTO, or with status {@code 400 (Bad Request)} if the taskFields has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/task-fields")
    public ResponseEntity<TaskFieldsDTO> createTaskFields(@RequestBody TaskFieldsDTO taskFieldsDTO) throws URISyntaxException {
        log.debug("REST request to save TaskFields : {}", taskFieldsDTO);
        if (taskFieldsDTO.getId() != null) {
            throw new BadRequestAlertException("A new taskFields cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskFieldsDTO result = taskFieldsService.save(taskFieldsDTO);
        return ResponseEntity
            .created(new URI("/api/task-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /task-fields/:id} : Updates an existing taskFields.
     *
     * @param id the id of the taskFieldsDTO to save.
     * @param taskFieldsDTO the taskFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the taskFieldsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/task-fields/{id}")
    public ResponseEntity<TaskFieldsDTO> updateTaskFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskFieldsDTO taskFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaskFields : {}, {}", id, taskFieldsDTO);
        if (taskFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaskFieldsDTO result = taskFieldsService.save(taskFieldsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskFieldsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /task-fields/:id} : Partial updates given fields of an existing taskFields, field will ignore if it is null
     *
     * @param id the id of the taskFieldsDTO to save.
     * @param taskFieldsDTO the taskFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the taskFieldsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taskFieldsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/task-fields/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TaskFieldsDTO> partialUpdateTaskFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskFieldsDTO taskFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaskFields partially : {}, {}", id, taskFieldsDTO);
        if (taskFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskFieldsDTO> result = taskFieldsService.partialUpdate(taskFieldsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskFieldsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /task-fields} : get all the taskFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskFields in body.
     */
    @GetMapping("/task-fields")
    public ResponseEntity<List<TaskFieldsDTO>> getAllTaskFields(Pageable pageable) {
        log.debug("REST request to get a page of TaskFields");
        Page<TaskFieldsDTO> page = taskFieldsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /task-fields/:id} : get the "id" taskFields.
     *
     * @param id the id of the taskFieldsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskFieldsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/task-fields/{id}")
    public ResponseEntity<TaskFieldsDTO> getTaskFields(@PathVariable Long id) {
        log.debug("REST request to get TaskFields : {}", id);
        Optional<TaskFieldsDTO> taskFieldsDTO = taskFieldsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskFieldsDTO);
    }

    /**
     * {@code DELETE  /task-fields/:id} : delete the "id" taskFields.
     *
     * @param id the id of the taskFieldsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/task-fields/{id}")
    public ResponseEntity<Void> deleteTaskFields(@PathVariable Long id) {
        log.debug("REST request to delete TaskFields : {}", id);
        taskFieldsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/task-fields?query=:query} : search for the taskFields corresponding
     * to the query.
     *
     * @param query the query of the taskFields search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/task-fields")
    public ResponseEntity<List<TaskFieldsDTO>> searchTaskFields(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TaskFields for query {}", query);
        Page<TaskFieldsDTO> page = taskFieldsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
