package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.Country;
import com.automation.professional.repository.CountryRepository;
import com.automation.professional.repository.search.CountrySearchRepository;
import com.automation.professional.service.CountryService;
import com.automation.professional.service.dto.CountryDTO;
import com.automation.professional.service.mapper.CountryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Country}.
 */
@Service
@Transactional
public class CountryServiceImpl implements CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;

    private final CountrySearchRepository countrySearchRepository;

    public CountryServiceImpl(
        CountryRepository countryRepository,
        CountryMapper countryMapper,
        CountrySearchRepository countrySearchRepository
    ) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
        this.countrySearchRepository = countrySearchRepository;
    }

    @Override
    public CountryDTO save(CountryDTO countryDTO) {
        log.debug("Request to save Country : {}", countryDTO);
        Country country = countryMapper.toEntity(countryDTO);
        country = countryRepository.save(country);
        CountryDTO result = countryMapper.toDto(country);
        countrySearchRepository.save(country);
        return result;
    }

    @Override
    public Optional<CountryDTO> partialUpdate(CountryDTO countryDTO) {
        log.debug("Request to partially update Country : {}", countryDTO);

        return countryRepository
            .findById(countryDTO.getId())
            .map(
                existingCountry -> {
                    countryMapper.partialUpdate(existingCountry, countryDTO);
                    return existingCountry;
                }
            )
            .map(countryRepository::save)
            .map(
                savedCountry -> {
                    countrySearchRepository.save(savedCountry);

                    return savedCountry;
                }
            )
            .map(countryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CountryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Countries");
        return countryRepository.findAll(pageable).map(countryMapper::toDto);
    }

    /**
     *  Get all the countries where Devices is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CountryDTO> findAllWhereDevicesIsNull() {
        log.debug("Request to get all countries where Devices is null");
        return StreamSupport
            .stream(countryRepository.findAll().spliterator(), false)
            .filter(country -> country.getDevices() == null)
            .map(countryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the countries where Accounts is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CountryDTO> findAllWhereAccountsIsNull() {
        log.debug("Request to get all countries where Accounts is null");
        return StreamSupport
            .stream(countryRepository.findAll().spliterator(), false)
            .filter(country -> country.getAccounts() == null)
            .map(countryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the countries where Facebook is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CountryDTO> findAllWhereFacebookIsNull() {
        log.debug("Request to get all countries where Facebook is null");
        return StreamSupport
            .stream(countryRepository.findAll().spliterator(), false)
            .filter(country -> country.getFacebook() == null)
            .map(countryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CountryDTO> findOne(Long id) {
        log.debug("Request to get Country : {}", id);
        return countryRepository.findById(id).map(countryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Country : {}", id);
        countryRepository.deleteById(id);
        countrySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CountryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Countries for query {}", query);
        return countrySearchRepository.search(queryStringQuery(query), pageable).map(countryMapper::toDto);
    }
}
