package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.CommentFields;
import com.automation.professional.repository.CommentFieldsRepository;
import com.automation.professional.repository.search.CommentFieldsSearchRepository;
import com.automation.professional.service.dto.CommentFieldsDTO;
import com.automation.professional.service.mapper.CommentFieldsMapper;
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
 * Integration tests for the {@link CommentFieldsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommentFieldsResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/comment-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/comment-fields";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommentFieldsRepository commentFieldsRepository;

    @Autowired
    private CommentFieldsMapper commentFieldsMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.CommentFieldsSearchRepositoryMockConfiguration
     */
    @Autowired
    private CommentFieldsSearchRepository mockCommentFieldsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentFieldsMockMvc;

    private CommentFields commentFields;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentFields createEntity(EntityManager em) {
        CommentFields commentFields = new CommentFields().fieldName(DEFAULT_FIELD_NAME);
        return commentFields;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentFields createUpdatedEntity(EntityManager em) {
        CommentFields commentFields = new CommentFields().fieldName(UPDATED_FIELD_NAME);
        return commentFields;
    }

    @BeforeEach
    public void initTest() {
        commentFields = createEntity(em);
    }

    @Test
    @Transactional
    void createCommentFields() throws Exception {
        int databaseSizeBeforeCreate = commentFieldsRepository.findAll().size();
        // Create the CommentFields
        CommentFieldsDTO commentFieldsDTO = commentFieldsMapper.toDto(commentFields);
        restCommentFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentFieldsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommentFields in the database
        List<CommentFields> commentFieldsList = commentFieldsRepository.findAll();
        assertThat(commentFieldsList).hasSize(databaseSizeBeforeCreate + 1);
        CommentFields testCommentFields = commentFieldsList.get(commentFieldsList.size() - 1);
        assertThat(testCommentFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);

        // Validate the CommentFields in Elasticsearch
        verify(mockCommentFieldsSearchRepository, times(1)).save(testCommentFields);
    }

    @Test
    @Transactional
    void createCommentFieldsWithExistingId() throws Exception {
        // Create the CommentFields with an existing ID
        commentFields.setId(1L);
        CommentFieldsDTO commentFieldsDTO = commentFieldsMapper.toDto(commentFields);

        int databaseSizeBeforeCreate = commentFieldsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentFields in the database
        List<CommentFields> commentFieldsList = commentFieldsRepository.findAll();
        assertThat(commentFieldsList).hasSize(databaseSizeBeforeCreate);

        // Validate the CommentFields in Elasticsearch
        verify(mockCommentFieldsSearchRepository, times(0)).save(commentFields);
    }

    @Test
    @Transactional
    void getAllCommentFields() throws Exception {
        // Initialize the database
        commentFieldsRepository.saveAndFlush(commentFields);

        // Get all the commentFieldsList
        restCommentFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }

    @Test
    @Transactional
    void getCommentFields() throws Exception {
        // Initialize the database
        commentFieldsRepository.saveAndFlush(commentFields);

        // Get the commentFields
        restCommentFieldsMockMvc
            .perform(get(ENTITY_API_URL_ID, commentFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commentFields.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCommentFields() throws Exception {
        // Get the commentFields
        restCommentFieldsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommentFields() throws Exception {
        // Initialize the database
        commentFieldsRepository.saveAndFlush(commentFields);

        int databaseSizeBeforeUpdate = commentFieldsRepository.findAll().size();

        // Update the commentFields
        CommentFields updatedCommentFields = commentFieldsRepository.findById(commentFields.getId()).get();
        // Disconnect from session so that the updates on updatedCommentFields are not directly saved in db
        em.detach(updatedCommentFields);
        updatedCommentFields.fieldName(UPDATED_FIELD_NAME);
        CommentFieldsDTO commentFieldsDTO = commentFieldsMapper.toDto(updatedCommentFields);

        restCommentFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentFieldsDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommentFields in the database
        List<CommentFields> commentFieldsList = commentFieldsRepository.findAll();
        assertThat(commentFieldsList).hasSize(databaseSizeBeforeUpdate);
        CommentFields testCommentFields = commentFieldsList.get(commentFieldsList.size() - 1);
        assertThat(testCommentFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);

        // Validate the CommentFields in Elasticsearch
        verify(mockCommentFieldsSearchRepository).save(testCommentFields);
    }

    @Test
    @Transactional
    void putNonExistingCommentFields() throws Exception {
        int databaseSizeBeforeUpdate = commentFieldsRepository.findAll().size();
        commentFields.setId(count.incrementAndGet());

        // Create the CommentFields
        CommentFieldsDTO commentFieldsDTO = commentFieldsMapper.toDto(commentFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentFields in the database
        List<CommentFields> commentFieldsList = commentFieldsRepository.findAll();
        assertThat(commentFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentFields in Elasticsearch
        verify(mockCommentFieldsSearchRepository, times(0)).save(commentFields);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommentFields() throws Exception {
        int databaseSizeBeforeUpdate = commentFieldsRepository.findAll().size();
        commentFields.setId(count.incrementAndGet());

        // Create the CommentFields
        CommentFieldsDTO commentFieldsDTO = commentFieldsMapper.toDto(commentFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentFields in the database
        List<CommentFields> commentFieldsList = commentFieldsRepository.findAll();
        assertThat(commentFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentFields in Elasticsearch
        verify(mockCommentFieldsSearchRepository, times(0)).save(commentFields);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommentFields() throws Exception {
        int databaseSizeBeforeUpdate = commentFieldsRepository.findAll().size();
        commentFields.setId(count.incrementAndGet());

        // Create the CommentFields
        CommentFieldsDTO commentFieldsDTO = commentFieldsMapper.toDto(commentFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentFieldsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommentFields in the database
        List<CommentFields> commentFieldsList = commentFieldsRepository.findAll();
        assertThat(commentFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentFields in Elasticsearch
        verify(mockCommentFieldsSearchRepository, times(0)).save(commentFields);
    }

    @Test
    @Transactional
    void partialUpdateCommentFieldsWithPatch() throws Exception {
        // Initialize the database
        commentFieldsRepository.saveAndFlush(commentFields);

        int databaseSizeBeforeUpdate = commentFieldsRepository.findAll().size();

        // Update the commentFields using partial update
        CommentFields partialUpdatedCommentFields = new CommentFields();
        partialUpdatedCommentFields.setId(commentFields.getId());

        restCommentFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentFields))
            )
            .andExpect(status().isOk());

        // Validate the CommentFields in the database
        List<CommentFields> commentFieldsList = commentFieldsRepository.findAll();
        assertThat(commentFieldsList).hasSize(databaseSizeBeforeUpdate);
        CommentFields testCommentFields = commentFieldsList.get(commentFieldsList.size() - 1);
        assertThat(testCommentFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCommentFieldsWithPatch() throws Exception {
        // Initialize the database
        commentFieldsRepository.saveAndFlush(commentFields);

        int databaseSizeBeforeUpdate = commentFieldsRepository.findAll().size();

        // Update the commentFields using partial update
        CommentFields partialUpdatedCommentFields = new CommentFields();
        partialUpdatedCommentFields.setId(commentFields.getId());

        partialUpdatedCommentFields.fieldName(UPDATED_FIELD_NAME);

        restCommentFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentFields))
            )
            .andExpect(status().isOk());

        // Validate the CommentFields in the database
        List<CommentFields> commentFieldsList = commentFieldsRepository.findAll();
        assertThat(commentFieldsList).hasSize(databaseSizeBeforeUpdate);
        CommentFields testCommentFields = commentFieldsList.get(commentFieldsList.size() - 1);
        assertThat(testCommentFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCommentFields() throws Exception {
        int databaseSizeBeforeUpdate = commentFieldsRepository.findAll().size();
        commentFields.setId(count.incrementAndGet());

        // Create the CommentFields
        CommentFieldsDTO commentFieldsDTO = commentFieldsMapper.toDto(commentFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commentFieldsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentFields in the database
        List<CommentFields> commentFieldsList = commentFieldsRepository.findAll();
        assertThat(commentFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentFields in Elasticsearch
        verify(mockCommentFieldsSearchRepository, times(0)).save(commentFields);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommentFields() throws Exception {
        int databaseSizeBeforeUpdate = commentFieldsRepository.findAll().size();
        commentFields.setId(count.incrementAndGet());

        // Create the CommentFields
        CommentFieldsDTO commentFieldsDTO = commentFieldsMapper.toDto(commentFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentFields in the database
        List<CommentFields> commentFieldsList = commentFieldsRepository.findAll();
        assertThat(commentFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentFields in Elasticsearch
        verify(mockCommentFieldsSearchRepository, times(0)).save(commentFields);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommentFields() throws Exception {
        int databaseSizeBeforeUpdate = commentFieldsRepository.findAll().size();
        commentFields.setId(count.incrementAndGet());

        // Create the CommentFields
        CommentFieldsDTO commentFieldsDTO = commentFieldsMapper.toDto(commentFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommentFields in the database
        List<CommentFields> commentFieldsList = commentFieldsRepository.findAll();
        assertThat(commentFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentFields in Elasticsearch
        verify(mockCommentFieldsSearchRepository, times(0)).save(commentFields);
    }

    @Test
    @Transactional
    void deleteCommentFields() throws Exception {
        // Initialize the database
        commentFieldsRepository.saveAndFlush(commentFields);

        int databaseSizeBeforeDelete = commentFieldsRepository.findAll().size();

        // Delete the commentFields
        restCommentFieldsMockMvc
            .perform(delete(ENTITY_API_URL_ID, commentFields.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommentFields> commentFieldsList = commentFieldsRepository.findAll();
        assertThat(commentFieldsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CommentFields in Elasticsearch
        verify(mockCommentFieldsSearchRepository, times(1)).deleteById(commentFields.getId());
    }

    @Test
    @Transactional
    void searchCommentFields() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        commentFieldsRepository.saveAndFlush(commentFields);
        when(mockCommentFieldsSearchRepository.search(queryStringQuery("id:" + commentFields.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(commentFields), PageRequest.of(0, 1), 1));

        // Search the commentFields
        restCommentFieldsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + commentFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }
}
