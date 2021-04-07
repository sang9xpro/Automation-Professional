package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.MostOfContent;
import com.automation.professional.repository.MostOfContentRepository;
import com.automation.professional.repository.search.MostOfContentSearchRepository;
import com.automation.professional.service.dto.MostOfContentDTO;
import com.automation.professional.service.mapper.MostOfContentMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link MostOfContentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MostOfContentResourceIT {

    private static final String DEFAULT_URL_ORIGINAL = "AAAAAAAAAA";
    private static final String UPDATED_URL_ORIGINAL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TEXT = "BBBBBBBBBB";

    private static final Instant DEFAULT_POST_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_POST_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_NUMBER_LIKE = 1;
    private static final Integer UPDATED_NUMBER_LIKE = 2;

    private static final Integer DEFAULT_NUMBER_COMMENT = 1;
    private static final Integer UPDATED_NUMBER_COMMENT = 2;

    private static final String ENTITY_API_URL = "/api/most-of-contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/most-of-contents";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MostOfContentRepository mostOfContentRepository;

    @Autowired
    private MostOfContentMapper mostOfContentMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.MostOfContentSearchRepositoryMockConfiguration
     */
    @Autowired
    private MostOfContentSearchRepository mockMostOfContentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMostOfContentMockMvc;

    private MostOfContent mostOfContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MostOfContent createEntity(EntityManager em) {
        MostOfContent mostOfContent = new MostOfContent()
            .urlOriginal(DEFAULT_URL_ORIGINAL)
            .contentText(DEFAULT_CONTENT_TEXT)
            .postTime(DEFAULT_POST_TIME)
            .numberLike(DEFAULT_NUMBER_LIKE)
            .numberComment(DEFAULT_NUMBER_COMMENT);
        return mostOfContent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MostOfContent createUpdatedEntity(EntityManager em) {
        MostOfContent mostOfContent = new MostOfContent()
            .urlOriginal(UPDATED_URL_ORIGINAL)
            .contentText(UPDATED_CONTENT_TEXT)
            .postTime(UPDATED_POST_TIME)
            .numberLike(UPDATED_NUMBER_LIKE)
            .numberComment(UPDATED_NUMBER_COMMENT);
        return mostOfContent;
    }

    @BeforeEach
    public void initTest() {
        mostOfContent = createEntity(em);
    }

    @Test
    @Transactional
    void createMostOfContent() throws Exception {
        int databaseSizeBeforeCreate = mostOfContentRepository.findAll().size();
        // Create the MostOfContent
        MostOfContentDTO mostOfContentDTO = mostOfContentMapper.toDto(mostOfContent);
        restMostOfContentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mostOfContentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MostOfContent in the database
        List<MostOfContent> mostOfContentList = mostOfContentRepository.findAll();
        assertThat(mostOfContentList).hasSize(databaseSizeBeforeCreate + 1);
        MostOfContent testMostOfContent = mostOfContentList.get(mostOfContentList.size() - 1);
        assertThat(testMostOfContent.getUrlOriginal()).isEqualTo(DEFAULT_URL_ORIGINAL);
        assertThat(testMostOfContent.getContentText()).isEqualTo(DEFAULT_CONTENT_TEXT);
        assertThat(testMostOfContent.getPostTime()).isEqualTo(DEFAULT_POST_TIME);
        assertThat(testMostOfContent.getNumberLike()).isEqualTo(DEFAULT_NUMBER_LIKE);
        assertThat(testMostOfContent.getNumberComment()).isEqualTo(DEFAULT_NUMBER_COMMENT);

        // Validate the MostOfContent in Elasticsearch
        verify(mockMostOfContentSearchRepository, times(1)).save(testMostOfContent);
    }

    @Test
    @Transactional
    void createMostOfContentWithExistingId() throws Exception {
        // Create the MostOfContent with an existing ID
        mostOfContent.setId(1L);
        MostOfContentDTO mostOfContentDTO = mostOfContentMapper.toDto(mostOfContent);

        int databaseSizeBeforeCreate = mostOfContentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMostOfContentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mostOfContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContent in the database
        List<MostOfContent> mostOfContentList = mostOfContentRepository.findAll();
        assertThat(mostOfContentList).hasSize(databaseSizeBeforeCreate);

        // Validate the MostOfContent in Elasticsearch
        verify(mockMostOfContentSearchRepository, times(0)).save(mostOfContent);
    }

    @Test
    @Transactional
    void getAllMostOfContents() throws Exception {
        // Initialize the database
        mostOfContentRepository.saveAndFlush(mostOfContent);

        // Get all the mostOfContentList
        restMostOfContentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mostOfContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].urlOriginal").value(hasItem(DEFAULT_URL_ORIGINAL)))
            .andExpect(jsonPath("$.[*].contentText").value(hasItem(DEFAULT_CONTENT_TEXT)))
            .andExpect(jsonPath("$.[*].postTime").value(hasItem(DEFAULT_POST_TIME.toString())))
            .andExpect(jsonPath("$.[*].numberLike").value(hasItem(DEFAULT_NUMBER_LIKE)))
            .andExpect(jsonPath("$.[*].numberComment").value(hasItem(DEFAULT_NUMBER_COMMENT)));
    }

    @Test
    @Transactional
    void getMostOfContent() throws Exception {
        // Initialize the database
        mostOfContentRepository.saveAndFlush(mostOfContent);

        // Get the mostOfContent
        restMostOfContentMockMvc
            .perform(get(ENTITY_API_URL_ID, mostOfContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mostOfContent.getId().intValue()))
            .andExpect(jsonPath("$.urlOriginal").value(DEFAULT_URL_ORIGINAL))
            .andExpect(jsonPath("$.contentText").value(DEFAULT_CONTENT_TEXT))
            .andExpect(jsonPath("$.postTime").value(DEFAULT_POST_TIME.toString()))
            .andExpect(jsonPath("$.numberLike").value(DEFAULT_NUMBER_LIKE))
            .andExpect(jsonPath("$.numberComment").value(DEFAULT_NUMBER_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingMostOfContent() throws Exception {
        // Get the mostOfContent
        restMostOfContentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMostOfContent() throws Exception {
        // Initialize the database
        mostOfContentRepository.saveAndFlush(mostOfContent);

        int databaseSizeBeforeUpdate = mostOfContentRepository.findAll().size();

        // Update the mostOfContent
        MostOfContent updatedMostOfContent = mostOfContentRepository.findById(mostOfContent.getId()).get();
        // Disconnect from session so that the updates on updatedMostOfContent are not directly saved in db
        em.detach(updatedMostOfContent);
        updatedMostOfContent
            .urlOriginal(UPDATED_URL_ORIGINAL)
            .contentText(UPDATED_CONTENT_TEXT)
            .postTime(UPDATED_POST_TIME)
            .numberLike(UPDATED_NUMBER_LIKE)
            .numberComment(UPDATED_NUMBER_COMMENT);
        MostOfContentDTO mostOfContentDTO = mostOfContentMapper.toDto(updatedMostOfContent);

        restMostOfContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mostOfContentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContentDTO))
            )
            .andExpect(status().isOk());

        // Validate the MostOfContent in the database
        List<MostOfContent> mostOfContentList = mostOfContentRepository.findAll();
        assertThat(mostOfContentList).hasSize(databaseSizeBeforeUpdate);
        MostOfContent testMostOfContent = mostOfContentList.get(mostOfContentList.size() - 1);
        assertThat(testMostOfContent.getUrlOriginal()).isEqualTo(UPDATED_URL_ORIGINAL);
        assertThat(testMostOfContent.getContentText()).isEqualTo(UPDATED_CONTENT_TEXT);
        assertThat(testMostOfContent.getPostTime()).isEqualTo(UPDATED_POST_TIME);
        assertThat(testMostOfContent.getNumberLike()).isEqualTo(UPDATED_NUMBER_LIKE);
        assertThat(testMostOfContent.getNumberComment()).isEqualTo(UPDATED_NUMBER_COMMENT);

        // Validate the MostOfContent in Elasticsearch
        verify(mockMostOfContentSearchRepository).save(testMostOfContent);
    }

    @Test
    @Transactional
    void putNonExistingMostOfContent() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContentRepository.findAll().size();
        mostOfContent.setId(count.incrementAndGet());

        // Create the MostOfContent
        MostOfContentDTO mostOfContentDTO = mostOfContentMapper.toDto(mostOfContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMostOfContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mostOfContentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContent in the database
        List<MostOfContent> mostOfContentList = mostOfContentRepository.findAll();
        assertThat(mostOfContentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContent in Elasticsearch
        verify(mockMostOfContentSearchRepository, times(0)).save(mostOfContent);
    }

    @Test
    @Transactional
    void putWithIdMismatchMostOfContent() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContentRepository.findAll().size();
        mostOfContent.setId(count.incrementAndGet());

        // Create the MostOfContent
        MostOfContentDTO mostOfContentDTO = mostOfContentMapper.toDto(mostOfContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMostOfContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContent in the database
        List<MostOfContent> mostOfContentList = mostOfContentRepository.findAll();
        assertThat(mostOfContentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContent in Elasticsearch
        verify(mockMostOfContentSearchRepository, times(0)).save(mostOfContent);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMostOfContent() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContentRepository.findAll().size();
        mostOfContent.setId(count.incrementAndGet());

        // Create the MostOfContent
        MostOfContentDTO mostOfContentDTO = mostOfContentMapper.toDto(mostOfContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMostOfContentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mostOfContentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MostOfContent in the database
        List<MostOfContent> mostOfContentList = mostOfContentRepository.findAll();
        assertThat(mostOfContentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContent in Elasticsearch
        verify(mockMostOfContentSearchRepository, times(0)).save(mostOfContent);
    }

    @Test
    @Transactional
    void partialUpdateMostOfContentWithPatch() throws Exception {
        // Initialize the database
        mostOfContentRepository.saveAndFlush(mostOfContent);

        int databaseSizeBeforeUpdate = mostOfContentRepository.findAll().size();

        // Update the mostOfContent using partial update
        MostOfContent partialUpdatedMostOfContent = new MostOfContent();
        partialUpdatedMostOfContent.setId(mostOfContent.getId());

        partialUpdatedMostOfContent.urlOriginal(UPDATED_URL_ORIGINAL).numberLike(UPDATED_NUMBER_LIKE);

        restMostOfContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMostOfContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMostOfContent))
            )
            .andExpect(status().isOk());

        // Validate the MostOfContent in the database
        List<MostOfContent> mostOfContentList = mostOfContentRepository.findAll();
        assertThat(mostOfContentList).hasSize(databaseSizeBeforeUpdate);
        MostOfContent testMostOfContent = mostOfContentList.get(mostOfContentList.size() - 1);
        assertThat(testMostOfContent.getUrlOriginal()).isEqualTo(UPDATED_URL_ORIGINAL);
        assertThat(testMostOfContent.getContentText()).isEqualTo(DEFAULT_CONTENT_TEXT);
        assertThat(testMostOfContent.getPostTime()).isEqualTo(DEFAULT_POST_TIME);
        assertThat(testMostOfContent.getNumberLike()).isEqualTo(UPDATED_NUMBER_LIKE);
        assertThat(testMostOfContent.getNumberComment()).isEqualTo(DEFAULT_NUMBER_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateMostOfContentWithPatch() throws Exception {
        // Initialize the database
        mostOfContentRepository.saveAndFlush(mostOfContent);

        int databaseSizeBeforeUpdate = mostOfContentRepository.findAll().size();

        // Update the mostOfContent using partial update
        MostOfContent partialUpdatedMostOfContent = new MostOfContent();
        partialUpdatedMostOfContent.setId(mostOfContent.getId());

        partialUpdatedMostOfContent
            .urlOriginal(UPDATED_URL_ORIGINAL)
            .contentText(UPDATED_CONTENT_TEXT)
            .postTime(UPDATED_POST_TIME)
            .numberLike(UPDATED_NUMBER_LIKE)
            .numberComment(UPDATED_NUMBER_COMMENT);

        restMostOfContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMostOfContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMostOfContent))
            )
            .andExpect(status().isOk());

        // Validate the MostOfContent in the database
        List<MostOfContent> mostOfContentList = mostOfContentRepository.findAll();
        assertThat(mostOfContentList).hasSize(databaseSizeBeforeUpdate);
        MostOfContent testMostOfContent = mostOfContentList.get(mostOfContentList.size() - 1);
        assertThat(testMostOfContent.getUrlOriginal()).isEqualTo(UPDATED_URL_ORIGINAL);
        assertThat(testMostOfContent.getContentText()).isEqualTo(UPDATED_CONTENT_TEXT);
        assertThat(testMostOfContent.getPostTime()).isEqualTo(UPDATED_POST_TIME);
        assertThat(testMostOfContent.getNumberLike()).isEqualTo(UPDATED_NUMBER_LIKE);
        assertThat(testMostOfContent.getNumberComment()).isEqualTo(UPDATED_NUMBER_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingMostOfContent() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContentRepository.findAll().size();
        mostOfContent.setId(count.incrementAndGet());

        // Create the MostOfContent
        MostOfContentDTO mostOfContentDTO = mostOfContentMapper.toDto(mostOfContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMostOfContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mostOfContentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContent in the database
        List<MostOfContent> mostOfContentList = mostOfContentRepository.findAll();
        assertThat(mostOfContentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContent in Elasticsearch
        verify(mockMostOfContentSearchRepository, times(0)).save(mostOfContent);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMostOfContent() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContentRepository.findAll().size();
        mostOfContent.setId(count.incrementAndGet());

        // Create the MostOfContent
        MostOfContentDTO mostOfContentDTO = mostOfContentMapper.toDto(mostOfContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMostOfContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MostOfContent in the database
        List<MostOfContent> mostOfContentList = mostOfContentRepository.findAll();
        assertThat(mostOfContentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContent in Elasticsearch
        verify(mockMostOfContentSearchRepository, times(0)).save(mostOfContent);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMostOfContent() throws Exception {
        int databaseSizeBeforeUpdate = mostOfContentRepository.findAll().size();
        mostOfContent.setId(count.incrementAndGet());

        // Create the MostOfContent
        MostOfContentDTO mostOfContentDTO = mostOfContentMapper.toDto(mostOfContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMostOfContentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mostOfContentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MostOfContent in the database
        List<MostOfContent> mostOfContentList = mostOfContentRepository.findAll();
        assertThat(mostOfContentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MostOfContent in Elasticsearch
        verify(mockMostOfContentSearchRepository, times(0)).save(mostOfContent);
    }

    @Test
    @Transactional
    void deleteMostOfContent() throws Exception {
        // Initialize the database
        mostOfContentRepository.saveAndFlush(mostOfContent);

        int databaseSizeBeforeDelete = mostOfContentRepository.findAll().size();

        // Delete the mostOfContent
        restMostOfContentMockMvc
            .perform(delete(ENTITY_API_URL_ID, mostOfContent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MostOfContent> mostOfContentList = mostOfContentRepository.findAll();
        assertThat(mostOfContentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MostOfContent in Elasticsearch
        verify(mockMostOfContentSearchRepository, times(1)).deleteById(mostOfContent.getId());
    }

    @Test
    @Transactional
    void searchMostOfContent() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        mostOfContentRepository.saveAndFlush(mostOfContent);
        when(mockMostOfContentSearchRepository.search(queryStringQuery("id:" + mostOfContent.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(mostOfContent), PageRequest.of(0, 1), 1));

        // Search the mostOfContent
        restMostOfContentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + mostOfContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mostOfContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].urlOriginal").value(hasItem(DEFAULT_URL_ORIGINAL)))
            .andExpect(jsonPath("$.[*].contentText").value(hasItem(DEFAULT_CONTENT_TEXT)))
            .andExpect(jsonPath("$.[*].postTime").value(hasItem(DEFAULT_POST_TIME.toString())))
            .andExpect(jsonPath("$.[*].numberLike").value(hasItem(DEFAULT_NUMBER_LIKE)))
            .andExpect(jsonPath("$.[*].numberComment").value(hasItem(DEFAULT_NUMBER_COMMENT)));
    }
}
