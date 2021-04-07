package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.SchedulerTaskDevice;
import com.automation.professional.domain.enumeration.SchedulerStatus;
import com.automation.professional.repository.SchedulerTaskDeviceRepository;
import com.automation.professional.repository.search.SchedulerTaskDeviceSearchRepository;
import com.automation.professional.service.dto.SchedulerTaskDeviceDTO;
import com.automation.professional.service.mapper.SchedulerTaskDeviceMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SchedulerTaskDeviceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchedulerTaskDeviceResourceIT {

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_COUNT_FROM = 1;
    private static final Integer UPDATED_COUNT_FROM = 2;

    private static final Integer DEFAULT_COUNT_TO = 1;
    private static final Integer UPDATED_COUNT_TO = 2;

    private static final Double DEFAULT_POINT = 1D;
    private static final Double UPDATED_POINT = 2D;

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final SchedulerStatus DEFAULT_STATUS = SchedulerStatus.Open;
    private static final SchedulerStatus UPDATED_STATUS = SchedulerStatus.Processing;

    private static final String ENTITY_API_URL = "/api/scheduler-task-devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/scheduler-task-devices";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchedulerTaskDeviceRepository schedulerTaskDeviceRepository;

    @Autowired
    private SchedulerTaskDeviceMapper schedulerTaskDeviceMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.SchedulerTaskDeviceSearchRepositoryMockConfiguration
     */
    @Autowired
    private SchedulerTaskDeviceSearchRepository mockSchedulerTaskDeviceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchedulerTaskDeviceMockMvc;

    private SchedulerTaskDevice schedulerTaskDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulerTaskDevice createEntity(EntityManager em) {
        SchedulerTaskDevice schedulerTaskDevice = new SchedulerTaskDevice()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .countFrom(DEFAULT_COUNT_FROM)
            .countTo(DEFAULT_COUNT_TO)
            .point(DEFAULT_POINT)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .owner(DEFAULT_OWNER)
            .status(DEFAULT_STATUS);
        return schedulerTaskDevice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulerTaskDevice createUpdatedEntity(EntityManager em) {
        SchedulerTaskDevice schedulerTaskDevice = new SchedulerTaskDevice()
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .countFrom(UPDATED_COUNT_FROM)
            .countTo(UPDATED_COUNT_TO)
            .point(UPDATED_POINT)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .owner(UPDATED_OWNER)
            .status(UPDATED_STATUS);
        return schedulerTaskDevice;
    }

    @BeforeEach
    public void initTest() {
        schedulerTaskDevice = createEntity(em);
    }

    @Test
    @Transactional
    void createSchedulerTaskDevice() throws Exception {
        int databaseSizeBeforeCreate = schedulerTaskDeviceRepository.findAll().size();
        // Create the SchedulerTaskDevice
        SchedulerTaskDeviceDTO schedulerTaskDeviceDTO = schedulerTaskDeviceMapper.toDto(schedulerTaskDevice);
        restSchedulerTaskDeviceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchedulerTaskDevice in the database
        List<SchedulerTaskDevice> schedulerTaskDeviceList = schedulerTaskDeviceRepository.findAll();
        assertThat(schedulerTaskDeviceList).hasSize(databaseSizeBeforeCreate + 1);
        SchedulerTaskDevice testSchedulerTaskDevice = schedulerTaskDeviceList.get(schedulerTaskDeviceList.size() - 1);
        assertThat(testSchedulerTaskDevice.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testSchedulerTaskDevice.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testSchedulerTaskDevice.getCountFrom()).isEqualTo(DEFAULT_COUNT_FROM);
        assertThat(testSchedulerTaskDevice.getCountTo()).isEqualTo(DEFAULT_COUNT_TO);
        assertThat(testSchedulerTaskDevice.getPoint()).isEqualTo(DEFAULT_POINT);
        assertThat(testSchedulerTaskDevice.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testSchedulerTaskDevice.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testSchedulerTaskDevice.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the SchedulerTaskDevice in Elasticsearch
        verify(mockSchedulerTaskDeviceSearchRepository, times(1)).save(testSchedulerTaskDevice);
    }

    @Test
    @Transactional
    void createSchedulerTaskDeviceWithExistingId() throws Exception {
        // Create the SchedulerTaskDevice with an existing ID
        schedulerTaskDevice.setId(1L);
        SchedulerTaskDeviceDTO schedulerTaskDeviceDTO = schedulerTaskDeviceMapper.toDto(schedulerTaskDevice);

        int databaseSizeBeforeCreate = schedulerTaskDeviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedulerTaskDeviceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDevice in the database
        List<SchedulerTaskDevice> schedulerTaskDeviceList = schedulerTaskDeviceRepository.findAll();
        assertThat(schedulerTaskDeviceList).hasSize(databaseSizeBeforeCreate);

        // Validate the SchedulerTaskDevice in Elasticsearch
        verify(mockSchedulerTaskDeviceSearchRepository, times(0)).save(schedulerTaskDevice);
    }

    @Test
    @Transactional
    void getAllSchedulerTaskDevices() throws Exception {
        // Initialize the database
        schedulerTaskDeviceRepository.saveAndFlush(schedulerTaskDevice);

        // Get all the schedulerTaskDeviceList
        restSchedulerTaskDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerTaskDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].countFrom").value(hasItem(DEFAULT_COUNT_FROM)))
            .andExpect(jsonPath("$.[*].countTo").value(hasItem(DEFAULT_COUNT_TO)))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT.doubleValue())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getSchedulerTaskDevice() throws Exception {
        // Initialize the database
        schedulerTaskDeviceRepository.saveAndFlush(schedulerTaskDevice);

        // Get the schedulerTaskDevice
        restSchedulerTaskDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, schedulerTaskDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schedulerTaskDevice.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.countFrom").value(DEFAULT_COUNT_FROM))
            .andExpect(jsonPath("$.countTo").value(DEFAULT_COUNT_TO))
            .andExpect(jsonPath("$.point").value(DEFAULT_POINT.doubleValue()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSchedulerTaskDevice() throws Exception {
        // Get the schedulerTaskDevice
        restSchedulerTaskDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchedulerTaskDevice() throws Exception {
        // Initialize the database
        schedulerTaskDeviceRepository.saveAndFlush(schedulerTaskDevice);

        int databaseSizeBeforeUpdate = schedulerTaskDeviceRepository.findAll().size();

        // Update the schedulerTaskDevice
        SchedulerTaskDevice updatedSchedulerTaskDevice = schedulerTaskDeviceRepository.findById(schedulerTaskDevice.getId()).get();
        // Disconnect from session so that the updates on updatedSchedulerTaskDevice are not directly saved in db
        em.detach(updatedSchedulerTaskDevice);
        updatedSchedulerTaskDevice
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .countFrom(UPDATED_COUNT_FROM)
            .countTo(UPDATED_COUNT_TO)
            .point(UPDATED_POINT)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .owner(UPDATED_OWNER)
            .status(UPDATED_STATUS);
        SchedulerTaskDeviceDTO schedulerTaskDeviceDTO = schedulerTaskDeviceMapper.toDto(updatedSchedulerTaskDevice);

        restSchedulerTaskDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulerTaskDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerTaskDevice in the database
        List<SchedulerTaskDevice> schedulerTaskDeviceList = schedulerTaskDeviceRepository.findAll();
        assertThat(schedulerTaskDeviceList).hasSize(databaseSizeBeforeUpdate);
        SchedulerTaskDevice testSchedulerTaskDevice = schedulerTaskDeviceList.get(schedulerTaskDeviceList.size() - 1);
        assertThat(testSchedulerTaskDevice.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testSchedulerTaskDevice.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testSchedulerTaskDevice.getCountFrom()).isEqualTo(UPDATED_COUNT_FROM);
        assertThat(testSchedulerTaskDevice.getCountTo()).isEqualTo(UPDATED_COUNT_TO);
        assertThat(testSchedulerTaskDevice.getPoint()).isEqualTo(UPDATED_POINT);
        assertThat(testSchedulerTaskDevice.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testSchedulerTaskDevice.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testSchedulerTaskDevice.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the SchedulerTaskDevice in Elasticsearch
        verify(mockSchedulerTaskDeviceSearchRepository).save(testSchedulerTaskDevice);
    }

    @Test
    @Transactional
    void putNonExistingSchedulerTaskDevice() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceRepository.findAll().size();
        schedulerTaskDevice.setId(count.incrementAndGet());

        // Create the SchedulerTaskDevice
        SchedulerTaskDeviceDTO schedulerTaskDeviceDTO = schedulerTaskDeviceMapper.toDto(schedulerTaskDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulerTaskDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDevice in the database
        List<SchedulerTaskDevice> schedulerTaskDeviceList = schedulerTaskDeviceRepository.findAll();
        assertThat(schedulerTaskDeviceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDevice in Elasticsearch
        verify(mockSchedulerTaskDeviceSearchRepository, times(0)).save(schedulerTaskDevice);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchedulerTaskDevice() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceRepository.findAll().size();
        schedulerTaskDevice.setId(count.incrementAndGet());

        // Create the SchedulerTaskDevice
        SchedulerTaskDeviceDTO schedulerTaskDeviceDTO = schedulerTaskDeviceMapper.toDto(schedulerTaskDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDevice in the database
        List<SchedulerTaskDevice> schedulerTaskDeviceList = schedulerTaskDeviceRepository.findAll();
        assertThat(schedulerTaskDeviceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDevice in Elasticsearch
        verify(mockSchedulerTaskDeviceSearchRepository, times(0)).save(schedulerTaskDevice);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchedulerTaskDevice() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceRepository.findAll().size();
        schedulerTaskDevice.setId(count.incrementAndGet());

        // Create the SchedulerTaskDevice
        SchedulerTaskDeviceDTO schedulerTaskDeviceDTO = schedulerTaskDeviceMapper.toDto(schedulerTaskDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulerTaskDevice in the database
        List<SchedulerTaskDevice> schedulerTaskDeviceList = schedulerTaskDeviceRepository.findAll();
        assertThat(schedulerTaskDeviceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDevice in Elasticsearch
        verify(mockSchedulerTaskDeviceSearchRepository, times(0)).save(schedulerTaskDevice);
    }

    @Test
    @Transactional
    void partialUpdateSchedulerTaskDeviceWithPatch() throws Exception {
        // Initialize the database
        schedulerTaskDeviceRepository.saveAndFlush(schedulerTaskDevice);

        int databaseSizeBeforeUpdate = schedulerTaskDeviceRepository.findAll().size();

        // Update the schedulerTaskDevice using partial update
        SchedulerTaskDevice partialUpdatedSchedulerTaskDevice = new SchedulerTaskDevice();
        partialUpdatedSchedulerTaskDevice.setId(schedulerTaskDevice.getId());

        partialUpdatedSchedulerTaskDevice
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .countFrom(UPDATED_COUNT_FROM)
            .countTo(UPDATED_COUNT_TO)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .status(UPDATED_STATUS);

        restSchedulerTaskDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulerTaskDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulerTaskDevice))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerTaskDevice in the database
        List<SchedulerTaskDevice> schedulerTaskDeviceList = schedulerTaskDeviceRepository.findAll();
        assertThat(schedulerTaskDeviceList).hasSize(databaseSizeBeforeUpdate);
        SchedulerTaskDevice testSchedulerTaskDevice = schedulerTaskDeviceList.get(schedulerTaskDeviceList.size() - 1);
        assertThat(testSchedulerTaskDevice.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testSchedulerTaskDevice.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testSchedulerTaskDevice.getCountFrom()).isEqualTo(UPDATED_COUNT_FROM);
        assertThat(testSchedulerTaskDevice.getCountTo()).isEqualTo(UPDATED_COUNT_TO);
        assertThat(testSchedulerTaskDevice.getPoint()).isEqualTo(DEFAULT_POINT);
        assertThat(testSchedulerTaskDevice.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testSchedulerTaskDevice.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testSchedulerTaskDevice.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateSchedulerTaskDeviceWithPatch() throws Exception {
        // Initialize the database
        schedulerTaskDeviceRepository.saveAndFlush(schedulerTaskDevice);

        int databaseSizeBeforeUpdate = schedulerTaskDeviceRepository.findAll().size();

        // Update the schedulerTaskDevice using partial update
        SchedulerTaskDevice partialUpdatedSchedulerTaskDevice = new SchedulerTaskDevice();
        partialUpdatedSchedulerTaskDevice.setId(schedulerTaskDevice.getId());

        partialUpdatedSchedulerTaskDevice
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .countFrom(UPDATED_COUNT_FROM)
            .countTo(UPDATED_COUNT_TO)
            .point(UPDATED_POINT)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .owner(UPDATED_OWNER)
            .status(UPDATED_STATUS);

        restSchedulerTaskDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulerTaskDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulerTaskDevice))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerTaskDevice in the database
        List<SchedulerTaskDevice> schedulerTaskDeviceList = schedulerTaskDeviceRepository.findAll();
        assertThat(schedulerTaskDeviceList).hasSize(databaseSizeBeforeUpdate);
        SchedulerTaskDevice testSchedulerTaskDevice = schedulerTaskDeviceList.get(schedulerTaskDeviceList.size() - 1);
        assertThat(testSchedulerTaskDevice.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testSchedulerTaskDevice.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testSchedulerTaskDevice.getCountFrom()).isEqualTo(UPDATED_COUNT_FROM);
        assertThat(testSchedulerTaskDevice.getCountTo()).isEqualTo(UPDATED_COUNT_TO);
        assertThat(testSchedulerTaskDevice.getPoint()).isEqualTo(UPDATED_POINT);
        assertThat(testSchedulerTaskDevice.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testSchedulerTaskDevice.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testSchedulerTaskDevice.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingSchedulerTaskDevice() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceRepository.findAll().size();
        schedulerTaskDevice.setId(count.incrementAndGet());

        // Create the SchedulerTaskDevice
        SchedulerTaskDeviceDTO schedulerTaskDeviceDTO = schedulerTaskDeviceMapper.toDto(schedulerTaskDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schedulerTaskDeviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDevice in the database
        List<SchedulerTaskDevice> schedulerTaskDeviceList = schedulerTaskDeviceRepository.findAll();
        assertThat(schedulerTaskDeviceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDevice in Elasticsearch
        verify(mockSchedulerTaskDeviceSearchRepository, times(0)).save(schedulerTaskDevice);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchedulerTaskDevice() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceRepository.findAll().size();
        schedulerTaskDevice.setId(count.incrementAndGet());

        // Create the SchedulerTaskDevice
        SchedulerTaskDeviceDTO schedulerTaskDeviceDTO = schedulerTaskDeviceMapper.toDto(schedulerTaskDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDevice in the database
        List<SchedulerTaskDevice> schedulerTaskDeviceList = schedulerTaskDeviceRepository.findAll();
        assertThat(schedulerTaskDeviceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDevice in Elasticsearch
        verify(mockSchedulerTaskDeviceSearchRepository, times(0)).save(schedulerTaskDevice);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchedulerTaskDevice() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceRepository.findAll().size();
        schedulerTaskDevice.setId(count.incrementAndGet());

        // Create the SchedulerTaskDevice
        SchedulerTaskDeviceDTO schedulerTaskDeviceDTO = schedulerTaskDeviceMapper.toDto(schedulerTaskDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulerTaskDevice in the database
        List<SchedulerTaskDevice> schedulerTaskDeviceList = schedulerTaskDeviceRepository.findAll();
        assertThat(schedulerTaskDeviceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDevice in Elasticsearch
        verify(mockSchedulerTaskDeviceSearchRepository, times(0)).save(schedulerTaskDevice);
    }

    @Test
    @Transactional
    void deleteSchedulerTaskDevice() throws Exception {
        // Initialize the database
        schedulerTaskDeviceRepository.saveAndFlush(schedulerTaskDevice);

        int databaseSizeBeforeDelete = schedulerTaskDeviceRepository.findAll().size();

        // Delete the schedulerTaskDevice
        restSchedulerTaskDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, schedulerTaskDevice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchedulerTaskDevice> schedulerTaskDeviceList = schedulerTaskDeviceRepository.findAll();
        assertThat(schedulerTaskDeviceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SchedulerTaskDevice in Elasticsearch
        verify(mockSchedulerTaskDeviceSearchRepository, times(1)).deleteById(schedulerTaskDevice.getId());
    }

    @Test
    @Transactional
    void searchSchedulerTaskDevice() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        schedulerTaskDeviceRepository.saveAndFlush(schedulerTaskDevice);
        when(mockSchedulerTaskDeviceSearchRepository.search(queryStringQuery("id:" + schedulerTaskDevice.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(schedulerTaskDevice), PageRequest.of(0, 1), 1));

        // Search the schedulerTaskDevice
        restSchedulerTaskDeviceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + schedulerTaskDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerTaskDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].countFrom").value(hasItem(DEFAULT_COUNT_FROM)))
            .andExpect(jsonPath("$.[*].countTo").value(hasItem(DEFAULT_COUNT_TO)))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT.doubleValue())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
