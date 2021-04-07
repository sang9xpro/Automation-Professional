package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.DeviceValues;
import com.automation.professional.repository.DeviceValuesRepository;
import com.automation.professional.repository.search.DeviceValuesSearchRepository;
import com.automation.professional.service.DeviceValuesService;
import com.automation.professional.service.dto.DeviceValuesDTO;
import com.automation.professional.service.mapper.DeviceValuesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeviceValues}.
 */
@Service
@Transactional
public class DeviceValuesServiceImpl implements DeviceValuesService {

    private final Logger log = LoggerFactory.getLogger(DeviceValuesServiceImpl.class);

    private final DeviceValuesRepository deviceValuesRepository;

    private final DeviceValuesMapper deviceValuesMapper;

    private final DeviceValuesSearchRepository deviceValuesSearchRepository;

    public DeviceValuesServiceImpl(
        DeviceValuesRepository deviceValuesRepository,
        DeviceValuesMapper deviceValuesMapper,
        DeviceValuesSearchRepository deviceValuesSearchRepository
    ) {
        this.deviceValuesRepository = deviceValuesRepository;
        this.deviceValuesMapper = deviceValuesMapper;
        this.deviceValuesSearchRepository = deviceValuesSearchRepository;
    }

    @Override
    public DeviceValuesDTO save(DeviceValuesDTO deviceValuesDTO) {
        log.debug("Request to save DeviceValues : {}", deviceValuesDTO);
        DeviceValues deviceValues = deviceValuesMapper.toEntity(deviceValuesDTO);
        deviceValues = deviceValuesRepository.save(deviceValues);
        DeviceValuesDTO result = deviceValuesMapper.toDto(deviceValues);
        deviceValuesSearchRepository.save(deviceValues);
        return result;
    }

    @Override
    public Optional<DeviceValuesDTO> partialUpdate(DeviceValuesDTO deviceValuesDTO) {
        log.debug("Request to partially update DeviceValues : {}", deviceValuesDTO);

        return deviceValuesRepository
            .findById(deviceValuesDTO.getId())
            .map(
                existingDeviceValues -> {
                    deviceValuesMapper.partialUpdate(existingDeviceValues, deviceValuesDTO);
                    return existingDeviceValues;
                }
            )
            .map(deviceValuesRepository::save)
            .map(
                savedDeviceValues -> {
                    deviceValuesSearchRepository.save(savedDeviceValues);

                    return savedDeviceValues;
                }
            )
            .map(deviceValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceValuesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeviceValues");
        return deviceValuesRepository.findAll(pageable).map(deviceValuesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceValuesDTO> findOne(Long id) {
        log.debug("Request to get DeviceValues : {}", id);
        return deviceValuesRepository.findById(id).map(deviceValuesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeviceValues : {}", id);
        deviceValuesRepository.deleteById(id);
        deviceValuesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceValuesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DeviceValues for query {}", query);
        return deviceValuesSearchRepository.search(queryStringQuery(query), pageable).map(deviceValuesMapper::toDto);
    }
}
