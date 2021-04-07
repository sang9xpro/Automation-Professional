package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.AccountFields;
import com.automation.professional.repository.AccountFieldsRepository;
import com.automation.professional.repository.search.AccountFieldsSearchRepository;
import com.automation.professional.service.AccountFieldsService;
import com.automation.professional.service.dto.AccountFieldsDTO;
import com.automation.professional.service.mapper.AccountFieldsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AccountFields}.
 */
@Service
@Transactional
public class AccountFieldsServiceImpl implements AccountFieldsService {

    private final Logger log = LoggerFactory.getLogger(AccountFieldsServiceImpl.class);

    private final AccountFieldsRepository accountFieldsRepository;

    private final AccountFieldsMapper accountFieldsMapper;

    private final AccountFieldsSearchRepository accountFieldsSearchRepository;

    public AccountFieldsServiceImpl(
        AccountFieldsRepository accountFieldsRepository,
        AccountFieldsMapper accountFieldsMapper,
        AccountFieldsSearchRepository accountFieldsSearchRepository
    ) {
        this.accountFieldsRepository = accountFieldsRepository;
        this.accountFieldsMapper = accountFieldsMapper;
        this.accountFieldsSearchRepository = accountFieldsSearchRepository;
    }

    @Override
    public AccountFieldsDTO save(AccountFieldsDTO accountFieldsDTO) {
        log.debug("Request to save AccountFields : {}", accountFieldsDTO);
        AccountFields accountFields = accountFieldsMapper.toEntity(accountFieldsDTO);
        accountFields = accountFieldsRepository.save(accountFields);
        AccountFieldsDTO result = accountFieldsMapper.toDto(accountFields);
        accountFieldsSearchRepository.save(accountFields);
        return result;
    }

    @Override
    public Optional<AccountFieldsDTO> partialUpdate(AccountFieldsDTO accountFieldsDTO) {
        log.debug("Request to partially update AccountFields : {}", accountFieldsDTO);

        return accountFieldsRepository
            .findById(accountFieldsDTO.getId())
            .map(
                existingAccountFields -> {
                    accountFieldsMapper.partialUpdate(existingAccountFields, accountFieldsDTO);
                    return existingAccountFields;
                }
            )
            .map(accountFieldsRepository::save)
            .map(
                savedAccountFields -> {
                    accountFieldsSearchRepository.save(savedAccountFields);

                    return savedAccountFields;
                }
            )
            .map(accountFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountFieldsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountFields");
        return accountFieldsRepository.findAll(pageable).map(accountFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountFieldsDTO> findOne(Long id) {
        log.debug("Request to get AccountFields : {}", id);
        return accountFieldsRepository.findById(id).map(accountFieldsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountFields : {}", id);
        accountFieldsRepository.deleteById(id);
        accountFieldsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountFieldsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountFields for query {}", query);
        return accountFieldsSearchRepository.search(queryStringQuery(query), pageable).map(accountFieldsMapper::toDto);
    }
}
