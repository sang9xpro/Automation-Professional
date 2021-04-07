package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.SchedulerTaskDeviceFields;
import com.automation.professional.repository.SchedulerTaskDeviceFieldsRepository;
import com.automation.professional.repository.search.SchedulerTaskDeviceFieldsSearchRepository;
import com.automation.professional.service.dto.SchedulerTaskDeviceFieldsDTO;
import com.automation.professional.service.mapper.SchedulerTaskDeviceFieldsMapper;
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
 * Integration tests for the {@link SchedulerTaskDeviceFieldsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchedulerTaskDeviceFieldsResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/scheduler-task-device-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/scheduler-task-device-fields";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchedulerTaskDeviceFieldsRepository schedulerTaskDeviceFieldsRepository;

    @Autowired
    private SchedulerTaskDeviceFieldsMapper schedulerTaskDeviceFieldsMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.SchedulerTaskDeviceFieldsSearchRepositoryMockConfiguration
     */
    @Autowired
    private SchedulerTaskDeviceFieldsSearchRepository mockSchedulerTaskDeviceFieldsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchedulerTaskDeviceFieldsMockMvc;

    private SchedulerTaskDeviceFields schedulerTaskDeviceFields;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulerTaskDeviceFields createEntity(EntityManager em) {
        SchedulerTaskDeviceFields schedulerTaskDeviceFields = new SchedulerTaskDeviceFields().fieldName(DEFAULT_FIELD_NAME);
        return schedulerTaskDeviceFields;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulerTaskDeviceFields createUpdatedEntity(EntityManager em) {
        SchedulerTaskDeviceFields schedulerTaskDeviceFields = new SchedulerTaskDeviceFields().fieldName(UPDATED_FIELD_NAME);
        return schedulerTaskDeviceFields;
    }

    @BeforeEach
    public void initTest() {
        schedulerTaskDeviceFields = createEntity(em);
    }

    @Test
    @Transactional
    void createSchedulerTaskDeviceFields() throws Exception {
        int databaseSizeBeforeCreate = schedulerTaskDeviceFieldsRepository.findAll().size();
        // Create the SchedulerTaskDeviceFields
        SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO = schedulerTaskDeviceFieldsMapper.toDto(schedulerTaskDeviceFields);
        restSchedulerTaskDeviceFieldsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceFieldsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchedulerTaskDeviceFields in the database
        List<SchedulerTaskDeviceFields> schedulerTaskDeviceFieldsList = schedulerTaskDeviceFieldsRepository.findAll();
        assertThat(schedulerTaskDeviceFieldsList).hasSize(databaseSizeBeforeCreate + 1);
        SchedulerTaskDeviceFields testSchedulerTaskDeviceFields = schedulerTaskDeviceFieldsList.get(
            schedulerTaskDeviceFieldsList.size() - 1
        );
        assertThat(testSchedulerTaskDeviceFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);

        // Validate the SchedulerTaskDeviceFields in Elasticsearch
        verify(mockSchedulerTaskDeviceFieldsSearchRepository, times(1)).save(testSchedulerTaskDeviceFields);
    }

    @Test
    @Transactional
    void createSchedulerTaskDeviceFieldsWithExistingId() throws Exception {
        // Create the SchedulerTaskDeviceFields with an existing ID
        schedulerTaskDeviceFields.setId(1L);
        SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO = schedulerTaskDeviceFieldsMapper.toDto(schedulerTaskDeviceFields);

        int databaseSizeBeforeCreate = schedulerTaskDeviceFieldsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedulerTaskDeviceFieldsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDeviceFields in the database
        List<SchedulerTaskDeviceFields> schedulerTaskDeviceFieldsList = schedulerTaskDeviceFieldsRepository.findAll();
        assertThat(schedulerTaskDeviceFieldsList).hasSize(databaseSizeBeforeCreate);

        // Validate the SchedulerTaskDeviceFields in Elasticsearch
        verify(mockSchedulerTaskDeviceFieldsSearchRepository, times(0)).save(schedulerTaskDeviceFields);
    }

    @Test
    @Transactional
    void getAllSchedulerTaskDeviceFields() throws Exception {
        // Initialize the database
        schedulerTaskDeviceFieldsRepository.saveAndFlush(schedulerTaskDeviceFields);

        // Get all the schedulerTaskDeviceFieldsList
        restSchedulerTaskDeviceFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerTaskDeviceFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }

    @Test
    @Transactional
    void getSchedulerTaskDeviceFields() throws Exception {
        // Initialize the database
        schedulerTaskDeviceFieldsRepository.saveAndFlush(schedulerTaskDeviceFields);

        // Get the schedulerTaskDeviceFields
        restSchedulerTaskDeviceFieldsMockMvc
            .perform(get(ENTITY_API_URL_ID, schedulerTaskDeviceFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schedulerTaskDeviceFields.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSchedulerTaskDeviceFields() throws Exception {
        // Get the schedulerTaskDeviceFields
        restSchedulerTaskDeviceFieldsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchedulerTaskDeviceFields() throws Exception {
        // Initialize the database
        schedulerTaskDeviceFieldsRepository.saveAndFlush(schedulerTaskDeviceFields);

        int databaseSizeBeforeUpdate = schedulerTaskDeviceFieldsRepository.findAll().size();

        // Update the schedulerTaskDeviceFields
        SchedulerTaskDeviceFields updatedSchedulerTaskDeviceFields = schedulerTaskDeviceFieldsRepository
            .findById(schedulerTaskDeviceFields.getId())
            .get();
        // Disconnect from session so that the updates on updatedSchedulerTaskDeviceFields are not directly saved in db
        em.detach(updatedSchedulerTaskDeviceFields);
        updatedSchedulerTaskDeviceFields.fieldName(UPDATED_FIELD_NAME);
        SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO = schedulerTaskDeviceFieldsMapper.toDto(updatedSchedulerTaskDeviceFields);

        restSchedulerTaskDeviceFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulerTaskDeviceFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceFieldsDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerTaskDeviceFields in the database
        List<SchedulerTaskDeviceFields> schedulerTaskDeviceFieldsList = schedulerTaskDeviceFieldsRepository.findAll();
        assertThat(schedulerTaskDeviceFieldsList).hasSize(databaseSizeBeforeUpdate);
        SchedulerTaskDeviceFields testSchedulerTaskDeviceFields = schedulerTaskDeviceFieldsList.get(
            schedulerTaskDeviceFieldsList.size() - 1
        );
        assertThat(testSchedulerTaskDeviceFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);

        // Validate the SchedulerTaskDeviceFields in Elasticsearch
        verify(mockSchedulerTaskDeviceFieldsSearchRepository).save(testSchedulerTaskDeviceFields);
    }

    @Test
    @Transactional
    void putNonExistingSchedulerTaskDeviceFields() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceFieldsRepository.findAll().size();
        schedulerTaskDeviceFields.setId(count.incrementAndGet());

        // Create the SchedulerTaskDeviceFields
        SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO = schedulerTaskDeviceFieldsMapper.toDto(schedulerTaskDeviceFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulerTaskDeviceFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDeviceFields in the database
        List<SchedulerTaskDeviceFields> schedulerTaskDeviceFieldsList = schedulerTaskDeviceFieldsRepository.findAll();
        assertThat(schedulerTaskDeviceFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDeviceFields in Elasticsearch
        verify(mockSchedulerTaskDeviceFieldsSearchRepository, times(0)).save(schedulerTaskDeviceFields);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchedulerTaskDeviceFields() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceFieldsRepository.findAll().size();
        schedulerTaskDeviceFields.setId(count.incrementAndGet());

        // Create the SchedulerTaskDeviceFields
        SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO = schedulerTaskDeviceFieldsMapper.toDto(schedulerTaskDeviceFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDeviceFields in the database
        List<SchedulerTaskDeviceFields> schedulerTaskDeviceFieldsList = schedulerTaskDeviceFieldsRepository.findAll();
        assertThat(schedulerTaskDeviceFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDeviceFields in Elasticsearch
        verify(mockSchedulerTaskDeviceFieldsSearchRepository, times(0)).save(schedulerTaskDeviceFields);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchedulerTaskDeviceFields() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceFieldsRepository.findAll().size();
        schedulerTaskDeviceFields.setId(count.incrementAndGet());

        // Create the SchedulerTaskDeviceFields
        SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO = schedulerTaskDeviceFieldsMapper.toDto(schedulerTaskDeviceFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceFieldsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulerTaskDeviceFields in the database
        List<SchedulerTaskDeviceFields> schedulerTaskDeviceFieldsList = schedulerTaskDeviceFieldsRepository.findAll();
        assertThat(schedulerTaskDeviceFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDeviceFields in Elasticsearch
        verify(mockSchedulerTaskDeviceFieldsSearchRepository, times(0)).save(schedulerTaskDeviceFields);
    }

    @Test
    @Transactional
    void partialUpdateSchedulerTaskDeviceFieldsWithPatch() throws Exception {
        // Initialize the database
        schedulerTaskDeviceFieldsRepository.saveAndFlush(schedulerTaskDeviceFields);

        int databaseSizeBeforeUpdate = schedulerTaskDeviceFieldsRepository.findAll().size();

        // Update the schedulerTaskDeviceFields using partial update
        SchedulerTaskDeviceFields partialUpdatedSchedulerTaskDeviceFields = new SchedulerTaskDeviceFields();
        partialUpdatedSchedulerTaskDeviceFields.setId(schedulerTaskDeviceFields.getId());

        partialUpdatedSchedulerTaskDeviceFields.fieldName(UPDATED_FIELD_NAME);

        restSchedulerTaskDeviceFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulerTaskDeviceFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulerTaskDeviceFields))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerTaskDeviceFields in the database
        List<SchedulerTaskDeviceFields> schedulerTaskDeviceFieldsList = schedulerTaskDeviceFieldsRepository.findAll();
        assertThat(schedulerTaskDeviceFieldsList).hasSize(databaseSizeBeforeUpdate);
        SchedulerTaskDeviceFields testSchedulerTaskDeviceFields = schedulerTaskDeviceFieldsList.get(
            schedulerTaskDeviceFieldsList.size() - 1
        );
        assertThat(testSchedulerTaskDeviceFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSchedulerTaskDeviceFieldsWithPatch() throws Exception {
        // Initialize the database
        schedulerTaskDeviceFieldsRepository.saveAndFlush(schedulerTaskDeviceFields);

        int databaseSizeBeforeUpdate = schedulerTaskDeviceFieldsRepository.findAll().size();

        // Update the schedulerTaskDeviceFields using partial update
        SchedulerTaskDeviceFields partialUpdatedSchedulerTaskDeviceFields = new SchedulerTaskDeviceFields();
        partialUpdatedSchedulerTaskDeviceFields.setId(schedulerTaskDeviceFields.getId());

        partialUpdatedSchedulerTaskDeviceFields.fieldName(UPDATED_FIELD_NAME);

        restSchedulerTaskDeviceFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulerTaskDeviceFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulerTaskDeviceFields))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerTaskDeviceFields in the database
        List<SchedulerTaskDeviceFields> schedulerTaskDeviceFieldsList = schedulerTaskDeviceFieldsRepository.findAll();
        assertThat(schedulerTaskDeviceFieldsList).hasSize(databaseSizeBeforeUpdate);
        SchedulerTaskDeviceFields testSchedulerTaskDeviceFields = schedulerTaskDeviceFieldsList.get(
            schedulerTaskDeviceFieldsList.size() - 1
        );
        assertThat(testSchedulerTaskDeviceFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSchedulerTaskDeviceFields() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceFieldsRepository.findAll().size();
        schedulerTaskDeviceFields.setId(count.incrementAndGet());

        // Create the SchedulerTaskDeviceFields
        SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO = schedulerTaskDeviceFieldsMapper.toDto(schedulerTaskDeviceFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schedulerTaskDeviceFieldsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDeviceFields in the database
        List<SchedulerTaskDeviceFields> schedulerTaskDeviceFieldsList = schedulerTaskDeviceFieldsRepository.findAll();
        assertThat(schedulerTaskDeviceFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDeviceFields in Elasticsearch
        verify(mockSchedulerTaskDeviceFieldsSearchRepository, times(0)).save(schedulerTaskDeviceFields);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchedulerTaskDeviceFields() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceFieldsRepository.findAll().size();
        schedulerTaskDeviceFields.setId(count.incrementAndGet());

        // Create the SchedulerTaskDeviceFields
        SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO = schedulerTaskDeviceFieldsMapper.toDto(schedulerTaskDeviceFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerTaskDeviceFields in the database
        List<SchedulerTaskDeviceFields> schedulerTaskDeviceFieldsList = schedulerTaskDeviceFieldsRepository.findAll();
        assertThat(schedulerTaskDeviceFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDeviceFields in Elasticsearch
        verify(mockSchedulerTaskDeviceFieldsSearchRepository, times(0)).save(schedulerTaskDeviceFields);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchedulerTaskDeviceFields() throws Exception {
        int databaseSizeBeforeUpdate = schedulerTaskDeviceFieldsRepository.findAll().size();
        schedulerTaskDeviceFields.setId(count.incrementAndGet());

        // Create the SchedulerTaskDeviceFields
        SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO = schedulerTaskDeviceFieldsMapper.toDto(schedulerTaskDeviceFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerTaskDeviceFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerTaskDeviceFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulerTaskDeviceFields in the database
        List<SchedulerTaskDeviceFields> schedulerTaskDeviceFieldsList = schedulerTaskDeviceFieldsRepository.findAll();
        assertThat(schedulerTaskDeviceFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerTaskDeviceFields in Elasticsearch
        verify(mockSchedulerTaskDeviceFieldsSearchRepository, times(0)).save(schedulerTaskDeviceFields);
    }

    @Test
    @Transactional
    void deleteSchedulerTaskDeviceFields() throws Exception {
        // Initialize the database
        schedulerTaskDeviceFieldsRepository.saveAndFlush(schedulerTaskDeviceFields);

        int databaseSizeBeforeDelete = schedulerTaskDeviceFieldsRepository.findAll().size();

        // Delete the schedulerTaskDeviceFields
        restSchedulerTaskDeviceFieldsMockMvc
            .perform(delete(ENTITY_API_URL_ID, schedulerTaskDeviceFields.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchedulerTaskDeviceFields> schedulerTaskDeviceFieldsList = schedulerTaskDeviceFieldsRepository.findAll();
        assertThat(schedulerTaskDeviceFieldsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SchedulerTaskDeviceFields in Elasticsearch
        verify(mockSchedulerTaskDeviceFieldsSearchRepository, times(1)).deleteById(schedulerTaskDeviceFields.getId());
    }

    @Test
    @Transactional
    void searchSchedulerTaskDeviceFields() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        schedulerTaskDeviceFieldsRepository.saveAndFlush(schedulerTaskDeviceFields);
        when(
            mockSchedulerTaskDeviceFieldsSearchRepository.search(
                queryStringQuery("id:" + schedulerTaskDeviceFields.getId()),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(schedulerTaskDeviceFields), PageRequest.of(0, 1), 1));

        // Search the schedulerTaskDeviceFields
        restSchedulerTaskDeviceFieldsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + schedulerTaskDeviceFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerTaskDeviceFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }
}
