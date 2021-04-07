package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.HistoryValues;
import com.automation.professional.repository.HistoryValuesRepository;
import com.automation.professional.repository.search.HistoryValuesSearchRepository;
import com.automation.professional.service.dto.HistoryValuesDTO;
import com.automation.professional.service.mapper.HistoryValuesMapper;
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
 * Integration tests for the {@link HistoryValuesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HistoryValuesResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/history-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/history-values";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoryValuesRepository historyValuesRepository;

    @Autowired
    private HistoryValuesMapper historyValuesMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.HistoryValuesSearchRepositoryMockConfiguration
     */
    @Autowired
    private HistoryValuesSearchRepository mockHistoryValuesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoryValuesMockMvc;

    private HistoryValues historyValues;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoryValues createEntity(EntityManager em) {
        HistoryValues historyValues = new HistoryValues().value(DEFAULT_VALUE);
        return historyValues;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoryValues createUpdatedEntity(EntityManager em) {
        HistoryValues historyValues = new HistoryValues().value(UPDATED_VALUE);
        return historyValues;
    }

    @BeforeEach
    public void initTest() {
        historyValues = createEntity(em);
    }

    @Test
    @Transactional
    void createHistoryValues() throws Exception {
        int databaseSizeBeforeCreate = historyValuesRepository.findAll().size();
        // Create the HistoryValues
        HistoryValuesDTO historyValuesDTO = historyValuesMapper.toDto(historyValues);
        restHistoryValuesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyValuesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HistoryValues in the database
        List<HistoryValues> historyValuesList = historyValuesRepository.findAll();
        assertThat(historyValuesList).hasSize(databaseSizeBeforeCreate + 1);
        HistoryValues testHistoryValues = historyValuesList.get(historyValuesList.size() - 1);
        assertThat(testHistoryValues.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the HistoryValues in Elasticsearch
        verify(mockHistoryValuesSearchRepository, times(1)).save(testHistoryValues);
    }

    @Test
    @Transactional
    void createHistoryValuesWithExistingId() throws Exception {
        // Create the HistoryValues with an existing ID
        historyValues.setId(1L);
        HistoryValuesDTO historyValuesDTO = historyValuesMapper.toDto(historyValues);

        int databaseSizeBeforeCreate = historyValuesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoryValuesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryValues in the database
        List<HistoryValues> historyValuesList = historyValuesRepository.findAll();
        assertThat(historyValuesList).hasSize(databaseSizeBeforeCreate);

        // Validate the HistoryValues in Elasticsearch
        verify(mockHistoryValuesSearchRepository, times(0)).save(historyValues);
    }

    @Test
    @Transactional
    void getAllHistoryValues() throws Exception {
        // Initialize the database
        historyValuesRepository.saveAndFlush(historyValues);

        // Get all the historyValuesList
        restHistoryValuesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historyValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getHistoryValues() throws Exception {
        // Initialize the database
        historyValuesRepository.saveAndFlush(historyValues);

        // Get the historyValues
        restHistoryValuesMockMvc
            .perform(get(ENTITY_API_URL_ID, historyValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historyValues.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingHistoryValues() throws Exception {
        // Get the historyValues
        restHistoryValuesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHistoryValues() throws Exception {
        // Initialize the database
        historyValuesRepository.saveAndFlush(historyValues);

        int databaseSizeBeforeUpdate = historyValuesRepository.findAll().size();

        // Update the historyValues
        HistoryValues updatedHistoryValues = historyValuesRepository.findById(historyValues.getId()).get();
        // Disconnect from session so that the updates on updatedHistoryValues are not directly saved in db
        em.detach(updatedHistoryValues);
        updatedHistoryValues.value(UPDATED_VALUE);
        HistoryValuesDTO historyValuesDTO = historyValuesMapper.toDto(updatedHistoryValues);

        restHistoryValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historyValuesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyValuesDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoryValues in the database
        List<HistoryValues> historyValuesList = historyValuesRepository.findAll();
        assertThat(historyValuesList).hasSize(databaseSizeBeforeUpdate);
        HistoryValues testHistoryValues = historyValuesList.get(historyValuesList.size() - 1);
        assertThat(testHistoryValues.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the HistoryValues in Elasticsearch
        verify(mockHistoryValuesSearchRepository).save(testHistoryValues);
    }

    @Test
    @Transactional
    void putNonExistingHistoryValues() throws Exception {
        int databaseSizeBeforeUpdate = historyValuesRepository.findAll().size();
        historyValues.setId(count.incrementAndGet());

        // Create the HistoryValues
        HistoryValuesDTO historyValuesDTO = historyValuesMapper.toDto(historyValues);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historyValuesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryValues in the database
        List<HistoryValues> historyValuesList = historyValuesRepository.findAll();
        assertThat(historyValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoryValues in Elasticsearch
        verify(mockHistoryValuesSearchRepository, times(0)).save(historyValues);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoryValues() throws Exception {
        int databaseSizeBeforeUpdate = historyValuesRepository.findAll().size();
        historyValues.setId(count.incrementAndGet());

        // Create the HistoryValues
        HistoryValuesDTO historyValuesDTO = historyValuesMapper.toDto(historyValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryValues in the database
        List<HistoryValues> historyValuesList = historyValuesRepository.findAll();
        assertThat(historyValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoryValues in Elasticsearch
        verify(mockHistoryValuesSearchRepository, times(0)).save(historyValues);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoryValues() throws Exception {
        int databaseSizeBeforeUpdate = historyValuesRepository.findAll().size();
        historyValues.setId(count.incrementAndGet());

        // Create the HistoryValues
        HistoryValuesDTO historyValuesDTO = historyValuesMapper.toDto(historyValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryValuesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyValuesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoryValues in the database
        List<HistoryValues> historyValuesList = historyValuesRepository.findAll();
        assertThat(historyValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoryValues in Elasticsearch
        verify(mockHistoryValuesSearchRepository, times(0)).save(historyValues);
    }

    @Test
    @Transactional
    void partialUpdateHistoryValuesWithPatch() throws Exception {
        // Initialize the database
        historyValuesRepository.saveAndFlush(historyValues);

        int databaseSizeBeforeUpdate = historyValuesRepository.findAll().size();

        // Update the historyValues using partial update
        HistoryValues partialUpdatedHistoryValues = new HistoryValues();
        partialUpdatedHistoryValues.setId(historyValues.getId());

        restHistoryValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoryValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoryValues))
            )
            .andExpect(status().isOk());

        // Validate the HistoryValues in the database
        List<HistoryValues> historyValuesList = historyValuesRepository.findAll();
        assertThat(historyValuesList).hasSize(databaseSizeBeforeUpdate);
        HistoryValues testHistoryValues = historyValuesList.get(historyValuesList.size() - 1);
        assertThat(testHistoryValues.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateHistoryValuesWithPatch() throws Exception {
        // Initialize the database
        historyValuesRepository.saveAndFlush(historyValues);

        int databaseSizeBeforeUpdate = historyValuesRepository.findAll().size();

        // Update the historyValues using partial update
        HistoryValues partialUpdatedHistoryValues = new HistoryValues();
        partialUpdatedHistoryValues.setId(historyValues.getId());

        partialUpdatedHistoryValues.value(UPDATED_VALUE);

        restHistoryValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoryValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoryValues))
            )
            .andExpect(status().isOk());

        // Validate the HistoryValues in the database
        List<HistoryValues> historyValuesList = historyValuesRepository.findAll();
        assertThat(historyValuesList).hasSize(databaseSizeBeforeUpdate);
        HistoryValues testHistoryValues = historyValuesList.get(historyValuesList.size() - 1);
        assertThat(testHistoryValues.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingHistoryValues() throws Exception {
        int databaseSizeBeforeUpdate = historyValuesRepository.findAll().size();
        historyValues.setId(count.incrementAndGet());

        // Create the HistoryValues
        HistoryValuesDTO historyValuesDTO = historyValuesMapper.toDto(historyValues);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historyValuesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryValues in the database
        List<HistoryValues> historyValuesList = historyValuesRepository.findAll();
        assertThat(historyValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoryValues in Elasticsearch
        verify(mockHistoryValuesSearchRepository, times(0)).save(historyValues);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoryValues() throws Exception {
        int databaseSizeBeforeUpdate = historyValuesRepository.findAll().size();
        historyValues.setId(count.incrementAndGet());

        // Create the HistoryValues
        HistoryValuesDTO historyValuesDTO = historyValuesMapper.toDto(historyValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryValues in the database
        List<HistoryValues> historyValuesList = historyValuesRepository.findAll();
        assertThat(historyValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoryValues in Elasticsearch
        verify(mockHistoryValuesSearchRepository, times(0)).save(historyValues);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoryValues() throws Exception {
        int databaseSizeBeforeUpdate = historyValuesRepository.findAll().size();
        historyValues.setId(count.incrementAndGet());

        // Create the HistoryValues
        HistoryValuesDTO historyValuesDTO = historyValuesMapper.toDto(historyValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryValuesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyValuesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoryValues in the database
        List<HistoryValues> historyValuesList = historyValuesRepository.findAll();
        assertThat(historyValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HistoryValues in Elasticsearch
        verify(mockHistoryValuesSearchRepository, times(0)).save(historyValues);
    }

    @Test
    @Transactional
    void deleteHistoryValues() throws Exception {
        // Initialize the database
        historyValuesRepository.saveAndFlush(historyValues);

        int databaseSizeBeforeDelete = historyValuesRepository.findAll().size();

        // Delete the historyValues
        restHistoryValuesMockMvc
            .perform(delete(ENTITY_API_URL_ID, historyValues.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoryValues> historyValuesList = historyValuesRepository.findAll();
        assertThat(historyValuesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HistoryValues in Elasticsearch
        verify(mockHistoryValuesSearchRepository, times(1)).deleteById(historyValues.getId());
    }

    @Test
    @Transactional
    void searchHistoryValues() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        historyValuesRepository.saveAndFlush(historyValues);
        when(mockHistoryValuesSearchRepository.search(queryStringQuery("id:" + historyValues.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(historyValues), PageRequest.of(0, 1), 1));

        // Search the historyValues
        restHistoryValuesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + historyValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historyValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
}
