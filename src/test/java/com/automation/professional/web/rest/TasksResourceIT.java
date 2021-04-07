package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.Tasks;
import com.automation.professional.domain.enumeration.Social;
import com.automation.professional.domain.enumeration.TaskType;
import com.automation.professional.repository.TasksRepository;
import com.automation.professional.repository.search.TasksSearchRepository;
import com.automation.professional.service.dto.TasksDTO;
import com.automation.professional.service.mapper.TasksMapper;
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
 * Integration tests for the {@link TasksResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TasksResourceIT {

    private static final String DEFAULT_TASK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TASK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Social DEFAULT_SOCIAL = Social.Facebook;
    private static final Social UPDATED_SOCIAL = Social.Youtube;

    private static final TaskType DEFAULT_TYPE = TaskType.Stream;
    private static final TaskType UPDATED_TYPE = TaskType.NonStream;

    private static final String ENTITY_API_URL = "/api/tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/tasks";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private TasksMapper tasksMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.TasksSearchRepositoryMockConfiguration
     */
    @Autowired
    private TasksSearchRepository mockTasksSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTasksMockMvc;

    private Tasks tasks;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tasks createEntity(EntityManager em) {
        Tasks tasks = new Tasks()
            .taskName(DEFAULT_TASK_NAME)
            .description(DEFAULT_DESCRIPTION)
            .source(DEFAULT_SOURCE)
            .price(DEFAULT_PRICE)
            .social(DEFAULT_SOCIAL)
            .type(DEFAULT_TYPE);
        return tasks;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tasks createUpdatedEntity(EntityManager em) {
        Tasks tasks = new Tasks()
            .taskName(UPDATED_TASK_NAME)
            .description(UPDATED_DESCRIPTION)
            .source(UPDATED_SOURCE)
            .price(UPDATED_PRICE)
            .social(UPDATED_SOCIAL)
            .type(UPDATED_TYPE);
        return tasks;
    }

    @BeforeEach
    public void initTest() {
        tasks = createEntity(em);
    }

    @Test
    @Transactional
    void createTasks() throws Exception {
        int databaseSizeBeforeCreate = tasksRepository.findAll().size();
        // Create the Tasks
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);
        restTasksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isCreated());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeCreate + 1);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getTaskName()).isEqualTo(DEFAULT_TASK_NAME);
        assertThat(testTasks.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTasks.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testTasks.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testTasks.getSocial()).isEqualTo(DEFAULT_SOCIAL);
        assertThat(testTasks.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Tasks in Elasticsearch
        verify(mockTasksSearchRepository, times(1)).save(testTasks);
    }

    @Test
    @Transactional
    void createTasksWithExistingId() throws Exception {
        // Create the Tasks with an existing ID
        tasks.setId(1L);
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        int databaseSizeBeforeCreate = tasksRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTasksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeCreate);

        // Validate the Tasks in Elasticsearch
        verify(mockTasksSearchRepository, times(0)).save(tasks);
    }

    @Test
    @Transactional
    void getAllTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList
        restTasksMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tasks.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskName").value(hasItem(DEFAULT_TASK_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].social").value(hasItem(DEFAULT_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get the tasks
        restTasksMockMvc
            .perform(get(ENTITY_API_URL_ID, tasks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tasks.getId().intValue()))
            .andExpect(jsonPath("$.taskName").value(DEFAULT_TASK_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.social").value(DEFAULT_SOCIAL.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTasks() throws Exception {
        // Get the tasks
        restTasksMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Update the tasks
        Tasks updatedTasks = tasksRepository.findById(tasks.getId()).get();
        // Disconnect from session so that the updates on updatedTasks are not directly saved in db
        em.detach(updatedTasks);
        updatedTasks
            .taskName(UPDATED_TASK_NAME)
            .description(UPDATED_DESCRIPTION)
            .source(UPDATED_SOURCE)
            .price(UPDATED_PRICE)
            .social(UPDATED_SOCIAL)
            .type(UPDATED_TYPE);
        TasksDTO tasksDTO = tasksMapper.toDto(updatedTasks);

        restTasksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tasksDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tasksDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getTaskName()).isEqualTo(UPDATED_TASK_NAME);
        assertThat(testTasks.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTasks.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testTasks.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTasks.getSocial()).isEqualTo(UPDATED_SOCIAL);
        assertThat(testTasks.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Tasks in Elasticsearch
        verify(mockTasksSearchRepository).save(testTasks);
    }

    @Test
    @Transactional
    void putNonExistingTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // Create the Tasks
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tasksDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tasksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tasks in Elasticsearch
        verify(mockTasksSearchRepository, times(0)).save(tasks);
    }

    @Test
    @Transactional
    void putWithIdMismatchTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // Create the Tasks
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tasksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tasks in Elasticsearch
        verify(mockTasksSearchRepository, times(0)).save(tasks);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // Create the Tasks
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tasks in Elasticsearch
        verify(mockTasksSearchRepository, times(0)).save(tasks);
    }

    @Test
    @Transactional
    void partialUpdateTasksWithPatch() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Update the tasks using partial update
        Tasks partialUpdatedTasks = new Tasks();
        partialUpdatedTasks.setId(tasks.getId());

        partialUpdatedTasks.taskName(UPDATED_TASK_NAME).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);

        restTasksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTasks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTasks))
            )
            .andExpect(status().isOk());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getTaskName()).isEqualTo(UPDATED_TASK_NAME);
        assertThat(testTasks.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTasks.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testTasks.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTasks.getSocial()).isEqualTo(DEFAULT_SOCIAL);
        assertThat(testTasks.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateTasksWithPatch() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Update the tasks using partial update
        Tasks partialUpdatedTasks = new Tasks();
        partialUpdatedTasks.setId(tasks.getId());

        partialUpdatedTasks
            .taskName(UPDATED_TASK_NAME)
            .description(UPDATED_DESCRIPTION)
            .source(UPDATED_SOURCE)
            .price(UPDATED_PRICE)
            .social(UPDATED_SOCIAL)
            .type(UPDATED_TYPE);

        restTasksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTasks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTasks))
            )
            .andExpect(status().isOk());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getTaskName()).isEqualTo(UPDATED_TASK_NAME);
        assertThat(testTasks.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTasks.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testTasks.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTasks.getSocial()).isEqualTo(UPDATED_SOCIAL);
        assertThat(testTasks.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // Create the Tasks
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tasksDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tasksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tasks in Elasticsearch
        verify(mockTasksSearchRepository, times(0)).save(tasks);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // Create the Tasks
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tasksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tasks in Elasticsearch
        verify(mockTasksSearchRepository, times(0)).save(tasks);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // Create the Tasks
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tasks in Elasticsearch
        verify(mockTasksSearchRepository, times(0)).save(tasks);
    }

    @Test
    @Transactional
    void deleteTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        int databaseSizeBeforeDelete = tasksRepository.findAll().size();

        // Delete the tasks
        restTasksMockMvc
            .perform(delete(ENTITY_API_URL_ID, tasks.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Tasks in Elasticsearch
        verify(mockTasksSearchRepository, times(1)).deleteById(tasks.getId());
    }

    @Test
    @Transactional
    void searchTasks() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);
        when(mockTasksSearchRepository.search(queryStringQuery("id:" + tasks.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tasks), PageRequest.of(0, 1), 1));

        // Search the tasks
        restTasksMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tasks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tasks.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskName").value(hasItem(DEFAULT_TASK_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].social").value(hasItem(DEFAULT_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
}
