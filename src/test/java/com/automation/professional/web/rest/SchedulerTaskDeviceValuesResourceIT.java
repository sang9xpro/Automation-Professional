package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.SchedulerTaskDeviceValues;
import com.automation.professional.repository.SchedulerTaskDeviceValuesRepository;
import com.automation.professional.repository.search.SchedulerTaskDeviceValuesSearchRepository;
import com.automation.professional.service.dto.SchedulerTaskDeviceValuesDTO;
import com.automation.professional.service.mapper.SchedulerTaskDeviceValuesMapper;
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
 * Integration tests for the {@link SchedulerTaskDeviceValuesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchedulerTaskDeviceValuesResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/scheduler-task-device-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/scheduler-task-device-values";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchedulerTaskDeviceValuesRepository schedulerTaskDeviceValuesRepository;

    @Autowired
    private SchedulerTaskDeviceValuesMapper schedulerTaskDeviceValuesMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.SchedulerTaskDeviceValuesSearchRepositoryMockConfiguration
     */
    @Autowired
    private SchedulerTaskDeviceValuesSearchRepository mockSchedulerTaskDeviceValuesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchedulerTaskDeviceValuesMockMvc;

    private SchedulerTaskDeviceValues schedulerTaskDeviceValues;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulerTaskDeviceValues createEntity(EntityManager em) {
        SchedulerTaskDeviceValues schedulerTaskDeviceValues = new SchedulerTaskDeviceValues().value(DEFAULT_VALUE);
        return schedulerTaskDeviceValues;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulerTaskDeviceValues createUpdatedEntity(EntityManager em) {
        SchedulerTaskDeviceValues schedulerTaskDeviceValues = new SchedulerTaskDeviceValues().value(UPDATED_VALUE);
        return schedulerTaskDeviceValues;
    }

    @BeforeEach
    public void initTest() {
        schedulerTaskDeviceValues = createEntity(em);
    }

    @Test
    @Transactional
    void createSchedulerTaskDeviceValues() throws Exception {
        int databaseSizeBeforeCreate = schedulerTaskDeviceValuesRepository.findAll().size();
        // Create the SchedulerTaskDeviceValues
        SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO = schedulerTaskDeviceValuesMapper.toDto(schedulerTaskDeviceValues);
        restSchedulerTaskDeviceValuesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceValuesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchedulerTaskDeviceValues in the database
        List<SchedulerTaskDeviceValues> schedulerTaskDeviceValuesList = schedulerTaskDeviceValuesRepository.findAll();
        assertThat(schedulerTaskDeviceValuesList).hasSize(databaseSizeBeforeCreate + 1);
        SchedulerTaskDeviceValues testSchedulerTaskDeviceValues = schedulerTaskDeviceValuesList.get(
            schedulerTaskDeviceValuesList.size() - 1
        );
        assertThat(testSchedulerTaskDeviceValues.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the SchedulerTaskDeviceValues in Elasticsearch
        verify(mockSchedulerTaskDeviceValuesSearchRepository, times(1)).save(testSchedulerTaskDeviceValues);
    }

    @Test
    @Transactional
    void createSchedulerTaskDeviceValuesWithExistingId() throws Exception {
        // Create the SchedulerTaskDeviceValues with an existing ID
        schedulerTaskDeviceValues.setId(1L);
        SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO = schedulerTaskDeviceValuesMapper.toDto(schedulerTaskDeviceValues);

        int databaseSizeBeforeCreate = schedulerTaskDeviceValuesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedulerTaskDeviceValuesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDeviceValues in the database
        List<SchedulerTaskDeviceValues> schedulerTaskDeviceValuesList = schedulerTaskDeviceValuesRepository.findAll();
        assertThat(schedulerTaskDeviceValuesList).hasSize(databaseSizeBeforeCreate);

        // Validate the SchedulerTaskDeviceValues in Elasticsearch
        verify(mockSchedulerTaskDeviceValuesSearchRepository, times(0)).save(schedulerTaskDeviceValues);
    }

    @Test
    @Transactional
    void getAllSchedulerTaskDeviceValues() throws Exception {
        // Initialize the database
        schedulerTaskDeviceValuesRepository.saveAndFlush(schedulerTaskDeviceValues);

        // Get all the schedulerTaskDeviceValuesList
        restSchedulerTaskDeviceValuesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerTaskDeviceValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getSchedulerTaskDeviceValues() throws Exception {
        // Initialize the database
        schedulerTaskDeviceValuesRepository.saveAndFlush(schedulerTaskDeviceValues);

        // Get the schedulerTaskDeviceValues
        restSchedulerTaskDeviceValuesMockMvc
            .perform(get(ENTITY_API_URL_ID, schedulerTaskDeviceValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schedulerTaskDeviceValues.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingSchedulerTaskDeviceValues() throws Exception {
        // Get the schedulerTaskDeviceValues
        restSchedulerTaskDeviceValuesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchedulerTaskDeviceValues() throws Exception {
        // Initialize the database
        schedulerTaskDeviceValuesRepository.saveAndFlush(schedulerTaskDeviceValues);

        int databaseSizeBeforeUpdate = schedulerTaskDeviceValuesRepository.findAll().size();

        // Update the schedulerTaskDeviceValues
        SchedulerTaskDeviceValues updatedSchedulerTaskDeviceValues = schedulerTaskDeviceValuesRepository
            .findById(schedulerTaskDeviceValues.getId())
            .get();
        // Disconnect from session so that the updates on updatedSchedulerTaskDeviceValues are not directly saved in db
        em.detach(updatedSchedulerTaskDeviceValues);
        updatedSchedulerTaskDeviceValues.value(UPDATED_VALUE);
        SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO = schedulerTaskDeviceValuesMapper.toDto(updatedSchedulerTaskDeviceValues);

        restSchedulerTaskDeviceValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulerTaskDeviceValuesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceValuesDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerTaskDeviceValues in the database
        List<SchedulerTaskDeviceValues> schedulerTaskDeviceValuesList = schedulerTaskDeviceValuesRepository.findAll();
        assertThat(schedulerTaskDeviceValuesList).hasSize(databaseSizeBeforeUpdate);
        SchedulerTaskDeviceValues testSchedulerTaskDeviceValues = schedulerTaskDeviceValuesList.get(
            schedulerTaskDeviceValuesList.size() - 1
        );
        assertThat(testSchedulerTaskDeviceValues.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the SchedulerTaskDeviceValues in Elasticsearch
        verify(mockSchedulerTaskDeviceValuesSearchRepository).save(testSchedulerTaskDeviceValues);
    }

    @Test
    @Transactional
    void putNonExistingSchedulerTaskDeviceValues() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceValuesRepository.findAll().size();
        schedulerTaskDeviceValues.setId(count.incrementAndGet());

        // Create the SchedulerTaskDeviceValues
        SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO = schedulerTaskDeviceValuesMapper.toDto(schedulerTaskDeviceValues);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulerTaskDeviceValuesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDeviceValues in the database
        List<SchedulerTaskDeviceValues> schedulerTaskDeviceValuesList = schedulerTaskDeviceValuesRepository.findAll();
        assertThat(schedulerTaskDeviceValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDeviceValues in Elasticsearch
        verify(mockSchedulerTaskDeviceValuesSearchRepository, times(0)).save(schedulerTaskDeviceValues);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchedulerTaskDeviceValues() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceValuesRepository.findAll().size();
        schedulerTaskDeviceValues.setId(count.incrementAndGet());

        // Create the SchedulerTaskDeviceValues
        SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO = schedulerTaskDeviceValuesMapper.toDto(schedulerTaskDeviceValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDeviceValues in the database
        List<SchedulerTaskDeviceValues> schedulerTaskDeviceValuesList = schedulerTaskDeviceValuesRepository.findAll();
        assertThat(schedulerTaskDeviceValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDeviceValues in Elasticsearch
        verify(mockSchedulerTaskDeviceValuesSearchRepository, times(0)).save(schedulerTaskDeviceValues);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchedulerTaskDeviceValues() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceValuesRepository.findAll().size();
        schedulerTaskDeviceValues.setId(count.incrementAndGet());

        // Create the SchedulerTaskDeviceValues
        SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO = schedulerTaskDeviceValuesMapper.toDto(schedulerTaskDeviceValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceValuesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceValuesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulerTaskDeviceValues in the database
        List<SchedulerTaskDeviceValues> schedulerTaskDeviceValuesList = schedulerTaskDeviceValuesRepository.findAll();
        assertThat(schedulerTaskDeviceValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDeviceValues in Elasticsearch
        verify(mockSchedulerTaskDeviceValuesSearchRepository, times(0)).save(schedulerTaskDeviceValues);
    }

    @Test
    @Transactional
    void partialUpdateSchedulerTaskDeviceValuesWithPatch() throws Exception {
        // Initialize the database
        schedulerTaskDeviceValuesRepository.saveAndFlush(schedulerTaskDeviceValues);

        int databaseSizeBeforeUpdate = schedulerTaskDeviceValuesRepository.findAll().size();

        // Update the schedulerTaskDeviceValues using partial update
        SchedulerTaskDeviceValues partialUpdatedSchedulerTaskDeviceValues = new SchedulerTaskDeviceValues();
        partialUpdatedSchedulerTaskDeviceValues.setId(schedulerTaskDeviceValues.getId());

        restSchedulerTaskDeviceValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulerTaskDeviceValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulerTaskDeviceValues))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerTaskDeviceValues in the database
        List<SchedulerTaskDeviceValues> schedulerTaskDeviceValuesList = schedulerTaskDeviceValuesRepository.findAll();
        assertThat(schedulerTaskDeviceValuesList).hasSize(databaseSizeBeforeUpdate);
        SchedulerTaskDeviceValues testSchedulerTaskDeviceValues = schedulerTaskDeviceValuesList.get(
            schedulerTaskDeviceValuesList.size() - 1
        );
        assertThat(testSchedulerTaskDeviceValues.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateSchedulerTaskDeviceValuesWithPatch() throws Exception {
        // Initialize the database
        schedulerTaskDeviceValuesRepository.saveAndFlush(schedulerTaskDeviceValues);

        int databaseSizeBeforeUpdate = schedulerTaskDeviceValuesRepository.findAll().size();

        // Update the schedulerTaskDeviceValues using partial update
        SchedulerTaskDeviceValues partialUpdatedSchedulerTaskDeviceValues = new SchedulerTaskDeviceValues();
        partialUpdatedSchedulerTaskDeviceValues.setId(schedulerTaskDeviceValues.getId());

        partialUpdatedSchedulerTaskDeviceValues.value(UPDATED_VALUE);

        restSchedulerTaskDeviceValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulerTaskDeviceValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulerTaskDeviceValues))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerTaskDeviceValues in the database
        List<SchedulerTaskDeviceValues> schedulerTaskDeviceValuesList = schedulerTaskDeviceValuesRepository.findAll();
        assertThat(schedulerTaskDeviceValuesList).hasSize(databaseSizeBeforeUpdate);
        SchedulerTaskDeviceValues testSchedulerTaskDeviceValues = schedulerTaskDeviceValuesList.get(
            schedulerTaskDeviceValuesList.size() - 1
        );
        assertThat(testSchedulerTaskDeviceValues.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingSchedulerTaskDeviceValues() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceValuesRepository.findAll().size();
        schedulerTaskDeviceValues.setId(count.incrementAndGet());

        // Create the SchedulerTaskDeviceValues
        SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO = schedulerTaskDeviceValuesMapper.toDto(schedulerTaskDeviceValues);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schedulerTaskDeviceValuesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDeviceValues in the database
        List<SchedulerTaskDeviceValues> schedulerTaskDeviceValuesList = schedulerTaskDeviceValuesRepository.findAll();
        assertThat(schedulerTaskDeviceValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDeviceValues in Elasticsearch
        verify(mockSchedulerTaskDeviceValuesSearchRepository, times(0)).save(schedulerTaskDeviceValues);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchedulerTaskDeviceValues() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceValuesRepository.findAll().size();
        schedulerTaskDeviceValues.setId(count.incrementAndGet());

        // Create the SchedulerTaskDeviceValues
        SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO = schedulerTaskDeviceValuesMapper.toDto(schedulerTaskDeviceValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDeviceValues in the database
        List<SchedulerTaskDeviceValues> schedulerTaskDeviceValuesList = schedulerTaskDeviceValuesRepository.findAll();
        assertThat(schedulerTaskDeviceValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDeviceValues in Elasticsearch
        verify(mockSchedulerTaskDeviceValuesSearchRepository, times(0)).save(schedulerTaskDeviceValues);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchedulerTaskDeviceValues() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceValuesRepository.findAll().size();
        schedulerTaskDeviceValues.setId(count.incrementAndGet());

        // Create the SchedulerTaskDeviceValues
        SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO = schedulerTaskDeviceValuesMapper.toDto(schedulerTaskDeviceValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceValuesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceValuesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulerTaskDeviceValues in the database
        List<SchedulerTaskDeviceValues> schedulerTaskDeviceValuesList = schedulerTaskDeviceValuesRepository.findAll();
        assertThat(schedulerTaskDeviceValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDeviceValues in Elasticsearch
        verify(mockSchedulerTaskDeviceValuesSearchRepository, times(0)).save(schedulerTaskDeviceValues);
    }

    @Test
    @Transactional
    void deleteSchedulerTaskDeviceValues() throws Exception {
        // Initialize the database
        schedulerTaskDeviceValuesRepository.saveAndFlush(schedulerTaskDeviceValues);

        int databaseSizeBeforeDelete = schedulerTaskDeviceValuesRepository.findAll().size();

        // Delete the schedulerTaskDeviceValues
        restSchedulerTaskDeviceValuesMockMvc
            .perform(delete(ENTITY_API_URL_ID, schedulerTaskDeviceValues.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchedulerTaskDeviceValues> schedulerTaskDeviceValuesList = schedulerTaskDeviceValuesRepository.findAll();
        assertThat(schedulerTaskDeviceValuesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SchedulerTaskDeviceValues in Elasticsearch
        verify(mockSchedulerTaskDeviceValuesSearchRepository, times(1)).deleteById(schedulerTaskDeviceValues.getId());
    }

    @Test
    @Transactional
    void searchSchedulerTaskDeviceValues() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        schedulerTaskDeviceValuesRepository.saveAndFlush(schedulerTaskDeviceValues);
        when(
            mockSchedulerTaskDeviceValuesSearchRepository.search(
                queryStringQuery("id:" + schedulerTaskDeviceValues.getId()),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(schedulerTaskDeviceValues), PageRequest.of(0, 1), 1));

        // Search the schedulerTaskDeviceValues
        restSchedulerTaskDeviceValuesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + schedulerTaskDeviceValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerTaskDeviceValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
}
