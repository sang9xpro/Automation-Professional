package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.TaskValues;
import com.automation.professional.repository.TaskValuesRepository;
import com.automation.professional.repository.search.TaskValuesSearchRepository;
import com.automation.professional.service.dto.TaskValuesDTO;
import com.automation.professional.service.mapper.TaskValuesMapper;
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
 * Integration tests for the {@link TaskValuesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TaskValuesResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/task-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/task-values";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskValuesRepository taskValuesRepository;

    @Autowired
    private TaskValuesMapper taskValuesMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.TaskValuesSearchRepositoryMockConfiguration
     */
    @Autowired
    private TaskValuesSearchRepository mockTaskValuesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskValuesMockMvc;

    private TaskValues taskValues;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskValues createEntity(EntityManager em) {
        TaskValues taskValues = new TaskValues().value(DEFAULT_VALUE);
        return taskValues;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskValues createUpdatedEntity(EntityManager em) {
        TaskValues taskValues = new TaskValues().value(UPDATED_VALUE);
        return taskValues;
    }

    @BeforeEach
    public void initTest() {
        taskValues = createEntity(em);
    }

    @Test
    @Transactional
    void createTaskValues() throws Exception {
        int databaseSizeBeforeCreate = taskValuesRepository.findAll().size();
        // Create the TaskValues
        TaskValuesDTO taskValuesDTO = taskValuesMapper.toDto(taskValues);
        restTaskValuesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskValuesDTO)))
            .andExpect(status().isCreated());

        // Validate the TaskValues in the database
        List<TaskValues> taskValuesList = taskValuesRepository.findAll();
        assertThat(taskValuesList).hasSize(databaseSizeBeforeCreate + 1);
        TaskValues testTaskValues = taskValuesList.get(taskValuesList.size() - 1);
        assertThat(testTaskValues.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the TaskValues in Elasticsearch
        verify(mockTaskValuesSearchRepository, times(1)).save(testTaskValues);
    }

    @Test
    @Transactional
    void createTaskValuesWithExistingId() throws Exception {
        // Create the TaskValues with an existing ID
        taskValues.setId(1L);
        TaskValuesDTO taskValuesDTO = taskValuesMapper.toDto(taskValues);

        int databaseSizeBeforeCreate = taskValuesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskValuesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskValuesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaskValues in the database
        List<TaskValues> taskValuesList = taskValuesRepository.findAll();
        assertThat(taskValuesList).hasSize(databaseSizeBeforeCreate);

        // Validate the TaskValues in Elasticsearch
        verify(mockTaskValuesSearchRepository, times(0)).save(taskValues);
    }

    @Test
    @Transactional
    void getAllTaskValues() throws Exception {
        // Initialize the database
        taskValuesRepository.saveAndFlush(taskValues);

        // Get all the taskValuesList
        restTaskValuesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getTaskValues() throws Exception {
        // Initialize the database
        taskValuesRepository.saveAndFlush(taskValues);

        // Get the taskValues
        restTaskValuesMockMvc
            .perform(get(ENTITY_API_URL_ID, taskValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskValues.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingTaskValues() throws Exception {
        // Get the taskValues
        restTaskValuesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTaskValues() throws Exception {
        // Initialize the database
        taskValuesRepository.saveAndFlush(taskValues);

        int databaseSizeBeforeUpdate = taskValuesRepository.findAll().size();

        // Update the taskValues
        TaskValues updatedTaskValues = taskValuesRepository.findById(taskValues.getId()).get();
        // Disconnect from session so that the updates on updatedTaskValues are not directly saved in db
        em.detach(updatedTaskValues);
        updatedTaskValues.value(UPDATED_VALUE);
        TaskValuesDTO taskValuesDTO = taskValuesMapper.toDto(updatedTaskValues);

        restTaskValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskValuesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskValuesDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaskValues in the database
        List<TaskValues> taskValuesList = taskValuesRepository.findAll();
        assertThat(taskValuesList).hasSize(databaseSizeBeforeUpdate);
        TaskValues testTaskValues = taskValuesList.get(taskValuesList.size() - 1);
        assertThat(testTaskValues.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the TaskValues in Elasticsearch
        verify(mockTaskValuesSearchRepository).save(testTaskValues);
    }

    @Test
    @Transactional
    void putNonExistingTaskValues() throws Exception {
        int databaseSizeBeforeUpdate = taskValuesRepository.findAll().size();
        taskValues.setId(count.incrementAndGet());

        // Create the TaskValues
        TaskValuesDTO taskValuesDTO = taskValuesMapper.toDto(taskValues);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskValuesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskValues in the database
        List<TaskValues> taskValuesList = taskValuesRepository.findAll();
        assertThat(taskValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaskValues in Elasticsearch
        verify(mockTaskValuesSearchRepository, times(0)).save(taskValues);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaskValues() throws Exception {
        int databaseSizeBeforeUpdate = taskValuesRepository.findAll().size();
        taskValues.setId(count.incrementAndGet());

        // Create the TaskValues
        TaskValuesDTO taskValuesDTO = taskValuesMapper.toDto(taskValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskValues in the database
        List<TaskValues> taskValuesList = taskValuesRepository.findAll();
        assertThat(taskValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaskValues in Elasticsearch
        verify(mockTaskValuesSearchRepository, times(0)).save(taskValues);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaskValues() throws Exception {
        int databaseSizeBeforeUpdate = taskValuesRepository.findAll().size();
        taskValues.setId(count.incrementAndGet());

        // Create the TaskValues
        TaskValuesDTO taskValuesDTO = taskValuesMapper.toDto(taskValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskValuesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskValuesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskValues in the database
        List<TaskValues> taskValuesList = taskValuesRepository.findAll();
        assertThat(taskValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaskValues in Elasticsearch
        verify(mockTaskValuesSearchRepository, times(0)).save(taskValues);
    }

    @Test
    @Transactional
    void partialUpdateTaskValuesWithPatch() throws Exception {
        // Initialize the database
        taskValuesRepository.saveAndFlush(taskValues);

        int databaseSizeBeforeUpdate = taskValuesRepository.findAll().size();

        // Update the taskValues using partial update
        TaskValues partialUpdatedTaskValues = new TaskValues();
        partialUpdatedTaskValues.setId(taskValues.getId());

        restTaskValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskValues))
            )
            .andExpect(status().isOk());

        // Validate the TaskValues in the database
        List<TaskValues> taskValuesList = taskValuesRepository.findAll();
        assertThat(taskValuesList).hasSize(databaseSizeBeforeUpdate);
        TaskValues testTaskValues = taskValuesList.get(taskValuesList.size() - 1);
        assertThat(testTaskValues.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateTaskValuesWithPatch() throws Exception {
        // Initialize the database
        taskValuesRepository.saveAndFlush(taskValues);

        int databaseSizeBeforeUpdate = taskValuesRepository.findAll().size();

        // Update the taskValues using partial update
        TaskValues partialUpdatedTaskValues = new TaskValues();
        partialUpdatedTaskValues.setId(taskValues.getId());

        partialUpdatedTaskValues.value(UPDATED_VALUE);

        restTaskValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskValues))
            )
            .andExpect(status().isOk());

        // Validate the TaskValues in the database
        List<TaskValues> taskValuesList = taskValuesRepository.findAll();
        assertThat(taskValuesList).hasSize(databaseSizeBeforeUpdate);
        TaskValues testTaskValues = taskValuesList.get(taskValuesList.size() - 1);
        assertThat(testTaskValues.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingTaskValues() throws Exception {
        int databaseSizeBeforeUpdate = taskValuesRepository.findAll().size();
        taskValues.setId(count.incrementAndGet());

        // Create the TaskValues
        TaskValuesDTO taskValuesDTO = taskValuesMapper.toDto(taskValues);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskValuesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskValues in the database
        List<TaskValues> taskValuesList = taskValuesRepository.findAll();
        assertThat(taskValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaskValues in Elasticsearch
        verify(mockTaskValuesSearchRepository, times(0)).save(taskValues);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaskValues() throws Exception {
        int databaseSizeBeforeUpdate = taskValuesRepository.findAll().size();
        taskValues.setId(count.incrementAndGet());

        // Create the TaskValues
        TaskValuesDTO taskValuesDTO = taskValuesMapper.toDto(taskValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskValuesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskValues in the database
        List<TaskValues> taskValuesList = taskValuesRepository.findAll();
        assertThat(taskValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaskValues in Elasticsearch
        verify(mockTaskValuesSearchRepository, times(0)).save(taskValues);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaskValues() throws Exception {
        int databaseSizeBeforeUpdate = taskValuesRepository.findAll().size();
        taskValues.setId(count.incrementAndGet());

        // Create the TaskValues
        TaskValuesDTO taskValuesDTO = taskValuesMapper.toDto(taskValues);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskValuesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taskValuesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskValues in the database
        List<TaskValues> taskValuesList = taskValuesRepository.findAll();
        assertThat(taskValuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaskValues in Elasticsearch
        verify(mockTaskValuesSearchRepository, times(0)).save(taskValues);
    }

    @Test
    @Transactional
    void deleteTaskValues() throws Exception {
        // Initialize the database
        taskValuesRepository.saveAndFlush(taskValues);

        int databaseSizeBeforeDelete = taskValuesRepository.findAll().size();

        // Delete the taskValues
        restTaskValuesMockMvc
            .perform(delete(ENTITY_API_URL_ID, taskValues.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaskValues> taskValuesList = taskValuesRepository.findAll();
        assertThat(taskValuesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TaskValues in Elasticsearch
        verify(mockTaskValuesSearchRepository, times(1)).deleteById(taskValues.getId());
    }

    @Test
    @Transactional
    void searchTaskValues() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        taskValuesRepository.saveAndFlush(taskValues);
        when(mockTaskValuesSearchRepository.search(queryStringQuery("id:" + taskValues.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(taskValues), PageRequest.of(0, 1), 1));

        // Search the taskValues
        restTaskValuesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + taskValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
}
