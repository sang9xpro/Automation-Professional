package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.DevicesFields;
import com.automation.professional.repository.DevicesFieldsRepository;
import com.automation.professional.repository.search.DevicesFieldsSearchRepository;
import com.automation.professional.service.DevicesFieldsService;
import com.automation.professional.service.dto.DevicesFieldsDTO;
import com.automation.professional.service.mapper.DevicesFieldsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DevicesFields}.
 */
@Service
@Transactional
public class DevicesFieldsServiceImpl implements DevicesFieldsService {

    private final Logger log = LoggerFactory.getLogger(DevicesFieldsServiceImpl.class);

    private final DevicesFieldsRepository devicesFieldsRepository;

    private final DevicesFieldsMapper devicesFieldsMapper;

    private final DevicesFieldsSearchRepository devicesFieldsSearchRepository;

    public DevicesFieldsServiceImpl(
        DevicesFieldsRepository devicesFieldsRepository,
        DevicesFieldsMapper devicesFieldsMapper,
        DevicesFieldsSearchRepository devicesFieldsSearchRepository
    ) {
        this.devicesFieldsRepository = devicesFieldsRepository;
        this.devicesFieldsMapper = devicesFieldsMapper;
        this.devicesFieldsSearchRepository = devicesFieldsSearchRepository;
    }

    @Override
    public DevicesFieldsDTO save(DevicesFieldsDTO devicesFieldsDTO) {
        log.debug("Request to save DevicesFields : {}", devicesFieldsDTO);
        DevicesFields devicesFields = devicesFieldsMapper.toEntity(devicesFieldsDTO);
        devicesFields = devicesFieldsRepository.save(devicesFields);
        DevicesFieldsDTO result = devicesFieldsMapper.toDto(devicesFields);
        devicesFieldsSearchRepository.save(devicesFields);
        return result;
    }

    @Override
    public Optional<DevicesFieldsDTO> partialUpdate(DevicesFieldsDTO devicesFieldsDTO) {
        log.debug("Request to partially update DevicesFields : {}", devicesFieldsDTO);

        return devicesFieldsRepository
            .findById(devicesFieldsDTO.getId())
            .map(
                existingDevicesFields -> {
                    devicesFieldsMapper.partialUpdate(existingDevicesFields, devicesFieldsDTO);
                    return existingDevicesFields;
                }
            )
            .map(devicesFieldsRepository::save)
            .map(
                savedDevicesFields -> {
                    devicesFieldsSearchRepository.save(savedDevicesFields);

                    return savedDevicesFields;
                }
            )
            .map(devicesFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DevicesFieldsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DevicesFields");
        return devicesFieldsRepository.findAll(pageable).map(devicesFieldsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DevicesFieldsDTO> findOne(Long id) {
        log.debug("Request to get DevicesFields : {}", id);
        return devicesFieldsRepository.findById(id).map(devicesFieldsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DevicesFields : {}", id);
        devicesFieldsRepository.deleteById(id);
        devicesFieldsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DevicesFieldsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DevicesFields for query {}", query);
        return devicesFieldsSearchRepository.search(queryStringQuery(query), pageable).map(devicesFieldsMapper::toDto);
    }
}
