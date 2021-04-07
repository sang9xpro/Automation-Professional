package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.SchedulerValue;
import com.automation.professional.repository.SchedulerValueRepository;
import com.automation.professional.repository.search.SchedulerValueSearchRepository;
import com.automation.professional.service.dto.SchedulerValueDTO;
import com.automation.professional.service.mapper.SchedulerValueMapper;
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
 * Integration tests for the {@link SchedulerValueResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchedulerValueResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/scheduler-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/scheduler-values";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchedulerValueRepository schedulerValueRepository;

    @Autowired
    private SchedulerValueMapper schedulerValueMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.SchedulerValueSearchRepositoryMockConfiguration
     */
    @Autowired
    private SchedulerValueSearchRepository mockSchedulerValueSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchedulerValueMockMvc;

    private SchedulerValue schedulerValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulerValue createEntity(EntityManager em) {
        SchedulerValue schedulerValue = new SchedulerValue().value(DEFAULT_VALUE);
        return schedulerValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulerValue createUpdatedEntity(EntityManager em) {
        SchedulerValue schedulerValue = new SchedulerValue().value(UPDATED_VALUE);
        return schedulerValue;
    }

    @BeforeEach
    public void initTest() {
        schedulerValue = createEntity(em);
    }

    @Test
    @Transactional
    void createSchedulerValue() throws Exception {
        int databaseSizeBeforeCreate = schedulerValueRepository.findAll().size();
        // Create the SchedulerValue
        SchedulerValueDTO schedulerValueDTO = schedulerValueMapper.toDto(schedulerValue);
        restSchedulerValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schedulerValueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchedulerValue in the database
        List<SchedulerValue> schedulerValueList = schedulerValueRepository.findAll();
        assertThat(schedulerValueList).hasSize(databaseSizeBeforeCreate + 1);
        SchedulerValue testSchedulerValue = schedulerValueList.get(schedulerValueList.size() - 1);
        assertThat(testSchedulerValue.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the SchedulerValue in Elasticsearch
        verify(mockSchedulerValueSearchRepository, times(1)).save(testSchedulerValue);
    }

    @Test
    @Transactional
    void createSchedulerValueWithExistingId() throws Exception {
        // Create the SchedulerValue with an existing ID
        schedulerValue.setId(1L);
        SchedulerValueDTO schedulerValueDTO = schedulerValueMapper.toDto(schedulerValue);

        int databaseSizeBeforeCreate = schedulerValueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedulerValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schedulerValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerValue in the database
        List<SchedulerValue> schedulerValueList = schedulerValueRepository.findAll();
        assertThat(schedulerValueList).hasSize(databaseSizeBeforeCreate);

        // Validate the SchedulerValue in Elasticsearch
        verify(mockSchedulerValueSearchRepository, times(0)).save(schedulerValue);
    }

    @Test
    @Transactional
    void getAllSchedulerValues() throws Exception {
        // Initialize the database
        schedulerValueRepository.saveAndFlush(schedulerValue);

        // Get all the schedulerValueList
        restSchedulerValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getSchedulerValue() throws Exception {
        // Initialize the database
        schedulerValueRepository.saveAndFlush(schedulerValue);

        // Get the schedulerValue
        restSchedulerValueMockMvc
            .perform(get(ENTITY_API_URL_ID, schedulerValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schedulerValue.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingSchedulerValue() throws Exception {
        // Get the schedulerValue
        restSchedulerValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchedulerValue() throws Exception {
        // Initialize the database
        schedulerValueRepository.saveAndFlush(schedulerValue);

        int databaseSizeBeforeUpdate = schedulerValueRepository.findAll().size();

        // Update the schedulerValue
        SchedulerValue updatedSchedulerValue = schedulerValueRepository.findById(schedulerValue.getId()).get();
        // Disconnect from session so that the updates on updatedSchedulerValue are not directly saved in db
        em.detach(updatedSchedulerValue);
        updatedSchedulerValue.value(UPDATED_VALUE);
        SchedulerValueDTO schedulerValueDTO = schedulerValueMapper.toDto(updatedSchedulerValue);

        restSchedulerValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulerValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerValueDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerValue in the database
        List<SchedulerValue> schedulerValueList = schedulerValueRepository.findAll();
        assertThat(schedulerValueList).hasSize(databaseSizeBeforeUpdate);
        SchedulerValue testSchedulerValue = schedulerValueList.get(schedulerValueList.size() - 1);
        assertThat(testSchedulerValue.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the SchedulerValue in Elasticsearch
        verify(mockSchedulerValueSearchRepository).save(testSchedulerValue);
    }

    @Test
    @Transactional
    void putNonExistingSchedulerValue() throws Exception {
        int databaseSizeBeforeUpdate = schedulerValueRepository.findAll().size();
        schedulerValue.setId(count.incrementAndGet());

        // Create the SchedulerValue
        SchedulerValueDTO schedulerValueDTO = schedulerValueMapper.toDto(schedulerValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulerValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulerValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerValue in the database
        List<SchedulerValue> schedulerValueList = schedulerValueRepository.findAll();
        assertThat(schedulerValueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerValue in Elasticsearch
        verify(mockSchedulerValueSearchRepository, times(0)).save(schedulerValue);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchedulerValue() throws Exception {
        int databaseSizeBeforeUpdate = schedulerValueRepository.findAll().size();
        schedulerValue.setId(count.incrementAndGet());

        // Create the SchedulerValue
        SchedulerValueDTO schedulerValueDTO = schedulerValueMapper.toDto(schedulerValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerValue in the database
        List<SchedulerValue> schedulerValueList = schedulerValueRepository.findAll();
        assertThat(schedulerValueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerValue in Elasticsearch
        verify(mockSchedulerValueSearchRepository, times(0)).save(schedulerValue);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchedulerValue() throws Exception {
        int databaseSizeBeforeUpdate = schedulerValueRepository.findAll().size();
        schedulerValue.setId(count.incrementAndGet());

        // Create the SchedulerValue
        SchedulerValueDTO schedulerValueDTO = schedulerValueMapper.toDto(schedulerValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerValueMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schedulerValueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulerValue in the database
        List<SchedulerValue> schedulerValueList = schedulerValueRepository.findAll();
        assertThat(schedulerValueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerValue in Elasticsearch
        verify(mockSchedulerValueSearchRepository, times(0)).save(schedulerValue);
    }

    @Test
    @Transactional
    void partialUpdateSchedulerValueWithPatch() throws Exception {
        // Initialize the database
        schedulerValueRepository.saveAndFlush(schedulerValue);

        int databaseSizeBeforeUpdate = schedulerValueRepository.findAll().size();

        // Update the schedulerValue using partial update
        SchedulerValue partialUpdatedSchedulerValue = new SchedulerValue();
        partialUpdatedSchedulerValue.setId(schedulerValue.getId());

        restSchedulerValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulerValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulerValue))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerValue in the database
        List<SchedulerValue> schedulerValueList = schedulerValueRepository.findAll();
        assertThat(schedulerValueList).hasSize(databaseSizeBeforeUpdate);
        SchedulerValue testSchedulerValue = schedulerValueList.get(schedulerValueList.size() - 1);
        assertThat(testSchedulerValue.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateSchedulerValueWithPatch() throws Exception {
        // Initialize the database
        schedulerValueRepository.saveAndFlush(schedulerValue);

        int databaseSizeBeforeUpdate = schedulerValueRepository.findAll().size();

        // Update the schedulerValue using partial update
        SchedulerValue partialUpdatedSchedulerValue = new SchedulerValue();
        partialUpdatedSchedulerValue.setId(schedulerValue.getId());

        partialUpdatedSchedulerValue.value(UPDATED_VALUE);

        restSchedulerValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulerValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulerValue))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerValue in the database
        List<SchedulerValue> schedulerValueList = schedulerValueRepository.findAll();
        assertThat(schedulerValueList).hasSize(databaseSizeBeforeUpdate);
        SchedulerValue testSchedulerValue = schedulerValueList.get(schedulerValueList.size() - 1);
        assertThat(testSchedulerValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingSchedulerValue() throws Exception {
        int databaseSizeBeforeUpdate = schedulerValueRepository.findAll().size();
        schedulerValue.setId(count.incrementAndGet());

        // Create the SchedulerValue
        SchedulerValueDTO schedulerValueDTO = schedulerValueMapper.toDto(schedulerValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulerValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schedulerValueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerValue in the database
        List<SchedulerValue> schedulerValueList = schedulerValueRepository.findAll();
        assertThat(schedulerValueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerValue in Elasticsearch
        verify(mockSchedulerValueSearchRepository, times(0)).save(schedulerValue);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchedulerValue() throws Exception {
        int databaseSizeBeforeUpdate = schedulerValueRepository.findAll().size();
        schedulerValue.setId(count.incrementAndGet());

        // Create the SchedulerValue
        SchedulerValueDTO schedulerValueDTO = schedulerValueMapper.toDto(schedulerValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerValue in the database
        List<SchedulerValue> schedulerValueList = schedulerValueRepository.findAll();
        assertThat(schedulerValueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerValue in Elasticsearch
        verify(mockSchedulerValueSearchRepository, times(0)).save(schedulerValue);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchedulerValue() throws Exception {
        int databaseSizeBeforeUpdate = schedulerValueRepository.findAll().size();
        schedulerValue.setId(count.incrementAndGet());

        // Create the SchedulerValue
        SchedulerValueDTO schedulerValueDTO = schedulerValueMapper.toDto(schedulerValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerValueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerValueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulerValue in the database
        List<SchedulerValue> schedulerValueList = schedulerValueRepository.findAll();
        assertThat(schedulerValueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerValue in Elasticsearch
        verify(mockSchedulerValueSearchRepository, times(0)).save(schedulerValue);
    }

    @Test
    @Transactional
    void deleteSchedulerValue() throws Exception {
        // Initialize the database
        schedulerValueRepository.saveAndFlush(schedulerValue);

        int databaseSizeBeforeDelete = schedulerValueRepository.findAll().size();

        // Delete the schedulerValue
        restSchedulerValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, schedulerValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchedulerValue> schedulerValueList = schedulerValueRepository.findAll();
        assertThat(schedulerValueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SchedulerValue in Elasticsearch
        verify(mockSchedulerValueSearchRepository, times(1)).deleteById(schedulerValue.getId());
    }

    @Test
    @Transactional
    void searchSchedulerValue() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        schedulerValueRepository.saveAndFlush(schedulerValue);
        when(mockSchedulerValueSearchRepository.search(queryStringQuery("id:" + schedulerValue.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(schedulerValue), PageRequest.of(0, 1), 1));

        // Search the schedulerValue
        restSchedulerValueMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + schedulerValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
}
