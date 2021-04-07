package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.Accounts;
import com.automation.professional.repository.AccountsRepository;
import com.automation.professional.repository.search.AccountsSearchRepository;
import com.automation.professional.service.AccountsService;
import com.automation.professional.service.dto.AccountsDTO;
import com.automation.professional.service.mapper.AccountsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Accounts}.
 */
@Service
@Transactional
public class AccountsServiceImpl implements AccountsService {

    private final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private final AccountsRepository accountsRepository;

    private final AccountsMapper accountsMapper;

    private final AccountsSearchRepository accountsSearchRepository;

    public AccountsServiceImpl(
        AccountsRepository accountsRepository,
        AccountsMapper accountsMapper,
        AccountsSearchRepository accountsSearchRepository
    ) {
        this.accountsRepository = accountsRepository;
        this.accountsMapper = accountsMapper;
        this.accountsSearchRepository = accountsSearchRepository;
    }

    @Override
    public AccountsDTO save(AccountsDTO accountsDTO) {
        log.debug("Request to save Accounts : {}", accountsDTO);
        Accounts accounts = accountsMapper.toEntity(accountsDTO);
        accounts = accountsRepository.save(accounts);
        AccountsDTO result = accountsMapper.toDto(accounts);
        accountsSearchRepository.save(accounts);
        return result;
    }

    @Override
    public Optional<AccountsDTO> partialUpdate(AccountsDTO accountsDTO) {
        log.debug("Request to partially update Accounts : {}", accountsDTO);

        return accountsRepository
            .findById(accountsDTO.getId())
            .map(
                existingAccounts -> {
                    accountsMapper.partialUpdate(existingAccounts, accountsDTO);
                    return existingAccounts;
                }
            )
            .map(accountsRepository::save)
            .map(
                savedAccounts -> {
                    accountsSearchRepository.save(savedAccounts);

                    return savedAccounts;
                }
            )
            .map(accountsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Accounts");
        return accountsRepository.findAll(pageable).map(accountsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountsDTO> findOne(Long id) {
        log.debug("Request to get Accounts : {}", id);
        return accountsRepository.findById(id).map(accountsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Accounts : {}", id);
        accountsRepository.deleteById(id);
        accountsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Accounts for query {}", query);
        return accountsSearchRepository.search(queryStringQuery(query), pageable).map(accountsMapper::toDto);
    }
}
