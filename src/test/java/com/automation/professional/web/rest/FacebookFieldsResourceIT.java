package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.FacebookFields;
import com.automation.professional.repository.FacebookFieldsRepository;
import com.automation.professional.repository.search.FacebookFieldsSearchRepository;
import com.automation.professional.service.dto.FacebookFieldsDTO;
import com.automation.professional.service.mapper.FacebookFieldsMapper;
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
 * Integration tests for the {@link FacebookFieldsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FacebookFieldsResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facebook-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/facebook-fields";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacebookFieldsRepository facebookFieldsRepository;

    @Autowired
    private FacebookFieldsMapper facebookFieldsMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.FacebookFieldsSearchRepositoryMockConfiguration
     */
    @Autowired
    private FacebookFieldsSearchRepository mockFacebookFieldsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacebookFieldsMockMvc;

    private FacebookFields facebookFields;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacebookFields createEntity(EntityManager em) {
        FacebookFields facebookFields = new FacebookFields().fieldName(DEFAULT_FIELD_NAME);
        return facebookFields;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacebookFields createUpdatedEntity(EntityManager em) {
        FacebookFields facebookFields = new FacebookFields().fieldName(UPDATED_FIELD_NAME);
        return facebookFields;
    }

    @BeforeEach
    public void initTest() {
        facebookFields = createEntity(em);
    }

    @Test
    @Transactional
    void createFacebookFields() throws Exception {
        int databaseSizeBeforeCreate = facebookFieldsRepository.findAll().size();
        // Create the FacebookFields
        FacebookFieldsDTO facebookFieldsDTO = facebookFieldsMapper.toDto(facebookFields);
        restFacebookFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facebookFieldsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FacebookFields in the database
        List<FacebookFields> facebookFieldsList = facebookFieldsRepository.findAll();
        assertThat(facebookFieldsList).hasSize(databaseSizeBeforeCreate + 1);
        FacebookFields testFacebookFields = facebookFieldsList.get(facebookFieldsList.size() - 1);
        assertThat(testFacebookFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);

        // Validate the FacebookFields in Elasticsearch
        verify(mockFacebookFieldsSearchRepository, times(1)).save(testFacebookFields);
    }

    @Test
    @Transactional
    void createFacebookFieldsWithExistingId() throws Exception {
        // Create the FacebookFields with an existing ID
        facebookFields.setId(1L);
        FacebookFieldsDTO facebookFieldsDTO = facebookFieldsMapper.toDto(facebookFields);

        int databaseSizeBeforeCreate = facebookFieldsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacebookFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facebookFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacebookFields in the database
        List<FacebookFields> facebookFieldsList = facebookFieldsRepository.findAll();
        assertThat(facebookFieldsList).hasSize(databaseSizeBeforeCreate);

        // Validate the FacebookFields in Elasticsearch
        verify(mockFacebookFieldsSearchRepository, times(0)).save(facebookFields);
    }

    @Test
    @Transactional
    void getAllFacebookFields() throws Exception {
        // Initialize the database
        facebookFieldsRepository.saveAndFlush(facebookFields);

        // Get all the facebookFieldsList
        restFacebookFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facebookFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }

    @Test
    @Transactional
    void getFacebookFields() throws Exception {
        // Initialize the database
        facebookFieldsRepository.saveAndFlush(facebookFields);

        // Get the facebookFields
        restFacebookFieldsMockMvc
            .perform(get(ENTITY_API_URL_ID, facebookFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facebookFields.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }

    @Test
    @Transactional
    void getNonExistingFacebookFields() throws Exception {
        // Get the facebookFields
        restFacebookFieldsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFacebookFields() throws Exception {
        // Initialize the database
        facebookFieldsRepository.saveAndFlush(facebookFields);

        int databaseSizeBeforeUpdate = facebookFieldsRepository.findAll().size();

        // Update the facebookFields
        FacebookFields updatedFacebookFields = facebookFieldsRepository.findById(facebookFields.getId()).get();
        // Disconnect from session so that the updates on updatedFacebookFields are not directly saved in db
        em.detach(updatedFacebookFields);
        updatedFacebookFields.fieldName(UPDATED_FIELD_NAME);
        FacebookFieldsDTO facebookFieldsDTO = facebookFieldsMapper.toDto(updatedFacebookFields);

        restFacebookFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facebookFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facebookFieldsDTO))
            )
            .andExpect(status().isOk());

        // Validate the FacebookFields in the database
        List<FacebookFields> facebookFieldsList = facebookFieldsRepository.findAll();
        assertThat(facebookFieldsList).hasSize(databaseSizeBeforeUpdate);
        FacebookFields testFacebookFields = facebookFieldsList.get(facebookFieldsList.size() - 1);
        assertThat(testFacebookFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);

        // Validate the FacebookFields in Elasticsearch
        verify(mockFacebookFieldsSearchRepository).save(testFacebookFields);
    }

    @Test
    @Transactional
    void putNonExistingFacebookFields() throws Exception {
        int databaseSizeBeforeUpdate = facebookFieldsRepository.findAll().size();
        facebookFields.setId(count.incrementAndGet());

        // Create the FacebookFields
        FacebookFieldsDTO facebookFieldsDTO = facebookFieldsMapper.toDto(facebookFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacebookFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facebookFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facebookFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacebookFields in the database
        List<FacebookFields> facebookFieldsList = facebookFieldsRepository.findAll();
        assertThat(facebookFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FacebookFields in Elasticsearch
        verify(mockFacebookFieldsSearchRepository, times(0)).save(facebookFields);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacebookFields() throws Exception {
        int databaseSizeBeforeUpdate = facebookFieldsRepository.findAll().size();
        facebookFields.setId(count.incrementAndGet());

        // Create the FacebookFields
        FacebookFieldsDTO facebookFieldsDTO = facebookFieldsMapper.toDto(facebookFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacebookFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facebookFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacebookFields in the database
        List<FacebookFields> facebookFieldsList = facebookFieldsRepository.findAll();
        assertThat(facebookFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FacebookFields in Elasticsearch
        verify(mockFacebookFieldsSearchRepository, times(0)).save(facebookFields);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacebookFields() throws Exception {
        int databaseSizeBeforeUpdate = facebookFieldsRepository.findAll().size();
        facebookFields.setId(count.incrementAndGet());

        // Create the FacebookFields
        FacebookFieldsDTO facebookFieldsDTO = facebookFieldsMapper.toDto(facebookFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacebookFieldsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facebookFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacebookFields in the database
        List<FacebookFields> facebookFieldsList = facebookFieldsRepository.findAll();
        assertThat(facebookFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FacebookFields in Elasticsearch
        verify(mockFacebookFieldsSearchRepository, times(0)).save(facebookFields);
    }

    @Test
    @Transactional
    void partialUpdateFacebookFieldsWithPatch() throws Exception {
        // Initialize the database
        facebookFieldsRepository.saveAndFlush(facebookFields);

        int databaseSizeBeforeUpdate = facebookFieldsRepository.findAll().size();

        // Update the facebookFields using partial update
        FacebookFields partialUpdatedFacebookFields = new FacebookFields();
        partialUpdatedFacebookFields.setId(facebookFields.getId());

        partialUpdatedFacebookFields.fieldName(UPDATED_FIELD_NAME);

        restFacebookFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacebookFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacebookFields))
            )
            .andExpect(status().isOk());

        // Validate the FacebookFields in the database
        List<FacebookFields> facebookFieldsList = facebookFieldsRepository.findAll();
        assertThat(facebookFieldsList).hasSize(databaseSizeBeforeUpdate);
        FacebookFields testFacebookFields = facebookFieldsList.get(facebookFieldsList.size() - 1);
        assertThat(testFacebookFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void fullUpdateFacebookFieldsWithPatch() throws Exception {
        // Initialize the database
        facebookFieldsRepository.saveAndFlush(facebookFields);

        int databaseSizeBeforeUpdate = facebookFieldsRepository.findAll().size();

        // Update the facebookFields using partial update
        FacebookFields partialUpdatedFacebookFields = new FacebookFields();
        partialUpdatedFacebookFields.setId(facebookFields.getId());

        partialUpdatedFacebookFields.fieldName(UPDATED_FIELD_NAME);

        restFacebookFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacebookFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacebookFields))
            )
            .andExpect(status().isOk());

        // Validate the FacebookFields in the database
        List<FacebookFields> facebookFieldsList = facebookFieldsRepository.findAll();
        assertThat(facebookFieldsList).hasSize(databaseSizeBeforeUpdate);
        FacebookFields testFacebookFields = facebookFieldsList.get(facebookFieldsList.size() - 1);
        assertThat(testFacebookFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingFacebookFields() throws Exception {
        int databaseSizeBeforeUpdate = facebookFieldsRepository.findAll().size();
        facebookFields.setId(count.incrementAndGet());

        // Create the FacebookFields
        FacebookFieldsDTO facebookFieldsDTO = facebookFieldsMapper.toDto(facebookFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacebookFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facebookFieldsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facebookFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacebookFields in the database
        List<FacebookFields> facebookFieldsList = facebookFieldsRepository.findAll();
        assertThat(facebookFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FacebookFields in Elasticsearch
        verify(mockFacebookFieldsSearchRepository, times(0)).save(facebookFields);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacebookFields() throws Exception {
        int databaseSizeBeforeUpdate = facebookFieldsRepository.findAll().size();
        facebookFields.setId(count.incrementAndGet());

        // Create the FacebookFields
        FacebookFieldsDTO facebookFieldsDTO = facebookFieldsMapper.toDto(facebookFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacebookFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facebookFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacebookFields in the database
        List<FacebookFields> facebookFieldsList = facebookFieldsRepository.findAll();
        assertThat(facebookFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FacebookFields in Elasticsearch
        verify(mockFacebookFieldsSearchRepository, times(0)).save(facebookFields);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacebookFields() throws Exception {
        int databaseSizeBeforeUpdate = facebookFieldsRepository.findAll().size();
        facebookFields.setId(count.incrementAndGet());

        // Create the FacebookFields
        FacebookFieldsDTO facebookFieldsDTO = facebookFieldsMapper.toDto(facebookFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacebookFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facebookFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacebookFields in the database
        List<FacebookFields> facebookFieldsList = facebookFieldsRepository.findAll();
        assertThat(facebookFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FacebookFields in Elasticsearch
        verify(mockFacebookFieldsSearchRepository, times(0)).save(facebookFields);
    }

    @Test
    @Transactional
    void deleteFacebookFields() throws Exception {
        // Initialize the database
        facebookFieldsRepository.saveAndFlush(facebookFields);

        int databaseSizeBeforeDelete = facebookFieldsRepository.findAll().size();

        // Delete the facebookFields
        restFacebookFieldsMockMvc
            .perform(delete(ENTITY_API_URL_ID, facebookFields.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FacebookFields> facebookFieldsList = facebookFieldsRepository.findAll();
        assertThat(facebookFieldsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FacebookFields in Elasticsearch
        verify(mockFacebookFieldsSearchRepository, times(1)).deleteById(facebookFields.getId());
    }

    @Test
    @Transactional
    void searchFacebookFields() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        facebookFieldsRepository.saveAndFlush(facebookFields);
        when(mockFacebookFieldsSearchRepository.search(queryStringQuery("id:" + facebookFields.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(facebookFields), PageRequest.of(0, 1), 1));

        // Search the facebookFields
        restFacebookFieldsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + facebookFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facebookFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }
}
