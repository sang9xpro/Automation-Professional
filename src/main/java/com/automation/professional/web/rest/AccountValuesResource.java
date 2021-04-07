package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.AccountValuesRepository;
import com.automation.professional.service.AccountValuesService;
import com.automation.professional.service.dto.AccountValuesDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.AccountValues}.
 */
@RestController
@RequestMapping("/api")
public class AccountValuesResource {

    private final Logger log = LoggerFactory.getLogger(AccountValuesResource.class);

    private static final String ENTITY_NAME = "accountValues";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountValuesService accountValuesService;

    private final AccountValuesRepository accountValuesRepository;

    public AccountValuesResource(AccountValuesService accountValuesService, AccountValuesRepository accountValuesRepository) {
        this.accountValuesService = accountValuesService;
        this.accountValuesRepository = accountValuesRepository;
    }

    /**
     * {@code POST  /account-values} : Create a new accountValues.
     *
     * @param accountValuesDTO the accountValuesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountValuesDTO, or with status {@code 400 (Bad Request)} if the accountValues has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-values")
    public ResponseEntity<AccountValuesDTO> createAccountValues(@RequestBody AccountValuesDTO accountValuesDTO) throws URISyntaxException {
        log.debug("REST request to save AccountValues : {}", accountValuesDTO);
        if (accountValuesDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountValues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountValuesDTO result = accountValuesService.save(accountValuesDTO);
        return ResponseEntity
            .created(new URI("/api/account-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-values/:id} : Updates an existing accountValues.
     *
     * @param id the id of the accountValuesDTO to save.
     * @param accountValuesDTO the accountValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountValuesDTO,
     * or with status {@code 400 (Bad Request)} if the accountValuesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-values/{id}")
    public ResponseEntity<AccountValuesDTO> updateAccountValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountValuesDTO accountValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccountValues : {}, {}", id, accountValuesDTO);
        if (accountValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountValuesDTO result = accountValuesService.save(accountValuesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountValuesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-values/:id} : Partial updates given fields of an existing accountValues, field will ignore if it is null
     *
     * @param id the id of the accountValuesDTO to save.
     * @param accountValuesDTO the accountValuesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountValuesDTO,
     * or with status {@code 400 (Bad Request)} if the accountValuesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountValuesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountValuesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AccountValuesDTO> partialUpdateAccountValues(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountValuesDTO accountValuesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountValues partially : {}, {}", id, accountValuesDTO);
        if (accountValuesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountValuesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountValuesDTO> result = accountValuesService.partialUpdate(accountValuesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountValuesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /account-values} : get all the accountValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountValues in body.
     */
    @GetMapping("/account-values")
    public ResponseEntity<List<AccountValuesDTO>> getAllAccountValues(Pageable pageable) {
        log.debug("REST request to get a page of AccountValues");
        Page<AccountValuesDTO> page = accountValuesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-values/:id} : get the "id" accountValues.
     *
     * @param id the id of the accountValuesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountValuesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-values/{id}")
    public ResponseEntity<AccountValuesDTO> getAccountValues(@PathVariable Long id) {
        log.debug("REST request to get AccountValues : {}", id);
        Optional<AccountValuesDTO> accountValuesDTO = accountValuesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountValuesDTO);
    }

    /**
     * {@code DELETE  /account-values/:id} : delete the "id" accountValues.
     *
     * @param id the id of the accountValuesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-values/{id}")
    public ResponseEntity<Void> deleteAccountValues(@PathVariable Long id) {
        log.debug("REST request to delete AccountValues : {}", id);
        accountValuesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/account-values?query=:query} : search for the accountValues corresponding
     * to the query.
     *
     * @param query the query of the accountValues search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/account-values")
    public ResponseEntity<List<AccountValuesDTO>> searchAccountValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AccountValues for query {}", query);
        Page<AccountValuesDTO> page = accountValuesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
