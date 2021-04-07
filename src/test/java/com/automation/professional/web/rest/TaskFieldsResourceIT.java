package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.TaskFields;
import com.automation.professional.repository.TaskFieldsRepository;
import com.automation.professional.repository.search.TaskFieldsSearchRepository;
import com.automation.professional.service.dto.TaskFieldsDTO;
import com.automation.professional.service.mapper.TaskFieldsMapper;
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
 * Integration tests for the {@link TaskFieldsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TaskFieldsResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/task-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/task-fields";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskFieldsRepository taskFieldsRepository;

    @Autowired
    private TaskFieldsMapper taskFieldsMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.TaskFieldsSearchRepositoryMockConfiguration
     */
    @Autowired
    private TaskFieldsSearchRepository mockTaskFieldsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskFieldsMockMvc;

    private TaskFields taskFields;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskFields createEntity(EntityManager em) {
        TaskFields taskFields = new TaskFields().fieldName(DEFAULT_FIELD_NAME);
        return taskFields;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskFields createUpdatedEntity(EntityManager em) {
        TaskFields taskFields = new TaskFields().fieldName(UPDATED_FIELD_NAME);
        return taskFields;
    }

    @BeforeEach
    public void initTest() {
        taskFields = createEntity(em);
    }

    @Test
    @Transactional
    void createTaskFields() throws Exception {
        int databaseSizeBeforeCreate = taskFieldsRepository.findAll().size();
        // Create the TaskFields
        TaskFieldsDTO taskFieldsDTO = taskFieldsMapper.toDto(taskFields);
        restTaskFieldsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskFieldsDTO)))
            .andExpect(status().isCreated());

        // Validate the TaskFields in the database
        List<TaskFields> taskFieldsList = taskFieldsRepository.findAll();
        assertThat(taskFieldsList).hasSize(databaseSizeBeforeCreate + 1);
        TaskFields testTaskFields = taskFieldsList.get(taskFieldsList.size() - 1);
        assertThat(testTaskFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);

        // Validate the TaskFields in Elasticsearch
        verify(mockTaskFieldsSearchRepository, times(1)).save(testTaskFields);
    }

    @Test
    @Transactional
    void createTaskFieldsWithExistingId() throws Exception {
        // Create the TaskFields with an existing ID
        taskFields.setId(1L);
        TaskFieldsDTO taskFieldsDTO = taskFieldsMapper.toDto(taskFields);

        int databaseSizeBeforeCreate = taskFieldsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskFieldsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskFieldsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaskFields in the database
        List<TaskFields> taskFieldsList = taskFieldsRepository.findAll();
        assertThat(taskFieldsList).hasSize(databaseSizeBeforeCreate);

        // Validate the TaskFields in Elasticsearch
        verify(mockTaskFieldsSearchRepository, times(0)).save(taskFields);
    }

    @Test
    @Transactional
    void getAllTaskFields() throws Exception {
        // Initialize the database
        taskFieldsRepository.saveAndFlush(taskFields);

        // Get all the taskFieldsList
        restTaskFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }

    @Test
    @Transactional
    void getTaskFields() throws Exception {
        // Initialize the database
        taskFieldsRepository.saveAndFlush(taskFields);

        // Get the taskFields
        restTaskFieldsMockMvc
            .perform(get(ENTITY_API_URL_ID, taskFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskFields.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTaskFields() throws Exception {
        // Get the taskFields
        restTaskFieldsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTaskFields() throws Exception {
        // Initialize the database
        taskFieldsRepository.saveAndFlush(taskFields);

        int databaseSizeBeforeUpdate = taskFieldsRepository.findAll().size();

        // Update the taskFields
        TaskFields updatedTaskFields = taskFieldsRepository.findById(taskFields.getId()).get();
        // Disconnect from session so that the updates on updatedTaskFields are not directly saved in db
        em.detach(updatedTaskFields);
        updatedTaskFields.fieldName(UPDATED_FIELD_NAME);
        TaskFieldsDTO taskFieldsDTO = taskFieldsMapper.toDto(updatedTaskFields);

        restTaskFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskFieldsDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaskFields in the database
        List<TaskFields> taskFieldsList = taskFieldsRepository.findAll();
        assertThat(taskFieldsList).hasSize(databaseSizeBeforeUpdate);
        TaskFields testTaskFields = taskFieldsList.get(taskFieldsList.size() - 1);
        assertThat(testTaskFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);

        // Validate the TaskFields in Elasticsearch
        verify(mockTaskFieldsSearchRepository).save(testTaskFields);
    }

    @Test
    @Transactional
    void putNonExistingTaskFields() throws Exception {
        int databaseSizeBeforeUpdate = taskFieldsRepository.findAll().size();
        taskFields.setId(count.incrementAndGet());

        // Create the TaskFields
        TaskFieldsDTO taskFieldsDTO = taskFieldsMapper.toDto(taskFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskFields in the database
        List<TaskFields> taskFieldsList = taskFieldsRepository.findAll();
        assertThat(taskFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaskFields in Elasticsearch
        verify(mockTaskFieldsSearchRepository, times(0)).save(taskFields);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaskFields() throws Exception {
        int databaseSizeBeforeUpdate = taskFieldsRepository.findAll().size();
        taskFields.setId(count.incrementAndGet());

        // Create the TaskFields
        TaskFieldsDTO taskFieldsDTO = taskFieldsMapper.toDto(taskFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskFields in the database
        List<TaskFields> taskFieldsList = taskFieldsRepository.findAll();
        assertThat(taskFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaskFields in Elasticsearch
        verify(mockTaskFieldsSearchRepository, times(0)).save(taskFields);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaskFields() throws Exception {
        int databaseSizeBeforeUpdate = taskFieldsRepository.findAll().size();
        taskFields.setId(count.incrementAndGet());

        // Create the TaskFields
        TaskFieldsDTO taskFieldsDTO = taskFieldsMapper.toDto(taskFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskFieldsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskFieldsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskFields in the database
        List<TaskFields> taskFieldsList = taskFieldsRepository.findAll();
        assertThat(taskFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaskFields in Elasticsearch
        verify(mockTaskFieldsSearchRepository, times(0)).save(taskFields);
    }

    @Test
    @Transactional
    void partialUpdateTaskFieldsWithPatch() throws Exception {
        // Initialize the database
        taskFieldsRepository.saveAndFlush(taskFields);

        int databaseSizeBeforeUpdate = taskFieldsRepository.findAll().size();

        // Update the taskFields using partial update
        TaskFields partialUpdatedTaskFields = new TaskFields();
        partialUpdatedTaskFields.setId(taskFields.getId());

        restTaskFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskFields))
            )
            .andExpect(status().isOk());

        // Validate the TaskFields in the database
        List<TaskFields> taskFieldsList = taskFieldsRepository.findAll();
        assertThat(taskFieldsList).hasSize(databaseSizeBeforeUpdate);
        TaskFields testTaskFields = taskFieldsList.get(taskFieldsList.size() - 1);
        assertThat(testTaskFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTaskFieldsWithPatch() throws Exception {
        // Initialize the database
        taskFieldsRepository.saveAndFlush(taskFields);

        int databaseSizeBeforeUpdate = taskFieldsRepository.findAll().size();

        // Update the taskFields using partial update
        TaskFields partialUpdatedTaskFields = new TaskFields();
        partialUpdatedTaskFields.setId(taskFields.getId());

        partialUpdatedTaskFields.fieldName(UPDATED_FIELD_NAME);

        restTaskFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskFields))
            )
            .andExpect(status().isOk());

        // Validate the TaskFields in the database
        List<TaskFields> taskFieldsList = taskFieldsRepository.findAll();
        assertThat(taskFieldsList).hasSize(databaseSizeBeforeUpdate);
        TaskFields testTaskFields = taskFieldsList.get(taskFieldsList.size() - 1);
        assertThat(testTaskFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTaskFields() throws Exception {
        int databaseSizeBeforeUpdate = taskFieldsRepository.findAll().size();
        taskFields.setId(count.incrementAndGet());

        // Create the TaskFields
        TaskFieldsDTO taskFieldsDTO = taskFieldsMapper.toDto(taskFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskFieldsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskFields in the database
        List<TaskFields> taskFieldsList = taskFieldsRepository.findAll();
        assertThat(taskFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaskFields in Elasticsearch
        verify(mockTaskFieldsSearchRepository, times(0)).save(taskFields);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaskFields() throws Exception {
        int databaseSizeBeforeUpdate = taskFieldsRepository.findAll().size();
        taskFields.setId(count.incrementAndGet());

        // Create the TaskFields
        TaskFieldsDTO taskFieldsDTO = taskFieldsMapper.toDto(taskFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskFields in the database
        List<TaskFields> taskFieldsList = taskFieldsRepository.findAll();
        assertThat(taskFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaskFields in Elasticsearch
        verify(mockTaskFieldsSearchRepository, times(0)).save(taskFields);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaskFields() throws Exception {
        int databaseSizeBeforeUpdate = taskFieldsRepository.findAll().size();
        taskFields.setId(count.incrementAndGet());

        // Create the TaskFields
        TaskFieldsDTO taskFieldsDTO = taskFieldsMapper.toDto(taskFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taskFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskFields in the database
        List<TaskFields> taskFieldsList = taskFieldsRepository.findAll();
        assertThat(taskFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaskFields in Elasticsearch
        verify(mockTaskFieldsSearchRepository, times(0)).save(taskFields);
    }

    @Test
    @Transactional
    void deleteTaskFields() throws Exception {
        // Initialize the database
        taskFieldsRepository.saveAndFlush(taskFields);

        int databaseSizeBeforeDelete = taskFieldsRepository.findAll().size();

        // Delete the taskFields
        restTaskFieldsMockMvc
            .perform(delete(ENTITY_API_URL_ID, taskFields.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaskFields> taskFieldsList = taskFieldsRepository.findAll();
        assertThat(taskFieldsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TaskFields in Elasticsearch
        verify(mockTaskFieldsSearchRepository, times(1)).deleteById(taskFields.getId());
    }

    @Test
    @Transactional
    void searchTaskFields() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        taskFieldsRepository.saveAndFlush(taskFields);
        when(mockTaskFieldsSearchRepository.search(queryStringQuery("id:" + taskFields.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(taskFields), PageRequest.of(0, 1), 1));

        // Search the taskFields
        restTaskFieldsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + taskFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }
}
