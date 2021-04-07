package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.LoggersFields;
import com.automation.professional.repository.LoggersFieldsRepository;
import com.automation.professional.repository.search.LoggersFieldsSearchRepository;
import com.automation.professional.service.dto.LoggersFieldsDTO;
import com.automation.professional.service.mapper.LoggersFieldsMapper;
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
 * Integration tests for the {@link LoggersFieldsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LoggersFieldsResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/loggers-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/loggers-fields";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoggersFieldsRepository loggersFieldsRepository;

    @Autowired
    private LoggersFieldsMapper loggersFieldsMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.LoggersFieldsSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoggersFieldsSearchRepository mockLoggersFieldsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoggersFieldsMockMvc;

    private LoggersFields loggersFields;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoggersFields createEntity(EntityManager em) {
        LoggersFields loggersFields = new LoggersFields().fieldName(DEFAULT_FIELD_NAME);
        return loggersFields;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoggersFields createUpdatedEntity(EntityManager em) {
        LoggersFields loggersFields = new LoggersFields().fieldName(UPDATED_FIELD_NAME);
        return loggersFields;
    }

    @BeforeEach
    public void initTest() {
        loggersFields = createEntity(em);
    }

    @Test
    @Transactional
    void createLoggersFields() throws Exception {
        int databaseSizeBeforeCreate = loggersFieldsRepository.findAll().size();
        // Create the LoggersFields
        LoggersFieldsDTO loggersFieldsDTO = loggersFieldsMapper.toDto(loggersFields);
        restLoggersFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loggersFieldsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoggersFields in the database
        List<LoggersFields> loggersFieldsList = loggersFieldsRepository.findAll();
        assertThat(loggersFieldsList).hasSize(databaseSizeBeforeCreate + 1);
        LoggersFields testLoggersFields = loggersFieldsList.get(loggersFieldsList.size() - 1);
        assertThat(testLoggersFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);

        // Validate the LoggersFields in Elasticsearch
        verify(mockLoggersFieldsSearchRepository, times(1)).save(testLoggersFields);
    }

    @Test
    @Transactional
    void createLoggersFieldsWithExistingId() throws Exception {
        // Create the LoggersFields with an existing ID
        loggersFields.setId(1L);
        LoggersFieldsDTO loggersFieldsDTO = loggersFieldsMapper.toDto(loggersFields);

        int databaseSizeBeforeCreate = loggersFieldsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoggersFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loggersFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoggersFields in the database
        List<LoggersFields> loggersFieldsList = loggersFieldsRepository.findAll();
        assertThat(loggersFieldsList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoggersFields in Elasticsearch
        verify(mockLoggersFieldsSearchRepository, times(0)).save(loggersFields);
    }

    @Test
    @Transactional
    void getAllLoggersFields() throws Exception {
        // Initialize the database
        loggersFieldsRepository.saveAndFlush(loggersFields);

        // Get all the loggersFieldsList
        restLoggersFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loggersFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }

    @Test
    @Transactional
    void getLoggersFields() throws Exception {
        // Initialize the database
        loggersFieldsRepository.saveAndFlush(loggersFields);

        // Get the loggersFields
        restLoggersFieldsMockMvc
            .perform(get(ENTITY_API_URL_ID, loggersFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loggersFields.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }

    @Test
    @Transactional
    void getNonExistingLoggersFields() throws Exception {
        // Get the loggersFields
        restLoggersFieldsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoggersFields() throws Exception {
        // Initialize the database
        loggersFieldsRepository.saveAndFlush(loggersFields);

        int databaseSizeBeforeUpdate = loggersFieldsRepository.findAll().size();

        // Update the loggersFields
        LoggersFields updatedLoggersFields = loggersFieldsRepository.findById(loggersFields.getId()).get();
        // Disconnect from session so that the updates on updatedLoggersFields are not directly saved in db
        em.detach(updatedLoggersFields);
        updatedLoggersFields.fieldName(UPDATED_FIELD_NAME);
        LoggersFieldsDTO loggersFieldsDTO = loggersFieldsMapper.toDto(updatedLoggersFields);

        restLoggersFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loggersFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loggersFieldsDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoggersFields in the database
        List<LoggersFields> loggersFieldsList = loggersFieldsRepository.findAll();
        assertThat(loggersFieldsList).hasSize(databaseSizeBeforeUpdate);
        LoggersFields testLoggersFields = loggersFieldsList.get(loggersFieldsList.size() - 1);
        assertThat(testLoggersFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);

        // Validate the LoggersFields in Elasticsearch
        verify(mockLoggersFieldsSearchRepository).save(testLoggersFields);
    }

    @Test
    @Transactional
    void putNonExistingLoggersFields() throws Exception {
        int databaseSizeBeforeUpdate = loggersFieldsRepository.findAll().size();
        loggersFields.setId(count.incrementAndGet());

        // Create the LoggersFields
        LoggersFieldsDTO loggersFieldsDTO = loggersFieldsMapper.toDto(loggersFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoggersFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loggersFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loggersFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoggersFields in the database
        List<LoggersFields> loggersFieldsList = loggersFieldsRepository.findAll();
        assertThat(loggersFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoggersFields in Elasticsearch
        verify(mockLoggersFieldsSearchRepository, times(0)).save(loggersFields);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoggersFields() throws Exception {
        int databaseSizeBeforeUpdate = loggersFieldsRepository.findAll().size();
        loggersFields.setId(count.incrementAndGet());

        // Create the LoggersFields
        LoggersFieldsDTO loggersFieldsDTO = loggersFieldsMapper.toDto(loggersFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoggersFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loggersFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoggersFields in the database
        List<LoggersFields> loggersFieldsList = loggersFieldsRepository.findAll();
        assertThat(loggersFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoggersFields in Elasticsearch
        verify(mockLoggersFieldsSearchRepository, times(0)).save(loggersFields);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoggersFields() throws Exception {
        int databaseSizeBeforeUpdate = loggersFieldsRepository.findAll().size();
        loggersFields.setId(count.incrementAndGet());

        // Create the LoggersFields
        LoggersFieldsDTO loggersFieldsDTO = loggersFieldsMapper.toDto(loggersFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoggersFieldsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loggersFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoggersFields in the database
        List<LoggersFields> loggersFieldsList = loggersFieldsRepository.findAll();
        assertThat(loggersFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoggersFields in Elasticsearch
        verify(mockLoggersFieldsSearchRepository, times(0)).save(loggersFields);
    }

    @Test
    @Transactional
    void partialUpdateLoggersFieldsWithPatch() throws Exception {
        // Initialize the database
        loggersFieldsRepository.saveAndFlush(loggersFields);

        int databaseSizeBeforeUpdate = loggersFieldsRepository.findAll().size();

        // Update the loggersFields using partial update
        LoggersFields partialUpdatedLoggersFields = new LoggersFields();
        partialUpdatedLoggersFields.setId(loggersFields.getId());

        partialUpdatedLoggersFields.fieldName(UPDATED_FIELD_NAME);

        restLoggersFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoggersFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoggersFields))
            )
            .andExpect(status().isOk());

        // Validate the LoggersFields in the database
        List<LoggersFields> loggersFieldsList = loggersFieldsRepository.findAll();
        assertThat(loggersFieldsList).hasSize(databaseSizeBeforeUpdate);
        LoggersFields testLoggersFields = loggersFieldsList.get(loggersFieldsList.size() - 1);
        assertThat(testLoggersFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void fullUpdateLoggersFieldsWithPatch() throws Exception {
        // Initialize the database
        loggersFieldsRepository.saveAndFlush(loggersFields);

        int databaseSizeBeforeUpdate = loggersFieldsRepository.findAll().size();

        // Update the loggersFields using partial update
        LoggersFields partialUpdatedLoggersFields = new LoggersFields();
        partialUpdatedLoggersFields.setId(loggersFields.getId());

        partialUpdatedLoggersFields.fieldName(UPDATED_FIELD_NAME);

        restLoggersFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoggersFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoggersFields))
            )
            .andExpect(status().isOk());

        // Validate the LoggersFields in the database
        List<LoggersFields> loggersFieldsList = loggersFieldsRepository.findAll();
        assertThat(loggersFieldsList).hasSize(databaseSizeBeforeUpdate);
        LoggersFields testLoggersFields = loggersFieldsList.get(loggersFieldsList.size() - 1);
        assertThat(testLoggersFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingLoggersFields() throws Exception {
        int databaseSizeBeforeUpdate = loggersFieldsRepository.findAll().size();
        loggersFields.setId(count.incrementAndGet());

        // Create the LoggersFields
        LoggersFieldsDTO loggersFieldsDTO = loggersFieldsMapper.toDto(loggersFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoggersFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loggersFieldsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loggersFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoggersFields in the database
        List<LoggersFields> loggersFieldsList = loggersFieldsRepository.findAll();
        assertThat(loggersFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoggersFields in Elasticsearch
        verify(mockLoggersFieldsSearchRepository, times(0)).save(loggersFields);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoggersFields() throws Exception {
        int databaseSizeBeforeUpdate = loggersFieldsRepository.findAll().size();
        loggersFields.setId(count.incrementAndGet());

        // Create the LoggersFields
        LoggersFieldsDTO loggersFieldsDTO = loggersFieldsMapper.toDto(loggersFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoggersFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loggersFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoggersFields in the database
        List<LoggersFields> loggersFieldsList = loggersFieldsRepository.findAll();
        assertThat(loggersFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoggersFields in Elasticsearch
        verify(mockLoggersFieldsSearchRepository, times(0)).save(loggersFields);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoggersFields() throws Exception {
        int databaseSizeBeforeUpdate = loggersFieldsRepository.findAll().size();
        loggersFields.setId(count.incrementAndGet());

        // Create the LoggersFields
        LoggersFieldsDTO loggersFieldsDTO = loggersFieldsMapper.toDto(loggersFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoggersFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loggersFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoggersFields in the database
        List<LoggersFields> loggersFieldsList = loggersFieldsRepository.findAll();
        assertThat(loggersFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoggersFields in Elasticsearch
        verify(mockLoggersFieldsSearchRepository, times(0)).save(loggersFields);
    }

    @Test
    @Transactional
    void deleteLoggersFields() throws Exception {
        // Initialize the database
        loggersFieldsRepository.saveAndFlush(loggersFields);

        int databaseSizeBeforeDelete = loggersFieldsRepository.findAll().size();

        // Delete the loggersFields
        restLoggersFieldsMockMvc
            .perform(delete(ENTITY_API_URL_ID, loggersFields.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoggersFields> loggersFieldsList = loggersFieldsRepository.findAll();
        assertThat(loggersFieldsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoggersFields in Elasticsearch
        verify(mockLoggersFieldsSearchRepository, times(1)).deleteById(loggersFields.getId());
    }

    @Test
    @Transactional
    void searchLoggersFields() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loggersFieldsRepository.saveAndFlush(loggersFields);
        when(mockLoggersFieldsSearchRepository.search(queryStringQuery("id:" + loggersFields.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(loggersFields), PageRequest.of(0, 1), 1));

        // Search the loggersFields
        restLoggersFieldsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + loggersFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loggersFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }
}
