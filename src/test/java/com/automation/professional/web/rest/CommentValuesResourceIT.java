package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.CommentValues;
import com.automation.professional.repository.CommentValuesRepository;
import com.automation.professional.repository.search.CommentValuesSearchRepository;
import com.automation.professional.service.dto.CommentValuesDTO;
import com.automation.professional.service.mapper.CommentValuesMapper;
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
 * Integration tests for the {@link CommentValuesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommentValuesResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/comment-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/comment-values";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommentValuesRepository commentValuesRepository;

    @Autowired
    private CommentValuesMapper commentValuesMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.CommentValuesSearchRepositoryMockConfiguration
     */
    @Autowired
    private CommentValuesSearchRepository mockCommentValuesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentValuesMockMvc;

    private CommentValues commentValues;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentValues createEntity(EntityManager em) {
        CommentValues commentValues = new CommentValues().value(DEFAULT_VALUE);
        return commentValues;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentValues createUpdatedEntity(EntityManager em) {
        CommentValues commentValues = new CommentValues().value(UPDATED_VALUE);
        return commentValues;
    }

    @BeforeEach
    public void initTest() {
        commentValues = createEntity(em);
    }

    @Test
    @Transactional
    void createCommentValues() throws Exception {
        int databaseSizeBeforeCreate = commentValuesRepository.findAll().size();
        // Create the CommentValues
        CommentValuesDTO commentValuesDTO = commentValuesMapper.toDto(commentValues);
        restCommentValuesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentValuesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommentValues in the database
        List<CommentValues> commentValuesList = commentValuesRepository.findAll();
        assertThat(commentValuesList).hasSize(databaseSizeBeforeCreate + 1);
        CommentValues testCommentValues = commentValuesList.get(commentValuesList.size() - 1);
        assertThat(testCommentValues.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the CommentValues in Elasticsearch
        verify(mockCommentValuesSearchRepository, times(1)).save(testCommentValues);
    }

    @Test
    @Transactional
    void createCommentValuesWithExistingId() throws Exception {
        // Create the CommentValues with an existing ID
        commentValues.setId(1L);
        CommentValuesDTO commentValuesDTO = commentValuesMapper.toDto(commentValues);

        int databaseSizeBeforeCreate = commentValuesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentValuesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentValues in the database
        List<CommentValues> commentValuesList = commentValuesRepository.findAll();
        assertThat(commentValuesList).hasSize(databaseSizeBeforeCreate);

        // Validate the CommentValues in Elasticsearch
        verify(mockCommentValuesSearchRepository, times(0)).save(commentValues);
    }

    @Test
    @Transactional
    void getAllCommentValues() throws Exception {
        // Initialize the database
        commentValuesRepository.saveAndFlush(commentValues);

        // Get all the commentValuesList
        restCommentValuesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getCommentValues() throws Exception {
        // Initialize the database
        commentValuesRepository.saveAndFlush(commentValues);

        // Get the commentValues
        restCommentValuesMockMvc
            .perform(get(ENTITY_API_URL_ID, commentValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commentValues.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingCommentValues() throws Exception {
        // Get the commentValues
        restCommentValuesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommentValues() throws Exception {
        // Initialize the database
        commentValuesRepository.saveAndFlush(commentValues);

        int databaseSizeBeforeUpdate = commentValuesRepository.findAll().size();

        // Update the commentValues
        CommentValues updatedCommentValues = commentValuesRepository.findById(commentValues.getId()).get();
        // Disconnect from session so that the updates on updatedCommentValues are not directly saved in db
        em.detach(updatedCommentValues);
        updatedCommentValues.value(UPDATED_VALUE);
        CommentValuesDTO commentValuesDTO = commentValuesMapper.toDto(updatedCommentValues);

        restCommentValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentValuesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentValuesDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommentValues in the database
        List<CommentValues> commentValuesList = commentValuesRepository.findAll();
        assertThat(commentValuesList).hasSize(databaseSizeBeforeUpdate);
        CommentValues testCommentValues = commentValuesList.get(commentValuesList.size() - 1);
        assertThat(testCommentValues.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the CommentValues in Elasticsearch
        verify(mockCommentValuesSearchRepository).save(testCommentValues);
    }

    @Test
    @Transactional
    void putNonExistingCommentValues() throws Exception {
        int databaseSizeBeforeUpdate = commentValuesRepository.findAll().size();
        commentValues.setId(count.incrementAndGet());

        // Create the CommentValues
        CommentValuesDTO commentValuesDTO = commentValuesMapper.toDto(commentValues);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentValuesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentValues in the database
        List<CommentValues> commentValuesList = commentValuesRepository.findAll();
        assertThat(commentValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentValues in Elasticsearch
        verify(mockCommentValuesSearchRepository, times(0)).save(commentValues);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommentValues() throws Exception {
        int databaseSizeBeforeUpdate = commentValuesRepository.findAll().size();
        commentValues.setId(count.incrementAndGet());

        // Create the CommentValues
        CommentValuesDTO commentValuesDTO = commentValuesMapper.toDto(commentValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentValues in the database
        List<CommentValues> commentValuesList = commentValuesRepository.findAll();
        assertThat(commentValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentValues in Elasticsearch
        verify(mockCommentValuesSearchRepository, times(0)).save(commentValues);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommentValues() throws Exception {
        int databaseSizeBeforeUpdate = commentValuesRepository.findAll().size();
        commentValues.setId(count.incrementAndGet());

        // Create the CommentValues
        CommentValuesDTO commentValuesDTO = commentValuesMapper.toDto(commentValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentValuesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentValuesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommentValues in the database
        List<CommentValues> commentValuesList = commentValuesRepository.findAll();
        assertThat(commentValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentValues in Elasticsearch
        verify(mockCommentValuesSearchRepository, times(0)).save(commentValues);
    }

    @Test
    @Transactional
    void partialUpdateCommentValuesWithPatch() throws Exception {
        // Initialize the database
        commentValuesRepository.saveAndFlush(commentValues);

        int databaseSizeBeforeUpdate = commentValuesRepository.findAll().size();

        // Update the commentValues using partial update
        CommentValues partialUpdatedCommentValues = new CommentValues();
        partialUpdatedCommentValues.setId(commentValues.getId());

        partialUpdatedCommentValues.value(UPDATED_VALUE);

        restCommentValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentValues))
            )
            .andExpect(status().isOk());

        // Validate the CommentValues in the database
        List<CommentValues> commentValuesList = commentValuesRepository.findAll();
        assertThat(commentValuesList).hasSize(databaseSizeBeforeUpdate);
        CommentValues testCommentValues = commentValuesList.get(commentValuesList.size() - 1);
        assertThat(testCommentValues.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateCommentValuesWithPatch() throws Exception {
        // Initialize the database
        commentValuesRepository.saveAndFlush(commentValues);

        int databaseSizeBeforeUpdate = commentValuesRepository.findAll().size();

        // Update the commentValues using partial update
        CommentValues partialUpdatedCommentValues = new CommentValues();
        partialUpdatedCommentValues.setId(commentValues.getId());

        partialUpdatedCommentValues.value(UPDATED_VALUE);

        restCommentValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentValues))
            )
            .andExpect(status().isOk());

        // Validate the CommentValues in the database
        List<CommentValues> commentValuesList = commentValuesRepository.findAll();
        assertThat(commentValuesList).hasSize(databaseSizeBeforeUpdate);
        CommentValues testCommentValues = commentValuesList.get(commentValuesList.size() - 1);
        assertThat(testCommentValues.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingCommentValues() throws Exception {
        int databaseSizeBeforeUpdate = commentValuesRepository.findAll().size();
        commentValues.setId(count.incrementAndGet());

        // Create the CommentValues
        CommentValuesDTO commentValuesDTO = commentValuesMapper.toDto(commentValues);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commentValuesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentValues in the database
        List<CommentValues> commentValuesList = commentValuesRepository.findAll();
        assertThat(commentValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentValues in Elasticsearch
        verify(mockCommentValuesSearchRepository, times(0)).save(commentValues);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommentValues() throws Exception {
        int databaseSizeBeforeUpdate = commentValuesRepository.findAll().size();
        commentValues.setId(count.incrementAndGet());

        // Create the CommentValues
        CommentValuesDTO commentValuesDTO = commentValuesMapper.toDto(commentValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentValues in the database
        List<CommentValues> commentValuesList = commentValuesRepository.findAll();
        assertThat(commentValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentValues in Elasticsearch
        verify(mockCommentValuesSearchRepository, times(0)).save(commentValues);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommentValues() throws Exception {
        int databaseSizeBeforeUpdate = commentValuesRepository.findAll().size();
        commentValues.setId(count.incrementAndGet());

        // Create the CommentValues
        CommentValuesDTO commentValuesDTO = commentValuesMapper.toDto(commentValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentValuesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentValuesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommentValues in the database
        List<CommentValues> commentValuesList = commentValuesRepository.findAll();
        assertThat(commentValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentValues in Elasticsearch
        verify(mockCommentValuesSearchRepository, times(0)).save(commentValues);
    }

    @Test
    @Transactional
    void deleteCommentValues() throws Exception {
        // Initialize the database
        commentValuesRepository.saveAndFlush(commentValues);

        int databaseSizeBeforeDelete = commentValuesRepository.findAll().size();

        // Delete the commentValues
        restCommentValuesMockMvc
            .perform(delete(ENTITY_API_URL_ID, commentValues.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommentValues> commentValuesList = commentValuesRepository.findAll();
        assertThat(commentValuesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CommentValues in Elasticsearch
        verify(mockCommentValuesSearchRepository, times(1)).deleteById(commentValues.getId());
    }

    @Test
    @Transactional
    void searchCommentValues() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        commentValuesRepository.saveAndFlush(commentValues);
        when(mockCommentValuesSearchRepository.search(queryStringQuery("id:" + commentValues.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(commentValues), PageRequest.of(0, 1), 1));

        // Search the commentValues
        restCommentValuesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + commentValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
}
