package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.Schedulers;
import com.automation.professional.domain.enumeration.SchedulerStatus;
import com.automation.professional.repository.SchedulersRepository;
import com.automation.professional.repository.search.SchedulersSearchRepository;
import com.automation.professional.service.dto.SchedulersDTO;
import com.automation.professional.service.mapper.SchedulersMapper;
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
 * Integration tests for the {@link SchedulersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchedulersResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_SOURCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final SchedulerStatus DEFAULT_STATUS = SchedulerStatus.Open;
    private static final SchedulerStatus UPDATED_STATUS = SchedulerStatus.Processing;

    private static final String ENTITY_API_URL = "/api/schedulers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/schedulers";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchedulersRepository schedulersRepository;

    @Autowired
    private SchedulersMapper schedulersMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.SchedulersSearchRepositoryMockConfiguration
     */
    @Autowired
    private SchedulersSearchRepository mockSchedulersSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchedulersMockMvc;

    private Schedulers schedulers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schedulers createEntity(EntityManager em) {
        Schedulers schedulers = new Schedulers()
            .url(DEFAULT_URL)
            .title(DEFAULT_TITLE)
            .otherSource(DEFAULT_OTHER_SOURCE)
            .count(DEFAULT_COUNT)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .owner(DEFAULT_OWNER)
            .status(DEFAULT_STATUS);
        return schedulers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schedulers createUpdatedEntity(EntityManager em) {
        Schedulers schedulers = new Schedulers()
            .url(UPDATED_URL)
            .title(UPDATED_TITLE)
            .otherSource(UPDATED_OTHER_SOURCE)
            .count(UPDATED_COUNT)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .owner(UPDATED_OWNER)
            .status(UPDATED_STATUS);
        return schedulers;
    }

    @BeforeEach
    public void initTest() {
        schedulers = createEntity(em);
    }

    @Test
    @Transactional
    void createSchedulers() throws Exception {
        int databaseSizeBeforeCreate = schedulersRepository.findAll().size();
        // Create the Schedulers
        SchedulersDTO schedulersDTO = schedulersMapper.toDto(schedulers);
        restSchedulersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schedulersDTO)))
            .andExpect(status().isCreated());

        // Validate the Schedulers in the database
        List<Schedulers> schedulersList = schedulersRepository.findAll();
        assertThat(schedulersList).hasSize(databaseSizeBeforeCreate + 1);
        Schedulers testSchedulers = schedulersList.get(schedulersList.size() - 1);
        assertThat(testSchedulers.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testSchedulers.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSchedulers.getOtherSource()).isEqualTo(DEFAULT_OTHER_SOURCE);
        assertThat(testSchedulers.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testSchedulers.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testSchedulers.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testSchedulers.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Schedulers in Elasticsearch
        verify(mockSchedulersSearchRepository, times(1)).save(testSchedulers);
    }

    @Test
    @Transactional
    void createSchedulersWithExistingId() throws Exception {
        // Create the Schedulers with an existing ID
        schedulers.setId(1L);
        SchedulersDTO schedulersDTO = schedulersMapper.toDto(schedulers);

        int databaseSizeBeforeCreate = schedulersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedulersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schedulersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Schedulers in the database
        List<Schedulers> schedulersList = schedulersRepository.findAll();
        assertThat(schedulersList).hasSize(databaseSizeBeforeCreate);

        // Validate the Schedulers in Elasticsearch
        verify(mockSchedulersSearchRepository, times(0)).save(schedulers);
    }

    @Test
    @Transactional
    void getAllSchedulers() throws Exception {
        // Initialize the database
        schedulersRepository.saveAndFlush(schedulers);

        // Get all the schedulersList
        restSchedulersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulers.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].otherSource").value(hasItem(DEFAULT_OTHER_SOURCE)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getSchedulers() throws Exception {
        // Initialize the database
        schedulersRepository.saveAndFlush(schedulers);

        // Get the schedulers
        restSchedulersMockMvc
            .perform(get(ENTITY_API_URL_ID, schedulers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schedulers.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.otherSource").value(DEFAULT_OTHER_SOURCE))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSchedulers() throws Exception {
        // Get the schedulers
        restSchedulersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchedulers() throws Exception {
        // Initialize the database
        schedulersRepository.saveAndFlush(schedulers);

        int databaseSizeBeforeUpdate = schedulersRepository.findAll().size();

        // Update the schedulers
        Schedulers updatedSchedulers = schedulersRepository.findById(schedulers.getId()).get();
        // Disconnect from session so that the updates on updatedSchedulers are not directly saved in db
        em.detach(updatedSchedulers);
        updatedSchedulers
            .url(UPDATED_URL)
            .title(UPDATED_TITLE)
            .otherSource(UPDATED_OTHER_SOURCE)
            .count(UPDATED_COUNT)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .owner(UPDATED_OWNER)
            .status(UPDATED_STATUS);
        SchedulersDTO schedulersDTO = schedulersMapper.toDto(updatedSchedulers);

        restSchedulersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulersDTO))
            )
            .andExpect(status().isOk());

        // Validate the Schedulers in the database
        List<Schedulers> schedulersList = schedulersRepository.findAll();
        assertThat(schedulersList).hasSize(databaseSizeBeforeUpdate);
        Schedulers testSchedulers = schedulersList.get(schedulersList.size() - 1);
        assertThat(testSchedulers.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testSchedulers.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSchedulers.getOtherSource()).isEqualTo(UPDATED_OTHER_SOURCE);
        assertThat(testSchedulers.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testSchedulers.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testSchedulers.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testSchedulers.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Schedulers in Elasticsearch
        verify(mockSchedulersSearchRepository).save(testSchedulers);
    }

    @Test
    @Transactional
    void putNonExistingSchedulers() throws Exception {
        int databaseSizeBeforeUpdate = schedulersRepository.findAll().size();
        schedulers.setId(count.incrementAndGet());

        // Create the Schedulers
        SchedulersDTO schedulersDTO = schedulersMapper.toDto(schedulers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedulers in the database
        List<Schedulers> schedulersList = schedulersRepository.findAll();
        assertThat(schedulersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Schedulers in Elasticsearch
        verify(mockSchedulersSearchRepository, times(0)).save(schedulers);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchedulers() throws Exception {
        int databaseSizeBeforeUpdate = schedulersRepository.findAll().size();
        schedulers.setId(count.incrementAndGet());

        // Create the Schedulers
        SchedulersDTO schedulersDTO = schedulersMapper.toDto(schedulers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedulers in the database
        List<Schedulers> schedulersList = schedulersRepository.findAll();
        assertThat(schedulersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Schedulers in Elasticsearch
        verify(mockSchedulersSearchRepository, times(0)).save(schedulers);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchedulers() throws Exception {
        int databaseSizeBeforeUpdate = schedulersRepository.findAll().size();
        schedulers.setId(count.incrementAndGet());

        // Create the Schedulers
        SchedulersDTO schedulersDTO = schedulersMapper.toDto(schedulers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schedulersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Schedulers in the database
        List<Schedulers> schedulersList = schedulersRepository.findAll();
        assertThat(schedulersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Schedulers in Elasticsearch
        verify(mockSchedulersSearchRepository, times(0)).save(schedulers);
    }

    @Test
    @Transactional
    void partialUpdateSchedulersWithPatch() throws Exception {
        // Initialize the database
        schedulersRepository.saveAndFlush(schedulers);

        int databaseSizeBeforeUpdate = schedulersRepository.findAll().size();

        // Update the schedulers using partial update
        Schedulers partialUpdatedSchedulers = new Schedulers();
        partialUpdatedSchedulers.setId(schedulers.getId());

        restSchedulersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulers))
            )
            .andExpect(status().isOk());

        // Validate the Schedulers in the database
        List<Schedulers> schedulersList = schedulersRepository.findAll();
        assertThat(schedulersList).hasSize(databaseSizeBeforeUpdate);
        Schedulers testSchedulers = schedulersList.get(schedulersList.size() - 1);
        assertThat(testSchedulers.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testSchedulers.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSchedulers.getOtherSource()).isEqualTo(DEFAULT_OTHER_SOURCE);
        assertThat(testSchedulers.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testSchedulers.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testSchedulers.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testSchedulers.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateSchedulersWithPatch() throws Exception {
        // Initialize the database
        schedulersRepository.saveAndFlush(schedulers);

        int databaseSizeBeforeUpdate = schedulersRepository.findAll().size();

        // Update the schedulers using partial update
        Schedulers partialUpdatedSchedulers = new Schedulers();
        partialUpdatedSchedulers.setId(schedulers.getId());

        partialUpdatedSchedulers
            .url(UPDATED_URL)
            .title(UPDATED_TITLE)
            .otherSource(UPDATED_OTHER_SOURCE)
            .count(UPDATED_COUNT)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .owner(UPDATED_OWNER)
            .status(UPDATED_STATUS);

        restSchedulersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulers))
            )
            .andExpect(status().isOk());

        // Validate the Schedulers in the database
        List<Schedulers> schedulersList = schedulersRepository.findAll();
        assertThat(schedulersList).hasSize(databaseSizeBeforeUpdate);
        Schedulers testSchedulers = schedulersList.get(schedulersList.size() - 1);
        assertThat(testSchedulers.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testSchedulers.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSchedulers.getOtherSource()).isEqualTo(UPDATED_OTHER_SOURCE);
        assertThat(testSchedulers.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testSchedulers.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testSchedulers.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testSchedulers.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingSchedulers() throws Exception {
        int databaseSizeBeforeUpdate = schedulersRepository.findAll().size();
        schedulers.setId(count.incrementAndGet());

        // Create the Schedulers
        SchedulersDTO schedulersDTO = schedulersMapper.toDto(schedulers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schedulersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedulers in the database
        List<Schedulers> schedulersList = schedulersRepository.findAll();
        assertThat(schedulersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Schedulers in Elasticsearch
        verify(mockSchedulersSearchRepository, times(0)).save(schedulers);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchedulers() throws Exception {
        int databaseSizeBeforeUpdate = schedulersRepository.findAll().size();
        schedulers.setId(count.incrementAndGet());

        // Create the Schedulers
        SchedulersDTO schedulersDTO = schedulersMapper.toDto(schedulers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedulers in the database
        List<Schedulers> schedulersList = schedulersRepository.findAll();
        assertThat(schedulersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Schedulers in Elasticsearch
        verify(mockSchedulersSearchRepository, times(0)).save(schedulers);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchedulers() throws Exception {
        int databaseSizeBeforeUpdate = schedulersRepository.findAll().size();
        schedulers.setId(count.incrementAndGet());

        // Create the Schedulers
        SchedulersDTO schedulersDTO = schedulersMapper.toDto(schedulers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulersMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(schedulersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Schedulers in the database
        List<Schedulers> schedulersList = schedulersRepository.findAll();
        assertThat(schedulersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Schedulers in Elasticsearch
        verify(mockSchedulersSearchRepository, times(0)).save(schedulers);
    }

    @Test
    @Transactional
    void deleteSchedulers() throws Exception {
        // Initialize the database
        schedulersRepository.saveAndFlush(schedulers);

        int databaseSizeBeforeDelete = schedulersRepository.findAll().size();

        // Delete the schedulers
        restSchedulersMockMvc
            .perform(delete(ENTITY_API_URL_ID, schedulers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Schedulers> schedulersList = schedulersRepository.findAll();
        assertThat(schedulersList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Schedulers in Elasticsearch
        verify(mockSchedulersSearchRepository, times(1)).deleteById(schedulers.getId());
    }

    @Test
    @Transactional
    void searchSchedulers() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        schedulersRepository.saveAndFlush(schedulers);
        when(mockSchedulersSearchRepository.search(queryStringQuery("id:" + schedulers.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(schedulers), PageRequest.of(0, 1), 1));

        // Search the schedulers
        restSchedulersMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + schedulers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulers.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].otherSource").value(hasItem(DEFAULT_OTHER_SOURCE)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
