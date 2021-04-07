package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.SchedulerFields;
import com.automation.professional.repository.SchedulerFieldsRepository;
import com.automation.professional.repository.search.SchedulerFieldsSearchRepository;
import com.automation.professional.service.dto.SchedulerFieldsDTO;
import com.automation.professional.service.mapper.SchedulerFieldsMapper;
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
 * Integration tests for the {@link SchedulerFieldsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchedulerFieldsResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/scheduler-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/scheduler-fields";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchedulerFieldsRepository schedulerFieldsRepository;

    @Autowired
    private SchedulerFieldsMapper schedulerFieldsMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.SchedulerFieldsSearchRepositoryMockConfiguration
     */
    @Autowired
    private SchedulerFieldsSearchRepository mockSchedulerFieldsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchedulerFieldsMockMvc;

    private SchedulerFields schedulerFields;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulerFields createEntity(EntityManager em) {
        SchedulerFields schedulerFields = new SchedulerFields().fieldName(DEFAULT_FIELD_NAME);
        return schedulerFields;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulerFields createUpdatedEntity(EntityManager em) {
        SchedulerFields schedulerFields = new SchedulerFields().fieldName(UPDATED_FIELD_NAME);
        return schedulerFields;
    }

    @BeforeEach
    public void initTest() {
        schedulerFields = createEntity(em);
    }

    @Test
    @Transactional
    void createSchedulerFields() throws Exception {
        int databaseSizeBeforeCreate = schedulerFieldsRepository.findAll().size();
        // Create the SchedulerFields
        SchedulerFieldsDTO schedulerFieldsDTO = schedulerFieldsMapper.toDto(schedulerFields);
        restSchedulerFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schedulerFieldsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchedulerFields in the database
        List<SchedulerFields> schedulerFieldsList = schedulerFieldsRepository.findAll();
        assertThat(schedulerFieldsList).hasSize(databaseSizeBeforeCreate + 1);
        SchedulerFields testSchedulerFields = schedulerFieldsList.get(schedulerFieldsList.size() - 1);
        assertThat(testSchedulerFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);

        // Validate the SchedulerFields in Elasticsearch
        verify(mockSchedulerFieldsSearchRepository, times(1)).save(testSchedulerFields);
    }

    @Test
    @Transactional
    void createSchedulerFieldsWithExistingId() throws Exception {
        // Create the SchedulerFields with an existing ID
        schedulerFields.setId(1L);
        SchedulerFieldsDTO schedulerFieldsDTO = schedulerFieldsMapper.toDto(schedulerFields);

        int databaseSizeBeforeCreate = schedulerFieldsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedulerFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schedulerFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerFields in the database
        List<SchedulerFields> schedulerFieldsList = schedulerFieldsRepository.findAll();
        assertThat(schedulerFieldsList).hasSize(databaseSizeBeforeCreate);

        // Validate the SchedulerFields in Elasticsearch
        verify(mockSchedulerFieldsSearchRepository, times(0)).save(schedulerFields);
    }

    @Test
    @Transactional
    void getAllSchedulerFields() throws Exception {
        // Initialize the database
        schedulerFieldsRepository.saveAndFlush(schedulerFields);

        // Get all the schedulerFieldsList
        restSchedulerFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }

    @Test
    @Transactional
    void getSchedulerFields() throws Exception {
        // Initialize the database
        schedulerFieldsRepository.saveAndFlush(schedulerFields);

        // Get the schedulerFields
        restSchedulerFieldsMockMvc
            .perform(get(ENTITY_API_URL_ID, schedulerFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schedulerFields.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSchedulerFields() throws Exception {
        // Get the schedulerFields
        restSchedulerFieldsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchedulerFields() throws Exception {
        // Initialize the database
        schedulerFieldsRepository.saveAndFlush(schedulerFields);

        int databaseSizeBeforeUpdate = schedulerFieldsRepository.findAll().size();

        // Update the schedulerFields
        SchedulerFields updatedSchedulerFields = schedulerFieldsRepository.findById(schedulerFields.getId()).get();
        // Disconnect from session so that the updates on updatedSchedulerFields are not directly saved in db
        em.detach(updatedSchedulerFields);
        updatedSchedulerFields.fieldName(UPDATED_FIELD_NAME);
        SchedulerFieldsDTO schedulerFieldsDTO = schedulerFieldsMapper.toDto(updatedSchedulerFields);

        restSchedulerFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulerFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerFieldsDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerFields in the database
        List<SchedulerFields> schedulerFieldsList = schedulerFieldsRepository.findAll();
        assertThat(schedulerFieldsList).hasSize(databaseSizeBeforeUpdate);
        SchedulerFields testSchedulerFields = schedulerFieldsList.get(schedulerFieldsList.size() - 1);
        assertThat(testSchedulerFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);

        // Validate the SchedulerFields in Elasticsearch
        verify(mockSchedulerFieldsSearchRepository).save(testSchedulerFields);
    }

    @Test
    @Transactional
    void putNonExistingSchedulerFields() throws Exception {
        int databaseSizeBeforeUpdate = schedulerFieldsRepository.findAll().size();
        schedulerFields.setId(count.incrementAndGet());

        // Create the SchedulerFields
        SchedulerFieldsDTO schedulerFieldsDTO = schedulerFieldsMapper.toDto(schedulerFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulerFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulerFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerFields in the database
        List<SchedulerFields> schedulerFieldsList = schedulerFieldsRepository.findAll();
        assertThat(schedulerFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerFields in Elasticsearch
        verify(mockSchedulerFieldsSearchRepository, times(0)).save(schedulerFields);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchedulerFields() throws Exception {
        int databaseSizeBeforeUpdate = schedulerFieldsRepository.findAll().size();
        schedulerFields.setId(count.incrementAndGet());

        // Create the SchedulerFields
        SchedulerFieldsDTO schedulerFieldsDTO = schedulerFieldsMapper.toDto(schedulerFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerFields in the database
        List<SchedulerFields> schedulerFieldsList = schedulerFieldsRepository.findAll();
        assertThat(schedulerFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerFields in Elasticsearch
        verify(mockSchedulerFieldsSearchRepository, times(0)).save(schedulerFields);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchedulerFields() throws Exception {
        int databaseSizeBeforeUpdate = schedulerFieldsRepository.findAll().size();
        schedulerFields.setId(count.incrementAndGet());

        // Create the SchedulerFields
        SchedulerFieldsDTO schedulerFieldsDTO = schedulerFieldsMapper.toDto(schedulerFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerFieldsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schedulerFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulerFields in the database
        List<SchedulerFields> schedulerFieldsList = schedulerFieldsRepository.findAll();
        assertThat(schedulerFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerFields in Elasticsearch
        verify(mockSchedulerFieldsSearchRepository, times(0)).save(schedulerFields);
    }

    @Test
    @Transactional
    void partialUpdateSchedulerFieldsWithPatch() throws Exception {
        // Initialize the database
        schedulerFieldsRepository.saveAndFlush(schedulerFields);

        int databaseSizeBeforeUpdate = schedulerFieldsRepository.findAll().size();

        // Update the schedulerFields using partial update
        SchedulerFields partialUpdatedSchedulerFields = new SchedulerFields();
        partialUpdatedSchedulerFields.setId(schedulerFields.getId());

        restSchedulerFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulerFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulerFields))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerFields in the database
        List<SchedulerFields> schedulerFieldsList = schedulerFieldsRepository.findAll();
        assertThat(schedulerFieldsList).hasSize(databaseSizeBeforeUpdate);
        SchedulerFields testSchedulerFields = schedulerFieldsList.get(schedulerFieldsList.size() - 1);
        assertThat(testSchedulerFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSchedulerFieldsWithPatch() throws Exception {
        // Initialize the database
        schedulerFieldsRepository.saveAndFlush(schedulerFields);

        int databaseSizeBeforeUpdate = schedulerFieldsRepository.findAll().size();

        // Update the schedulerFields using partial update
        SchedulerFields partialUpdatedSchedulerFields = new SchedulerFields();
        partialUpdatedSchedulerFields.setId(schedulerFields.getId());

        partialUpdatedSchedulerFields.fieldName(UPDATED_FIELD_NAME);

        restSchedulerFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulerFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulerFields))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerFields in the database
        List<SchedulerFields> schedulerFieldsList = schedulerFieldsRepository.findAll();
        assertThat(schedulerFieldsList).hasSize(databaseSizeBeforeUpdate);
        SchedulerFields testSchedulerFields = schedulerFieldsList.get(schedulerFieldsList.size() - 1);
        assertThat(testSchedulerFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSchedulerFields() throws Exception {
        int databaseSizeBeforeUpdate = schedulerFieldsRepository.findAll().size();
        schedulerFields.setId(count.incrementAndGet());

        // Create the SchedulerFields
        SchedulerFieldsDTO schedulerFieldsDTO = schedulerFieldsMapper.toDto(schedulerFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulerFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schedulerFieldsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerFields in the database
        List<SchedulerFields> schedulerFieldsList = schedulerFieldsRepository.findAll();
        assertThat(schedulerFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerFields in Elasticsearch
        verify(mockSchedulerFieldsSearchRepository, times(0)).save(schedulerFields);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchedulerFields() throws Exception {
        int databaseSizeBeforeUpdate = schedulerFieldsRepository.findAll().size();
        schedulerFields.setId(count.incrementAndGet());

        // Create the SchedulerFields
        SchedulerFieldsDTO schedulerFieldsDTO = schedulerFieldsMapper.toDto(schedulerFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerFields in the database
        List<SchedulerFields> schedulerFieldsList = schedulerFieldsRepository.findAll();
        assertThat(schedulerFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerFields in Elasticsearch
        verify(mockSchedulerFieldsSearchRepository, times(0)).save(schedulerFields);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchedulerFields() throws Exception {
        int databaseSizeBeforeUpdate = schedulerFieldsRepository.findAll().size();
        schedulerFields.setId(count.incrementAndGet());

        // Create the SchedulerFields
        SchedulerFieldsDTO schedulerFieldsDTO = schedulerFieldsMapper.toDto(schedulerFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulerFields in the database
        List<SchedulerFields> schedulerFieldsList = schedulerFieldsRepository.findAll();
        assertThat(schedulerFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SchedulerFields in Elasticsearch
        verify(mockSchedulerFieldsSearchRepository, times(0)).save(schedulerFields);
    }

    @Test
    @Transactional
    void deleteSchedulerFields() throws Exception {
        // Initialize the database
        schedulerFieldsRepository.saveAndFlush(schedulerFields);

        int databaseSizeBeforeDelete = schedulerFieldsRepository.findAll().size();

        // Delete the schedulerFields
        restSchedulerFieldsMockMvc
            .perform(delete(ENTITY_API_URL_ID, schedulerFields.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchedulerFields> schedulerFieldsList = schedulerFieldsRepository.findAll();
        assertThat(schedulerFieldsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SchedulerFields in Elasticsearch
        verify(mockSchedulerFieldsSearchRepository, times(1)).deleteById(schedulerFields.getId());
    }

    @Test
    @Transactional
    void searchSchedulerFields() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        schedulerFieldsRepository.saveAndFlush(schedulerFields);
        when(mockSchedulerFieldsSearchRepository.search(queryStringQuery("id:" + schedulerFields.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(schedulerFields), PageRequest.of(0, 1), 1));

        // Search the schedulerFields
        restSchedulerFieldsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + schedulerFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }
}
