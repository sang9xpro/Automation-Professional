package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.MostOfContFields;
import com.automation.professional.repository.MostOfContFieldsRepository;
import com.automation.professional.repository.search.MostOfContFieldsSearchRepository;
import com.automation.professional.service.dto.MostOfContFieldsDTO;
import com.automation.professional.service.mapper.MostOfContFieldsMapper;
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
 * Integration tests for the {@link MostOfContFieldsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MostOfContFieldsResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/most-of-cont-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/most-of-cont-fields";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MostOfContFieldsRepository mostOfContFieldsRepository;

    @Autowired
    private MostOfContFieldsMapper mostOfContFieldsMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.MostOfContFieldsSearchRepositoryMockConfiguration
     */
    @Autowired
    private MostOfContFieldsSearchRepository mockMostOfContFieldsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMostOfContFieldsMockMvc;

    private MostOfContFields mostOfContFields;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MostOfContFields createEntity(EntityManager em) {
        MostOfContFields mostOfContFields = new MostOfContFields().fieldName(DEFAULT_FIELD_NAME);
        return mostOfContFields;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MostOfContFields createUpdatedEntity(EntityManager em) {
        MostOfContFields mostOfContFields = new MostOfContFields().fieldName(UPDATED_FIELD_NAME);
        return mostOfContFields;
    }

    @BeforeEach
    public void initTest() {
        mostOfContFields = createEntity(em);
    }

    @Test
    @Transactional
    void createMostOfContFields() throws Exception {
        int databaseSizeBeforeCreate = mostOfContFieldsRepository.findAll().size();
        // Create the MostOfContFields
        MostOfContFieldsDTO mostOfContFieldsDTO = mostOfContFieldsMapper.toDto(mostOfContFields);
        restMostOfContFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mostOfContFieldsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MostOfContFields in the database
        List<MostOfContFields> mostOfContFieldsList = mostOfContFieldsRepository.findAll();
        assertThat(mostOfContFieldsList).hasSize(databaseSizeBeforeCreate + 1);
        MostOfContFields testMostOfContFields = mostOfContFieldsList.get(mostOfContFieldsList.size() - 1);
        assertThat(testMostOfContFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);

        // Validate the MostOfContFields in Elasticsearch
        verify(mockMostOfContFieldsSearchRepository, times(1)).save(testMostOfContFields);
    }

    @Test
    @Transactional
    void createMostOfContFieldsWithExistingId() throws Exception {
        // Create the MostOfContFields with an existing ID
        mostOfContFields.setId(1L);
        MostOfContFieldsDTO mostOfContFieldsDTO = mostOfContFieldsMapper.toDto(mostOfContFields);

        int databaseSizeBeforeCreate = mostOfContFieldsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMostOfContFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mostOfContFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContFields in the database
        List<MostOfContFields> mostOfContFieldsList = mostOfContFieldsRepository.findAll();
        assertThat(mostOfContFieldsList).hasSize(databaseSizeBeforeCreate);

        // Validate the MostOfContFields in Elasticsearch
        verify(mockMostOfContFieldsSearchRepository, times(0)).save(mostOfContFields);
    }

    @Test
    @Transactional
    void getAllMostOfContFields() throws Exception {
        // Initialize the database
        mostOfContFieldsRepository.saveAndFlush(mostOfContFields);

        // Get all the mostOfContFieldsList
        restMostOfContFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mostOfContFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }

    @Test
    @Transactional
    void getMostOfContFields() throws Exception {
        // Initialize the database
        mostOfContFieldsRepository.saveAndFlush(mostOfContFields);

        // Get the mostOfContFields
        restMostOfContFieldsMockMvc
            .perform(get(ENTITY_API_URL_ID, mostOfContFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mostOfContFields.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }

    @Test
    @Transactional
    void getNonExistingMostOfContFields() throws Exception {
        // Get the mostOfContFields
        restMostOfContFieldsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMostOfContFields() throws Exception {
        // Initialize the database
        mostOfContFieldsRepository.saveAndFlush(mostOfContFields);

        int databaseSizeBeforeUpdate = mostOfContFieldsRepository.findAll().size();

        // Update the mostOfContFields
        MostOfContFields updatedMostOfContFields = mostOfContFieldsRepository.findById(mostOfContFields.getId()).get();
        // Disconnect from session so that the updates on updatedMostOfContFields are not directly saved in db
        em.detach(updatedMostOfContFields);
        updatedMostOfContFields.fieldName(UPDATED_FIELD_NAME);
        MostOfContFieldsDTO mostOfContFieldsDTO = mostOfContFieldsMapper.toDto(updatedMostOfContFields);

        restMostOfContFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mostOfContFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContFieldsDTO))
            )
            .andExpect(status().isOk());

        // Validate the MostOfContFields in the database
        List<MostOfContFields> mostOfContFieldsList = mostOfContFieldsRepository.findAll();
        assertThat(mostOfContFieldsList).hasSize(databaseSizeBeforeUpdate);
        MostOfContFields testMostOfContFields = mostOfContFieldsList.get(mostOfContFieldsList.size() - 1);
        assertThat(testMostOfContFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);

        // Validate the MostOfContFields in Elasticsearch
        verify(mockMostOfContFieldsSearchRepository).save(testMostOfContFields);
    }

    @Test
    @Transactional
    void putNonExistingMostOfContFields() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContFieldsRepository.findAll().size();
        mostOfContFields.setId(count.incrementAndGet());

        // Create the MostOfContFields
        MostOfContFieldsDTO mostOfContFieldsDTO = mostOfContFieldsMapper.toDto(mostOfContFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMostOfContFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mostOfContFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContFields in the database
        List<MostOfContFields> mostOfContFieldsList = mostOfContFieldsRepository.findAll();
        assertThat(mostOfContFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContFields in Elasticsearch
        verify(mockMostOfContFieldsSearchRepository, times(0)).save(mostOfContFields);
    }

    @Test
    @Transactional
    void putWithIdMismatchMostOfContFields() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContFieldsRepository.findAll().size();
        mostOfContFields.setId(count.incrementAndGet());

        // Create the MostOfContFields
        MostOfContFieldsDTO mostOfContFieldsDTO = mostOfContFieldsMapper.toDto(mostOfContFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMostOfContFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContFields in the database
        List<MostOfContFields> mostOfContFieldsList = mostOfContFieldsRepository.findAll();
        assertThat(mostOfContFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContFields in Elasticsearch
        verify(mockMostOfContFieldsSearchRepository, times(0)).save(mostOfContFields);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMostOfContFields() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContFieldsRepository.findAll().size();
        mostOfContFields.setId(count.incrementAndGet());

        // Create the MostOfContFields
        MostOfContFieldsDTO mostOfContFieldsDTO = mostOfContFieldsMapper.toDto(mostOfContFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMostOfContFieldsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mostOfContFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MostOfContFields in the database
        List<MostOfContFields> mostOfContFieldsList = mostOfContFieldsRepository.findAll();
        assertThat(mostOfContFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContFields in Elasticsearch
        verify(mockMostOfContFieldsSearchRepository, times(0)).save(mostOfContFields);
    }

    @Test
    @Transactional
    void partialUpdateMostOfContFieldsWithPatch() throws Exception {
        // Initialize the database
        mostOfContFieldsRepository.saveAndFlush(mostOfContFields);

        int databaseSizeBeforeUpdate = mostOfContFieldsRepository.findAll().size();

        // Update the mostOfContFields using partial update
        MostOfContFields partialUpdatedMostOfContFields = new MostOfContFields();
        partialUpdatedMostOfContFields.setId(mostOfContFields.getId());

        restMostOfContFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMostOfContFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMostOfContFields))
            )
            .andExpect(status().isOk());

        // Validate the MostOfContFields in the database
        List<MostOfContFields> mostOfContFieldsList = mostOfContFieldsRepository.findAll();
        assertThat(mostOfContFieldsList).hasSize(databaseSizeBeforeUpdate);
        MostOfContFields testMostOfContFields = mostOfContFieldsList.get(mostOfContFieldsList.size() - 1);
        assertThat(testMostOfContFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
    }

    @Test
    @Transactional
    void fullUpdateMostOfContFieldsWithPatch() throws Exception {
        // Initialize the database
        mostOfContFieldsRepository.saveAndFlush(mostOfContFields);

        int databaseSizeBeforeUpdate = mostOfContFieldsRepository.findAll().size();

        // Update the mostOfContFields using partial update
        MostOfContFields partialUpdatedMostOfContFields = new MostOfContFields();
        partialUpdatedMostOfContFields.setId(mostOfContFields.getId());

        partialUpdatedMostOfContFields.fieldName(UPDATED_FIELD_NAME);

        restMostOfContFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMostOfContFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMostOfContFields))
            )
            .andExpect(status().isOk());

        // Validate the MostOfContFields in the database
        List<MostOfContFields> mostOfContFieldsList = mostOfContFieldsRepository.findAll();
        assertThat(mostOfContFieldsList).hasSize(databaseSizeBeforeUpdate);
        MostOfContFields testMostOfContFields = mostOfContFieldsList.get(mostOfContFieldsList.size() - 1);
        assertThat(testMostOfContFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingMostOfContFields() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContFieldsRepository.findAll().size();
        mostOfContFields.setId(count.incrementAndGet());

        // Create the MostOfContFields
        MostOfContFieldsDTO mostOfContFieldsDTO = mostOfContFieldsMapper.toDto(mostOfContFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMostOfContFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mostOfContFieldsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContFields in the database
        List<MostOfContFields> mostOfContFieldsList = mostOfContFieldsRepository.findAll();
        assertThat(mostOfContFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContFields in Elasticsearch
        verify(mockMostOfContFieldsSearchRepository, times(0)).save(mostOfContFields);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMostOfContFields() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContFieldsRepository.findAll().size();
        mostOfContFields.setId(count.incrementAndGet());

        // Create the MostOfContFields
        MostOfContFieldsDTO mostOfContFieldsDTO = mostOfContFieldsMapper.toDto(mostOfContFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMostOfContFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContFields in the database
        List<MostOfContFields> mostOfContFieldsList = mostOfContFieldsRepository.findAll();
        assertThat(mostOfContFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContFields in Elasticsearch
        verify(mockMostOfContFieldsSearchRepository, times(0)).save(mostOfContFields);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMostOfContFields() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContFieldsRepository.findAll().size();
        mostOfContFields.setId(count.incrementAndGet());

        // Create the MostOfContFields
        MostOfContFieldsDTO mostOfContFieldsDTO = mostOfContFieldsMapper.toDto(mostOfContFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMostOfContFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MostOfContFields in the database
        List<MostOfContFields> mostOfContFieldsList = mostOfContFieldsRepository.findAll();
        assertThat(mostOfContFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContFields in Elasticsearch
        verify(mockMostOfContFieldsSearchRepository, times(0)).save(mostOfContFields);
    }

    @Test
    @Transactional
    void deleteMostOfContFields() throws Exception {
        // Initialize the database
        mostOfContFieldsRepository.saveAndFlush(mostOfContFields);

        int databaseSizeBeforeDelete = mostOfContFieldsRepository.findAll().size();

        // Delete the mostOfContFields
        restMostOfContFieldsMockMvc
            .perform(delete(ENTITY_API_URL_ID, mostOfContFields.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MostOfContFields> mostOfContFieldsList = mostOfContFieldsRepository.findAll();
        assertThat(mostOfContFieldsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MostOfContFields in Elasticsearch
        verify(mockMostOfContFieldsSearchRepository, times(1)).deleteById(mostOfContFields.getId());
    }

    @Test
    @Transactional
    void searchMostOfContFields() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        mostOfContFieldsRepository.saveAndFlush(mostOfContFields);
        when(mockMostOfContFieldsSearchRepository.search(queryStringQuery("id:" + mostOfContFields.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(mostOfContFields), PageRequest.of(0, 1), 1));

        // Search the mostOfContFields
        restMostOfContFieldsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + mostOfContFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mostOfContFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }
}
