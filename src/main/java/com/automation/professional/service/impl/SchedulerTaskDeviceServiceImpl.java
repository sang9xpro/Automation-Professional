package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.SchedulerTaskDevice;
import com.automation.professional.repository.SchedulerTaskDeviceRepository;
import com.automation.professional.repository.search.SchedulerTaskDeviceSearchRepository;
import com.automation.professional.service.SchedulerTaskDeviceService;
import com.automation.professional.service.dto.SchedulerTaskDeviceDTO;
import com.automation.professional.service.mapper.SchedulerTaskDeviceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SchedulerTaskDevice}.
 */
@Service
@Transactional
public class SchedulerTaskDeviceServiceImpl implements SchedulerTaskDeviceService {

    private final Logger log = LoggerFactory.getLogger(SchedulerTaskDeviceServiceImpl.class);

    private final SchedulerTaskDeviceRepository schedulerTaskDeviceRepository;

    private final SchedulerTaskDeviceMapper schedulerTaskDeviceMapper;

    private final SchedulerTaskDeviceSearchRepository schedulerTaskDeviceSearchRepository;

    public SchedulerTaskDeviceServiceImpl(
        SchedulerTaskDeviceRepository schedulerTaskDeviceRepository,
        SchedulerTaskDeviceMapper schedulerTaskDeviceMapper,
        SchedulerTaskDeviceSearchRepository schedulerTaskDeviceSearchRepository
    ) {
        this.schedulerTaskDeviceRepository = schedulerTaskDeviceRepository;
        this.schedulerTaskDeviceMapper = schedulerTaskDeviceMapper;
        this.schedulerTaskDeviceSearchRepository = schedulerTaskDeviceSearchRepository;
    }

    @Override
    public SchedulerTaskDeviceDTO save(SchedulerTaskDeviceDTO schedulerTaskDeviceDTO) {
        log.debug("Request to save SchedulerTaskDevice : {}", schedulerTaskDeviceDTO);
        SchedulerTaskDevice schedulerTaskDevice = schedulerTaskDeviceMapper.toEntity(schedulerTaskDeviceDTO);
        schedulerTaskDevice = schedulerTaskDeviceRepository.save(schedulerTaskDevice);
        SchedulerTaskDeviceDTO result = schedulerTaskDeviceMapper.toDto(schedulerTaskDevice);
        schedulerTaskDeviceSearchRepository.save(schedulerTaskDevice);
        return result;
    }

    @Override
    public Optional<SchedulerTaskDeviceDTO> partialUpdate(SchedulerTaskDeviceDTO schedulerTaskDeviceDTO) {
        log.debug("Request to partially update SchedulerTaskDevice : {}", schedulerTaskDeviceDTO);

        return schedulerTaskDeviceRepository
            .findById(schedulerTaskDeviceDTO.getId())
            .map(
                existingSchedulerTaskDevice -> {
                    schedulerTaskDeviceMapper.partialUpdate(existingSchedulerTaskDevice, schedulerTaskDeviceDTO);
                    return existingSchedulerTaskDevice;
                }
            )
            .map(schedulerTaskDeviceRepository::save)
            .map(
                savedSchedulerTaskDevice -> {
                    schedulerTaskDeviceSearchRepository.save(savedSchedulerTaskDevice);

                    return savedSchedulerTaskDevice;
                }
            )
            .map(schedulerTaskDeviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerTaskDeviceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SchedulerTaskDevices");
        return schedulerTaskDeviceRepository.findAll(pageable).map(schedulerTaskDeviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SchedulerTaskDeviceDTO> findOne(Long id) {
        log.debug("Request to get SchedulerTaskDevice : {}", id);
        return schedulerTaskDeviceRepository.findById(id).map(schedulerTaskDeviceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SchedulerTaskDevice : {}", id);
        schedulerTaskDeviceRepository.deleteById(id);
        schedulerTaskDeviceSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerTaskDeviceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SchedulerTaskDevices for query {}", query);
        return schedulerTaskDeviceSearchRepository.search(queryStringQuery(query), pageable).map(schedulerTaskDeviceMapper::toDto);
    }
}
