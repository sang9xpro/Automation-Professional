package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.HistoryFields;
import com.automation.professional.repository.HistoryFieldsRepository;
import com.automation.professional.repository.search.HistoryFieldsSearchRepository;
import com.automation.professional.service.dto.HistoryFieldsDTO;
import com.automation.professional.service.mapper.HistoryFieldsMapper;
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
 * Integration tests for the {@link HistoryFieldsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HistoryFieldsResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/history-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/history-fields";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoryFieldsRepository historyFieldsRepository;

    @Autowired
    private HistoryFieldsMapper historyFieldsMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.HistoryFieldsSearchRepositoryMockConfiguration
     */
    @Autowired
    private HistoryFieldsSearchRepository mockHistoryFieldsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoryFieldsMockMvc;

    private HistoryFields historyFields;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoryFields createEntity(EntityManager em) {
        HistoryFields historyFields = new HistoryFields().fieldName(DEFAULT_FIELD_NAME);
        return historyFields;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoryFields createUpdatedEntity(EntityManager em) {
        HistoryFields historyFields = new HistoryFields().fieldName(UPDATED_FIELD_NAME);
        return historyFields;
    }

    @BeforeEach
    public void initTest() {
        historyFields = createEntity(em);
    }

    @Test
    @Transactional
    void createHistoryFields() throws Exception {
        int databaseSizeBeforeCreate = historyFieldsRepository.findAll().size();
        // Create the HistoryFields
        HistoryFieldsDTO historyFieldsDTO = historyFieldsMapper.toDto(historyFields);
        restHistoryFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyFieldsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HistoryFields in the database
        List<HistoryFields> historyFieldsList = historyFieldsRepository.findAll();
        assertThat(historyFieldsList).hasSize(databaseSizeBeforeCreate + 1);
        HistoryFields testHistoryFields = historyFieldsList.get(historyFieldsList.size() - 1);
        assertThat(testHistoryFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);

        // Validate the HistoryFields in Elasticsearch
        verify(mockHistoryFieldsSearchRepository, times(1)).save(testHistoryFields);
    }

    @Test
    @Transactional
    void createHistoryFieldsWithExistingId() throws Exception {
        // Create the HistoryFields with an existing ID
        historyFields.setId(1L);
        HistoryFieldsDTO historyFieldsDTO = historyFieldsMapper.toDto(historyFields);

        int databaseSizeBeforeCreate = historyFieldsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoryFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryFields in the database
        List<HistoryFields> historyFieldsList = historyFieldsRepository.findAll();
        assertThat(historyFieldsList).hasSize(databaseSizeBeforeCreate);

        // Validate the HistoryFields in Elasticsearch
        verify(mockHistoryFieldsSearchRepository, times(0)).save(historyFields);
    }

    @Test
    @Transactional
    void getAllHistoryFields() throws Exception {
        // Initialize the database
        historyFieldsRepository.saveAndFlush(historyFields);

        // Get all the historyFieldsList
        restHistoryFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historyFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }

    @Test
    @Transactional
    void getHistoryFields() throws Exception {
        // Initialize the database
        historyFieldsRepository.saveAndFlush(historyFields);

        // Get the historyFields
        restHistoryFieldsMockMvc
            .perform(get(ENTITY_API_URL_ID, historyFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historyFields.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }

    @Test
    @Transactional
    void getNonExistingHistoryFields() throws Exception {
        // Get the historyFields
        restHistoryFieldsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHistoryFields() throws Exception {
        // Initialize the database
        historyFieldsRepository.saveAndFlush(historyFields);

        int databaseSizeBeforeUpdate = historyFieldsRepository.findAll().size();

        // Update the historyFields
        HistoryFields updatedHistoryFields = historyFieldsRepository.findById(historyFields.getId()).get();
        // Disconnect from session so that the updates on updatedHistoryFields are not directly saved in db
        em.detach(updatedHistoryFields);
        updatedHistoryFields.fieldName(UPDATED_FIELD_NAME);
        HistoryFieldsDTO historyFieldsDTO = historyFieldsMapper.toDto(updatedHistoryFields);

        restHistoryFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historyFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyFieldsDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoryFields in the database
        List<HistoryFields> historyFieldsList = historyFieldsRepository.findAll();
        assertThat(historyFieldsList).hasSize(databaseSizeBeforeUpdate);
        HistoryFields testHistoryFields = historyFieldsList.get(historyFieldsList.size() - 1);
        assertThat(testHistoryFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);

        // Validate the HistoryFields in Elasticsearch
        verify(mockHistoryFieldsSearchRepository).save(testHistoryFields);
    }

    @Test
    @Transactional
    void putNonExistingHistoryFields() throws Exception {
        int databaseSizeBeforeUpdate = historyFieldsRepository.findAll().size();
        historyFields.setId(count.incrementAndGet());

        // Create the HistoryFields
        HistoryFieldsDTO historyFieldsDTO = historyFieldsMapper.toDto(historyFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historyFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryFields in the database
        List<HistoryFields> historyFieldsList = historyFieldsRepository.findAll();
        assertThat(historyFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoryFields in Elasticsearch
        verify(mockHistoryFieldsSearchRepository, times(0)).save(historyFields);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoryFields() throws Exception {
        int databaseSizeBeforeUpdate = historyFieldsRepository.findAll().size();
        historyFields.setId(count.incrementAndGet());

        // Create the HistoryFields
        HistoryFieldsDTO historyFieldsDTO = historyFieldsMapper.toDto(historyFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryFields in the database
        List<HistoryFields> historyFieldsList = historyFieldsRepository.findAll();
        assertThat(historyFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoryFields in Elasticsearch
        verify(mockHistoryFieldsSearchRepository, times(0)).save(historyFields);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoryFields() throws Exception {
        int databaseSizeBeforeUpdate = historyFieldsRepository.findAll().size();
        historyFields.setId(count.incrementAndGet());

        // Create the HistoryFields
        HistoryFieldsDTO historyFieldsDTO = historyFieldsMapper.toDto(historyFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryFieldsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoryFields in the database
        List<HistoryFields> historyFieldsList = historyFieldsRepository.findAll();
        assertThat(historyFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoryFields in Elasticsearch
        verify(mockHistoryFieldsSearchRepository, times(0)).save(historyFields);
    }

    @Test
    @Transactional
    void partialUpdateHistoryFieldsWithPatch() throws Exception {
        // Initialize the database
        historyFieldsRepository.saveAndFlush(historyFields);

        int databaseSizeBeforeUpdate = historyFieldsRepository.findAll().size();

        // Update the historyFields using partial update
        HistoryFields partialUpdatedHistoryFields = new HistoryFields();
        partialUpdatedHistoryFields.setId(historyFields.getId());

        restHistoryFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoryFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoryFields))
            )
            .andExpect(status().isOk());

        // Validate the HistoryFields in the database
        List<HistoryFields> historyFieldsList = historyFieldsRepository.findAll();
        assertThat(historyFieldsList).hasSize(databaseSizeBeforeUpdate);
        HistoryFields testHistoryFields = historyFieldsList.get(historyFieldsList.size() - 1);
        assertThat(testHistoryFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
    }

    @Test
    @Transactional
    void fullUpdateHistoryFieldsWithPatch() throws Exception {
        // Initialize the database
        historyFieldsRepository.saveAndFlush(historyFields);

        int databaseSizeBeforeUpdate = historyFieldsRepository.findAll().size();

        // Update the historyFields using partial update
        HistoryFields partialUpdatedHistoryFields = new HistoryFields();
        partialUpdatedHistoryFields.setId(historyFields.getId());

        partialUpdatedHistoryFields.fieldName(UPDATED_FIELD_NAME);

        restHistoryFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoryFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoryFields))
            )
            .andExpect(status().isOk());

        // Validate the HistoryFields in the database
        List<HistoryFields> historyFieldsList = historyFieldsRepository.findAll();
        assertThat(historyFieldsList).hasSize(databaseSizeBeforeUpdate);
        HistoryFields testHistoryFields = historyFieldsList.get(historyFieldsList.size() - 1);
        assertThat(testHistoryFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingHistoryFields() throws Exception {
        int databaseSizeBeforeUpdate = historyFieldsRepository.findAll().size();
        historyFields.setId(count.incrementAndGet());

        // Create the HistoryFields
        HistoryFieldsDTO historyFieldsDTO = historyFieldsMapper.toDto(historyFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historyFieldsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryFields in the database
        List<HistoryFields> historyFieldsList = historyFieldsRepository.findAll();
        assertThat(historyFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoryFields in Elasticsearch
        verify(mockHistoryFieldsSearchRepository, times(0)).save(historyFields);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoryFields() throws Exception {
        int databaseSizeBeforeUpdate = historyFieldsRepository.findAll().size();
        historyFields.setId(count.incrementAndGet());

        // Create the HistoryFields
        HistoryFieldsDTO historyFieldsDTO = historyFieldsMapper.toDto(historyFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryFields in the database
        List<HistoryFields> historyFieldsList = historyFieldsRepository.findAll();
        assertThat(historyFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoryFields in Elasticsearch
        verify(mockHistoryFieldsSearchRepository, times(0)).save(historyFields);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoryFields() throws Exception {
        int databaseSizeBeforeUpdate = historyFieldsRepository.findAll().size();
        historyFields.setId(count.incrementAndGet());

        // Create the HistoryFields
        HistoryFieldsDTO historyFieldsDTO = historyFieldsMapper.toDto(historyFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoryFields in the database
        List<HistoryFields> historyFieldsList = historyFieldsRepository.findAll();
        assertThat(historyFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoryFields in Elasticsearch
        verify(mockHistoryFieldsSearchRepository, times(0)).save(historyFields);
    }

    @Test
    @Transactional
    void deleteHistoryFields() throws Exception {
        // Initialize the database
        historyFieldsRepository.saveAndFlush(historyFields);

        int databaseSizeBeforeDelete = historyFieldsRepository.findAll().size();

        // Delete the historyFields
        restHistoryFieldsMockMvc
            .perform(delete(ENTITY_API_URL_ID, historyFields.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoryFields> historyFieldsList = historyFieldsRepository.findAll();
        assertThat(historyFieldsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HistoryFields in Elasticsearch
        verify(mockHistoryFieldsSearchRepository, times(1)).deleteById(historyFields.getId());
    }

    @Test
    @Transactional
    void searchHistoryFields() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        historyFieldsRepository.saveAndFlush(historyFields);
        when(mockHistoryFieldsSearchRepository.search(queryStringQuery("id:" + historyFields.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(historyFields), PageRequest.of(0, 1), 1));

        // Search the historyFields
        restHistoryFieldsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + historyFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historyFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }
}
