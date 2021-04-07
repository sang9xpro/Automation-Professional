package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.AccountsRepository;
import com.automation.professional.service.AccountsService;
import com.automation.professional.service.dto.AccountsDTO;
import com.automation.professional.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.automation.professional.domain.Accounts}.
 */
@RestController
@RequestMapping("/api")
public class AccountsResource {

    private final Logger log = LoggerFactory.getLogger(AccountsResource.class);

    private static final String ENTITY_NAME = "accounts";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountsService accountsService;

    private final AccountsRepository accountsRepository;

    public AccountsResource(AccountsService accountsService, AccountsRepository accountsRepository) {
        this.accountsService = accountsService;
        this.accountsRepository = accountsRepository;
    }

    /**
     * {@code POST  /accounts} : Create a new accounts.
     *
     * @param accountsDTO the accountsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountsDTO, or with status {@code 400 (Bad Request)} if the accounts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/accounts")
    public ResponseEntity<AccountsDTO> createAccounts(@Valid @RequestBody AccountsDTO accountsDTO) throws URISyntaxException {
        log.debug("REST request to save Accounts : {}", accountsDTO);
        if (accountsDTO.getId() != null) {
            throw new BadRequestAlertException("A new accounts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountsDTO result = accountsService.save(accountsDTO);
        return ResponseEntity
            .created(new URI("/api/accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /accounts/:id} : Updates an existing accounts.
     *
     * @param id the id of the accountsDTO to save.
     * @param accountsDTO the accountsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountsDTO,
     * or with status {@code 400 (Bad Request)} if the accountsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/accounts/{id}")
    public ResponseEntity<AccountsDTO> updateAccounts(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountsDTO accountsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Accounts : {}, {}", id, accountsDTO);
        if (accountsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountsDTO result = accountsService.save(accountsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /accounts/:id} : Partial updates given fields of an existing accounts, field will ignore if it is null
     *
     * @param id the id of the accountsDTO to save.
     * @param accountsDTO the accountsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountsDTO,
     * or with status {@code 400 (Bad Request)} if the accountsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/accounts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AccountsDTO> partialUpdateAccounts(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountsDTO accountsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Accounts partially : {}, {}", id, accountsDTO);
        if (accountsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountsDTO> result = accountsService.partialUpdate(accountsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /accounts} : get all the accounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accounts in body.
     */
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountsDTO>> getAllAccounts(Pageable pageable) {
        log.debug("REST request to get a page of Accounts");
        Page<AccountsDTO> page = accountsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /accounts/:id} : get the "id" accounts.
     *
     * @param id the id of the accountsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountsDTO> getAccounts(@PathVariable Long id) {
        log.debug("REST request to get Accounts : {}", id);
        Optional<AccountsDTO> accountsDTO = accountsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountsDTO);
    }

    /**
     * {@code DELETE  /accounts/:id} : delete the "id" accounts.
     *
     * @param id the id of the accountsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> deleteAccounts(@PathVariable Long id) {
        log.debug("REST request to delete Accounts : {}", id);
        accountsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/accounts?query=:query} : search for the accounts corresponding
     * to the query.
     *
     * @param query the query of the accounts search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/accounts")
    public ResponseEntity<List<AccountsDTO>> searchAccounts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Accounts for query {}", query);
        Page<AccountsDTO> page = accountsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
