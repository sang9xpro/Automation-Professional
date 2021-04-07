package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.Devices;
import com.automation.professional.repository.DevicesRepository;
import com.automation.professional.repository.search.DevicesSearchRepository;
import com.automation.professional.service.DevicesService;
import com.automation.professional.service.dto.DevicesDTO;
import com.automation.professional.service.mapper.DevicesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Devices}.
 */
@Service
@Transactional
public class DevicesServiceImpl implements DevicesService {

    private final Logger log = LoggerFactory.getLogger(DevicesServiceImpl.class);

    private final DevicesRepository devicesRepository;

    private final DevicesMapper devicesMapper;

    private final DevicesSearchRepository devicesSearchRepository;

    public DevicesServiceImpl(
        DevicesRepository devicesRepository,
        DevicesMapper devicesMapper,
        DevicesSearchRepository devicesSearchRepository
    ) {
        this.devicesRepository = devicesRepository;
        this.devicesMapper = devicesMapper;
        this.devicesSearchRepository = devicesSearchRepository;
    }

    @Override
    public DevicesDTO save(DevicesDTO devicesDTO) {
        log.debug("Request to save Devices : {}", devicesDTO);
        Devices devices = devicesMapper.toEntity(devicesDTO);
        devices = devicesRepository.save(devices);
        DevicesDTO result = devicesMapper.toDto(devices);
        devicesSearchRepository.save(devices);
        return result;
    }

    @Override
    public Optional<DevicesDTO> partialUpdate(DevicesDTO devicesDTO) {
        log.debug("Request to partially update Devices : {}", devicesDTO);

        return devicesRepository
            .findById(devicesDTO.getId())
            .map(
                existingDevices -> {
                    devicesMapper.partialUpdate(existingDevices, devicesDTO);
                    return existingDevices;
                }
            )
            .map(devicesRepository::save)
            .map(
                savedDevices -> {
                    devicesSearchRepository.save(savedDevices);

                    return savedDevices;
                }
            )
            .map(devicesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DevicesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Devices");
        return devicesRepository.findAll(pageable).map(devicesMapper::toDto);
    }

    public Page<DevicesDTO> findAllWithEagerRelationships(Pageable pageable) {
        return devicesRepository.findAllWithEagerRelationships(pageable).map(devicesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DevicesDTO> findOne(Long id) {
        log.debug("Request to get Devices : {}", id);
        return devicesRepository.findOneWithEagerRelationships(id).map(devicesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Devices : {}", id);
        devicesRepository.deleteById(id);
        devicesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DevicesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Devices for query {}", query);
        return devicesSearchRepository.search(queryStringQuery(query), pageable).map(devicesMapper::toDto);
    }
}
