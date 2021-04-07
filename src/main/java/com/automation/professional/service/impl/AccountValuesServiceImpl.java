package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.AccountValues;
import com.automation.professional.repository.AccountValuesRepository;
import com.automation.professional.repository.search.AccountValuesSearchRepository;
import com.automation.professional.service.AccountValuesService;
import com.automation.professional.service.dto.AccountValuesDTO;
import com.automation.professional.service.mapper.AccountValuesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AccountValues}.
 */
@Service
@Transactional
public class AccountValuesServiceImpl implements AccountValuesService {

    private final Logger log = LoggerFactory.getLogger(AccountValuesServiceImpl.class);

    private final AccountValuesRepository accountValuesRepository;

    private final AccountValuesMapper accountValuesMapper;

    private final AccountValuesSearchRepository accountValuesSearchRepository;

    public AccountValuesServiceImpl(
        AccountValuesRepository accountValuesRepository,
        AccountValuesMapper accountValuesMapper,
        AccountValuesSearchRepository accountValuesSearchRepository
    ) {
        this.accountValuesRepository = accountValuesRepository;
        this.accountValuesMapper = accountValuesMapper;
        this.accountValuesSearchRepository = accountValuesSearchRepository;
    }

    @Override
    public AccountValuesDTO save(AccountValuesDTO accountValuesDTO) {
        log.debug("Request to save AccountValues : {}", accountValuesDTO);
        AccountValues accountValues = accountValuesMapper.toEntity(accountValuesDTO);
        accountValues = accountValuesRepository.save(accountValues);
        AccountValuesDTO result = accountValuesMapper.toDto(accountValues);
        accountValuesSearchRepository.save(accountValues);
        return result;
    }

    @Override
    public Optional<AccountValuesDTO> partialUpdate(AccountValuesDTO accountValuesDTO) {
        log.debug("Request to partially update AccountValues : {}", accountValuesDTO);

        return accountValuesRepository
            .findById(accountValuesDTO.getId())
            .map(
                existingAccountValues -> {
                    accountValuesMapper.partialUpdate(existingAccountValues, accountValuesDTO);
                    return existingAccountValues;
                }
            )
            .map(accountValuesRepository::save)
            .map(
                savedAccountValues -> {
                    accountValuesSearchRepository.save(savedAccountValues);

                    return savedAccountValues;
                }
            )
            .map(accountValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountValuesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountValues");
        return accountValuesRepository.findAll(pageable).map(accountValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountValuesDTO> findOne(Long id) {
        log.debug("Request to get AccountValues : {}", id);
        return accountValuesRepository.findById(id).map(accountValuesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountValues : {}", id);
        accountValuesRepository.deleteById(id);
        accountValuesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountValuesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountValues for query {}", query);
        return accountValuesSearchRepository.search(queryStringQuery(query), pageable).map(accountValuesMapper::toDto);
    }
}
