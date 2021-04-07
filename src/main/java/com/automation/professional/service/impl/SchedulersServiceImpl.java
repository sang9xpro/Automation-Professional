package com.automation.professional.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.automation.professional.domain.Schedulers;
import com.automation.professional.repository.SchedulersRepository;
import com.automation.professional.repository.search.SchedulersSearchRepository;
import com.automation.professional.service.SchedulersService;
import com.automation.professional.service.dto.SchedulersDTO;
import com.automation.professional.service.mapper.SchedulersMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Schedulers}.
 */
@Service
@Transactional
public class SchedulersServiceImpl implements SchedulersService {

    private final Logger log = LoggerFactory.getLogger(SchedulersServiceImpl.class);

    private final SchedulersRepository schedulersRepository;

    private final SchedulersMapper schedulersMapper;

    private final SchedulersSearchRepository schedulersSearchRepository;

    public SchedulersServiceImpl(
        SchedulersRepository schedulersRepository,
        SchedulersMapper schedulersMapper,
        SchedulersSearchRepository schedulersSearchRepository
    ) {
        this.schedulersRepository = schedulersRepository;
        this.schedulersMapper = schedulersMapper;
        this.schedulersSearchRepository = schedulersSearchRepository;
    }

    @Override
    public SchedulersDTO save(SchedulersDTO schedulersDTO) {
        log.debug("Request to save Schedulers : {}", schedulersDTO);
        Schedulers schedulers = schedulersMapper.toEntity(schedulersDTO);
        schedulers = schedulersRepository.save(schedulers);
        SchedulersDTO result = schedulersMapper.toDto(schedulers);
        schedulersSearchRepository.save(schedulers);
        return result;
    }

    @Override
    public Optional<SchedulersDTO> partialUpdate(SchedulersDTO schedulersDTO) {
        log.debug("Request to partially update Schedulers : {}", schedulersDTO);

        return schedulersRepository
            .findById(schedulersDTO.getId())
            .map(
                existingSchedulers -> {
                    schedulersMapper.partialUpdate(existingSchedulers, schedulersDTO);
                    return existingSchedulers;
                }
            )
            .map(schedulersRepository::save)
            .map(
                savedSchedulers -> {
                    schedulersSearchRepository.save(savedSchedulers);

                    return savedSchedulers;
                }
            )
            .map(schedulersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Schedulers");
        return schedulersRepository.findAll(pageable).map(schedulersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SchedulersDTO> findOne(Long id) {
        log.debug("Request to get Schedulers : {}", id);
        return schedulersRepository.findById(id).map(schedulersMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Schedulers : {}", id);
        schedulersRepository.deleteById(id);
        schedulersSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulersDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Schedulers for query {}", query);
        return schedulersSearchRepository.search(queryStringQuery(query), pageable).map(schedulersMapper::toDto);
    }
}
