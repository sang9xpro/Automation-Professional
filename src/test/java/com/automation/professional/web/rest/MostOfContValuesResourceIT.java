package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.MostOfContValues;
import com.automation.professional.repository.MostOfContValuesRepository;
import com.automation.professional.repository.search.MostOfContValuesSearchRepository;
import com.automation.professional.service.dto.MostOfContValuesDTO;
import com.automation.professional.service.mapper.MostOfContValuesMapper;
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
 * Integration tests for the {@link MostOfContValuesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MostOfContValuesResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/most-of-cont-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/most-of-cont-values";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MostOfContValuesRepository mostOfContValuesRepository;

    @Autowired
    private MostOfContValuesMapper mostOfContValuesMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.MostOfContValuesSearchRepositoryMockConfiguration
     */
    @Autowired
    private MostOfContValuesSearchRepository mockMostOfContValuesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMostOfContValuesMockMvc;

    private MostOfContValues mostOfContValues;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MostOfContValues createEntity(EntityManager em) {
        MostOfContValues mostOfContValues = new MostOfContValues().value(DEFAULT_VALUE);
        return mostOfContValues;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MostOfContValues createUpdatedEntity(EntityManager em) {
        MostOfContValues mostOfContValues = new MostOfContValues().value(UPDATED_VALUE);
        return mostOfContValues;
    }

    @BeforeEach
    public void initTest() {
        mostOfContValues = createEntity(em);
    }

    @Test
    @Transactional
    void createMostOfContValues() throws Exception {
        int databaseSizeBeforeCreate = mostOfContValuesRepository.findAll().size();
        // Create the MostOfContValues
        MostOfContValuesDTO mostOfContValuesDTO = mostOfContValuesMapper.toDto(mostOfContValues);
        restMostOfContValuesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mostOfContValuesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MostOfContValues in the database
        List<MostOfContValues> mostOfContValuesList = mostOfContValuesRepository.findAll();
        assertThat(mostOfContValuesList).hasSize(databaseSizeBeforeCreate + 1);
        MostOfContValues testMostOfContValues = mostOfContValuesList.get(mostOfContValuesList.size() - 1);
        assertThat(testMostOfContValues.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the MostOfContValues in Elasticsearch
        verify(mockMostOfContValuesSearchRepository, times(1)).save(testMostOfContValues);
    }

    @Test
    @Transactional
    void createMostOfContValuesWithExistingId() throws Exception {
        // Create the MostOfContValues with an existing ID
        mostOfContValues.setId(1L);
        MostOfContValuesDTO mostOfContValuesDTO = mostOfContValuesMapper.toDto(mostOfContValues);

        int databaseSizeBeforeCreate = mostOfContValuesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMostOfContValuesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mostOfContValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContValues in the database
        List<MostOfContValues> mostOfContValuesList = mostOfContValuesRepository.findAll();
        assertThat(mostOfContValuesList).hasSize(databaseSizeBeforeCreate);

        // Validate the MostOfContValues in Elasticsearch
        verify(mockMostOfContValuesSearchRepository, times(0)).save(mostOfContValues);
    }

    @Test
    @Transactional
    void getAllMostOfContValues() throws Exception {
        // Initialize the database
        mostOfContValuesRepository.saveAndFlush(mostOfContValues);

        // Get all the mostOfContValuesList
        restMostOfContValuesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mostOfContValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getMostOfContValues() throws Exception {
        // Initialize the database
        mostOfContValuesRepository.saveAndFlush(mostOfContValues);

        // Get the mostOfContValues
        restMostOfContValuesMockMvc
            .perform(get(ENTITY_API_URL_ID, mostOfContValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mostOfContValues.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingMostOfContValues() throws Exception {
        // Get the mostOfContValues
        restMostOfContValuesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMostOfContValues() throws Exception {
        // Initialize the database
        mostOfContValuesRepository.saveAndFlush(mostOfContValues);

        int databaseSizeBeforeUpdate = mostOfContValuesRepository.findAll().size();

        // Update the mostOfContValues
        MostOfContValues updatedMostOfContValues = mostOfContValuesRepository.findById(mostOfContValues.getId()).get();
        // Disconnect from session so that the updates on updatedMostOfContValues are not directly saved in db
        em.detach(updatedMostOfContValues);
        updatedMostOfContValues.value(UPDATED_VALUE);
        MostOfContValuesDTO mostOfContValuesDTO = mostOfContValuesMapper.toDto(updatedMostOfContValues);

        restMostOfContValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mostOfContValuesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContValuesDTO))
            )
            .andExpect(status().isOk());

        // Validate the MostOfContValues in the database
        List<MostOfContValues> mostOfContValuesList = mostOfContValuesRepository.findAll();
        assertThat(mostOfContValuesList).hasSize(databaseSizeBeforeUpdate);
        MostOfContValues testMostOfContValues = mostOfContValuesList.get(mostOfContValuesList.size() - 1);
        assertThat(testMostOfContValues.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the MostOfContValues in Elasticsearch
        verify(mockMostOfContValuesSearchRepository).save(testMostOfContValues);
    }

    @Test
    @Transactional
    void putNonExistingMostOfContValues() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContValuesRepository.findAll().size();
        mostOfContValues.setId(count.incrementAndGet());

        // Create the MostOfContValues
        MostOfContValuesDTO mostOfContValuesDTO = mostOfContValuesMapper.toDto(mostOfContValues);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMostOfContValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mostOfContValuesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContValues in the database
        List<MostOfContValues> mostOfContValuesList = mostOfContValuesRepository.findAll();
        assertThat(mostOfContValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContValues in Elasticsearch
        verify(mockMostOfContValuesSearchRepository, times(0)).save(mostOfContValues);
    }

    @Test
    @Transactional
    void putWithIdMismatchMostOfContValues() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContValuesRepository.findAll().size();
        mostOfContValues.setId(count.incrementAndGet());

        // Create the MostOfContValues
        MostOfContValuesDTO mostOfContValuesDTO = mostOfContValuesMapper.toDto(mostOfContValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMostOfContValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContValues in the database
        List<MostOfContValues> mostOfContValuesList = mostOfContValuesRepository.findAll();
        assertThat(mostOfContValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContValues in Elasticsearch
        verify(mockMostOfContValuesSearchRepository, times(0)).save(mostOfContValues);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMostOfContValues() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContValuesRepository.findAll().size();
        mostOfContValues.setId(count.incrementAndGet());

        // Create the MostOfContValues
        MostOfContValuesDTO mostOfContValuesDTO = mostOfContValuesMapper.toDto(mostOfContValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMostOfContValuesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mostOfContValuesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MostOfContValues in the database
        List<MostOfContValues> mostOfContValuesList = mostOfContValuesRepository.findAll();
        assertThat(mostOfContValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContValues in Elasticsearch
        verify(mockMostOfContValuesSearchRepository, times(0)).save(mostOfContValues);
    }

    @Test
    @Transactional
    void partialUpdateMostOfContValuesWithPatch() throws Exception {
        // Initialize the database
        mostOfContValuesRepository.saveAndFlush(mostOfContValues);

        int databaseSizeBeforeUpdate = mostOfContValuesRepository.findAll().size();

        // Update the mostOfContValues using partial update
        MostOfContValues partialUpdatedMostOfContValues = new MostOfContValues();
        partialUpdatedMostOfContValues.setId(mostOfContValues.getId());

        partialUpdatedMostOfContValues.value(UPDATED_VALUE);

        restMostOfContValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMostOfContValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMostOfContValues))
            )
            .andExpect(status().isOk());

        // Validate the MostOfContValues in the database
        List<MostOfContValues> mostOfContValuesList = mostOfContValuesRepository.findAll();
        assertThat(mostOfContValuesList).hasSize(databaseSizeBeforeUpdate);
        MostOfContValues testMostOfContValues = mostOfContValuesList.get(mostOfContValuesList.size() - 1);
        assertThat(testMostOfContValues.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateMostOfContValuesWithPatch() throws Exception {
        // Initialize the database
        mostOfContValuesRepository.saveAndFlush(mostOfContValues);

        int databaseSizeBeforeUpdate = mostOfContValuesRepository.findAll().size();

        // Update the mostOfContValues using partial update
        MostOfContValues partialUpdatedMostOfContValues = new MostOfContValues();
        partialUpdatedMostOfContValues.setId(mostOfContValues.getId());

        partialUpdatedMostOfContValues.value(UPDATED_VALUE);

        restMostOfContValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMostOfContValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMostOfContValues))
            )
            .andExpect(status().isOk());

        // Validate the MostOfContValues in the database
        List<MostOfContValues> mostOfContValuesList = mostOfContValuesRepository.findAll();
        assertThat(mostOfContValuesList).hasSize(databaseSizeBeforeUpdate);
        MostOfContValues testMostOfContValues = mostOfContValuesList.get(mostOfContValuesList.size() - 1);
        assertThat(testMostOfContValues.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingMostOfContValues() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContValuesRepository.findAll().size();
        mostOfContValues.setId(count.incrementAndGet());

        // Create the MostOfContValues
        MostOfContValuesDTO mostOfContValuesDTO = mostOfContValuesMapper.toDto(mostOfContValues);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMostOfContValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mostOfContValuesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContValues in the database
        List<MostOfContValues> mostOfContValuesList = mostOfContValuesRepository.findAll();
        assertThat(mostOfContValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContValues in Elasticsearch
        verify(mockMostOfContValuesSearchRepository, times(0)).save(mostOfContValues);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMostOfContValues() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContValuesRepository.findAll().size();
        mostOfContValues.setId(count.incrementAndGet());

        // Create the MostOfContValues
        MostOfContValuesDTO mostOfContValuesDTO = mostOfContValuesMapper.toDto(mostOfContValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMostOfContValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContValues in the database
        List<MostOfContValues> mostOfContValuesList = mostOfContValuesRepository.findAll();
        assertThat(mostOfContValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContValues in Elasticsearch
        verify(mockMostOfContValuesSearchRepository, times(0)).save(mostOfContValues);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMostOfContValues() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContValuesRepository.findAll().size();
        mostOfContValues.setId(count.incrementAndGet());

        // Create the MostOfContValues
        MostOfContValuesDTO mostOfContValuesDTO = mostOfContValuesMapper.toDto(mostOfContValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMostOfContValuesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContValuesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MostOfContValues in the database
        List<MostOfContValues> mostOfContValuesList = mostOfContValuesRepository.findAll();
        assertThat(mostOfContValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContValues in Elasticsearch
        verify(mockMostOfContValuesSearchRepository, times(0)).save(mostOfContValues);
    }

    @Test
    @Transactional
    void deleteMostOfContValues() throws Exception {
        // Initialize the database
        mostOfContValuesRepository.saveAndFlush(mostOfContValues);

        int databaseSizeBeforeDelete = mostOfContValuesRepository.findAll().size();

        // Delete the mostOfContValues
        restMostOfContValuesMockMvc
            .perform(delete(ENTITY_API_URL_ID, mostOfContValues.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MostOfContValues> mostOfContValuesList = mostOfContValuesRepository.findAll();
        assertThat(mostOfContValuesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MostOfContValues in Elasticsearch
        verify(mockMostOfContValuesSearchRepository, times(1)).deleteById(mostOfContValues.getId());
    }

    @Test
    @Transactional
    void searchMostOfContValues() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        mostOfContValuesRepository.saveAndFlush(mostOfContValues);
        when(mockMostOfContValuesSearchRepository.search(queryStringQuery("id:" + mostOfContValues.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(mostOfContValues), PageRequest.of(0, 1), 1));

        // Search the mostOfContValues
        restMostOfContValuesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + mostOfContValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mostOfContValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
}
