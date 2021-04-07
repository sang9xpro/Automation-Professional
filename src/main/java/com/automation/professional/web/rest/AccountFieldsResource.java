package com.automation.professional.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.repository.AccountFieldsRepository;
import com.automation.professional.service.AccountFieldsService;
import com.automation.professional.service.dto.AccountFieldsDTO;
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
 * REST controller for managing {@link com.automation.professional.domain.AccountFields}.
 */
@RestController
@RequestMapping("/api")
public class AccountFieldsResource {

    private final Logger log = LoggerFactory.getLogger(AccountFieldsResource.class);

    private static final String ENTITY_NAME = "accountFields";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountFieldsService accountFieldsService;

    private final AccountFieldsRepository accountFieldsRepository;

    public AccountFieldsResource(AccountFieldsService accountFieldsService, AccountFieldsRepository accountFieldsRepository) {
        this.accountFieldsService = accountFieldsService;
        this.accountFieldsRepository = accountFieldsRepository;
    }

    /**
     * {@code POST  /account-fields} : Create a new accountFields.
     *
     * @param accountFieldsDTO the accountFieldsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountFieldsDTO, or with status {@code 400 (Bad Request)} if the accountFields has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-fields")
    public ResponseEntity<AccountFieldsDTO> createAccountFields(@RequestBody AccountFieldsDTO accountFieldsDTO) throws URISyntaxException {
        log.debug("REST request to save AccountFields : {}", accountFieldsDTO);
        if (accountFieldsDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountFields cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountFieldsDTO result = accountFieldsService.save(accountFieldsDTO);
        return ResponseEntity
            .created(new URI("/api/account-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-fields/:id} : Updates an existing accountFields.
     *
     * @param id the id of the accountFieldsDTO to save.
     * @param accountFieldsDTO the accountFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the accountFieldsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-fields/{id}")
    public ResponseEntity<AccountFieldsDTO> updateAccountFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountFieldsDTO accountFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccountFields : {}, {}", id, accountFieldsDTO);
        if (accountFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountFieldsDTO result = accountFieldsService.save(accountFieldsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountFieldsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-fields/:id} : Partial updates given fields of an existing accountFields, field will ignore if it is null
     *
     * @param id the id of the accountFieldsDTO to save.
     * @param accountFieldsDTO the accountFieldsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountFieldsDTO,
     * or with status {@code 400 (Bad Request)} if the accountFieldsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountFieldsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountFieldsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-fields/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AccountFieldsDTO> partialUpdateAccountFields(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountFieldsDTO accountFieldsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountFields partially : {}, {}", id, accountFieldsDTO);
        if (accountFieldsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountFieldsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountFieldsDTO> result = accountFieldsService.partialUpdate(accountFieldsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountFieldsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /account-fields} : get all the accountFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountFields in body.
     */
    @GetMapping("/account-fields")
    public ResponseEntity<List<AccountFieldsDTO>> getAllAccountFields(Pageable pageable) {
        log.debug("REST request to get a page of AccountFields");
        Page<AccountFieldsDTO> page = accountFieldsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-fields/:id} : get the "id" accountFields.
     *
     * @param id the id of the accountFieldsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountFieldsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-fields/{id}")
    public ResponseEntity<AccountFieldsDTO> getAccountFields(@PathVariable Long id) {
        log.debug("REST request to get AccountFields : {}", id);
        Optional<AccountFieldsDTO> accountFieldsDTO = accountFieldsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountFieldsDTO);
    }

    /**
     * {@code DELETE  /account-fields/:id} : delete the "id" accountFields.
     *
     * @param id the id of the accountFieldsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-fields/{id}")
    public ResponseEntity<Void> deleteAccountFields(@PathVariable Long id) {
        log.debug("REST request to delete AccountFields : {}", id);
        accountFieldsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/account-fields?query=:query} : search for the accountFields corresponding
     * to the query.
     *
     * @param query the query of the accountFields search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/account-fields")
    public ResponseEntity<List<AccountFieldsDTO>> searchAccountFields(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AccountFields for query {}", query);
        Page<AccountFieldsDTO> page = accountFieldsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
