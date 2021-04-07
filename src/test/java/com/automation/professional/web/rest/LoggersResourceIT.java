package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.Loggers;
import com.automation.professional.repository.LoggersRepository;
import com.automation.professional.repository.search.LoggersSearchRepository;
import com.automation.professional.service.dto.LoggersDTO;
import com.automation.professional.service.mapper.LoggersMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link LoggersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LoggersResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOG_DETAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOG_DETAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOG_DETAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOG_DETAIL_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/loggers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/loggers";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoggersRepository loggersRepository;

    @Autowired
    private LoggersMapper loggersMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.LoggersSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoggersSearchRepository mockLoggersSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoggersMockMvc;

    private Loggers loggers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loggers createEntity(EntityManager em) {
        Loggers loggers = new Loggers()
            .status(DEFAULT_STATUS)
            .logDetail(DEFAULT_LOG_DETAIL)
            .logDetailContentType(DEFAULT_LOG_DETAIL_CONTENT_TYPE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return loggers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loggers createUpdatedEntity(EntityManager em) {
        Loggers loggers = new Loggers()
            .status(UPDATED_STATUS)
            .logDetail(UPDATED_LOG_DETAIL)
            .logDetailContentType(UPDATED_LOG_DETAIL_CONTENT_TYPE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        return loggers;
    }

    @BeforeEach
    public void initTest() {
        loggers = createEntity(em);
    }

    @Test
    @Transactional
    void createLoggers() throws Exception {
        int databaseSizeBeforeCreate = loggersRepository.findAll().size();
        // Create the Loggers
        LoggersDTO loggersDTO = loggersMapper.toDto(loggers);
        restLoggersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loggersDTO)))
            .andExpect(status().isCreated());

        // Validate the Loggers in the database
        List<Loggers> loggersList = loggersRepository.findAll();
        assertThat(loggersList).hasSize(databaseSizeBeforeCreate + 1);
        Loggers testLoggers = loggersList.get(loggersList.size() - 1);
        assertThat(testLoggers.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLoggers.getLogDetail()).isEqualTo(DEFAULT_LOG_DETAIL);
        assertThat(testLoggers.getLogDetailContentType()).isEqualTo(DEFAULT_LOG_DETAIL_CONTENT_TYPE);
        assertThat(testLoggers.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the Loggers in Elasticsearch
        verify(mockLoggersSearchRepository, times(1)).save(testLoggers);
    }

    @Test
    @Transactional
    void createLoggersWithExistingId() throws Exception {
        // Create the Loggers with an existing ID
        loggers.setId(1L);
        LoggersDTO loggersDTO = loggersMapper.toDto(loggers);

        int databaseSizeBeforeCreate = loggersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoggersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loggersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Loggers in the database
        List<Loggers> loggersList = loggersRepository.findAll();
        assertThat(loggersList).hasSize(databaseSizeBeforeCreate);

        // Validate the Loggers in Elasticsearch
        verify(mockLoggersSearchRepository, times(0)).save(loggers);
    }

    @Test
    @Transactional
    void getAllLoggers() throws Exception {
        // Initialize the database
        loggersRepository.saveAndFlush(loggers);

        // Get all the loggersList
        restLoggersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loggers.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].logDetailContentType").value(hasItem(DEFAULT_LOG_DETAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logDetail").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOG_DETAIL))))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    void getLoggers() throws Exception {
        // Initialize the database
        loggersRepository.saveAndFlush(loggers);

        // Get the loggers
        restLoggersMockMvc
            .perform(get(ENTITY_API_URL_ID, loggers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loggers.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.logDetailContentType").value(DEFAULT_LOG_DETAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.logDetail").value(Base64Utils.encodeToString(DEFAULT_LOG_DETAIL)))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLoggers() throws Exception {
        // Get the loggers
        restLoggersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoggers() throws Exception {
        // Initialize the database
        loggersRepository.saveAndFlush(loggers);

        int databaseSizeBeforeUpdate = loggersRepository.findAll().size();

        // Update the loggers
        Loggers updatedLoggers = loggersRepository.findById(loggers.getId()).get();
        // Disconnect from session so that the updates on updatedLoggers are not directly saved in db
        em.detach(updatedLoggers);
        updatedLoggers
            .status(UPDATED_STATUS)
            .logDetail(UPDATED_LOG_DETAIL)
            .logDetailContentType(UPDATED_LOG_DETAIL_CONTENT_TYPE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        LoggersDTO loggersDTO = loggersMapper.toDto(updatedLoggers);

        restLoggersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loggersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loggersDTO))
            )
            .andExpect(status().isOk());

        // Validate the Loggers in the database
        List<Loggers> loggersList = loggersRepository.findAll();
        assertThat(loggersList).hasSize(databaseSizeBeforeUpdate);
        Loggers testLoggers = loggersList.get(loggersList.size() - 1);
        assertThat(testLoggers.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLoggers.getLogDetail()).isEqualTo(UPDATED_LOG_DETAIL);
        assertThat(testLoggers.getLogDetailContentType()).isEqualTo(UPDATED_LOG_DETAIL_CONTENT_TYPE);
        assertThat(testLoggers.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the Loggers in Elasticsearch
        verify(mockLoggersSearchRepository).save(testLoggers);
    }

    @Test
    @Transactional
    void putNonExistingLoggers() throws Exception {
        int databaseSizeBeforeUpdate = loggersRepository.findAll().size();
        loggers.setId(count.incrementAndGet());

        // Create the Loggers
        LoggersDTO loggersDTO = loggersMapper.toDto(loggers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoggersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loggersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loggersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loggers in the database
        List<Loggers> loggersList = loggersRepository.findAll();
        assertThat(loggersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Loggers in Elasticsearch
        verify(mockLoggersSearchRepository, times(0)).save(loggers);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoggers() throws Exception {
        int databaseSizeBeforeUpdate = loggersRepository.findAll().size();
        loggers.setId(count.incrementAndGet());

        // Create the Loggers
        LoggersDTO loggersDTO = loggersMapper.toDto(loggers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoggersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loggersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loggers in the database
        List<Loggers> loggersList = loggersRepository.findAll();
        assertThat(loggersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Loggers in Elasticsearch
        verify(mockLoggersSearchRepository, times(0)).save(loggers);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoggers() throws Exception {
        int databaseSizeBeforeUpdate = loggersRepository.findAll().size();
        loggers.setId(count.incrementAndGet());

        // Create the Loggers
        LoggersDTO loggersDTO = loggersMapper.toDto(loggers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoggersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loggersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Loggers in the database
        List<Loggers> loggersList = loggersRepository.findAll();
        assertThat(loggersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Loggers in Elasticsearch
        verify(mockLoggersSearchRepository, times(0)).save(loggers);
    }

    @Test
    @Transactional
    void partialUpdateLoggersWithPatch() throws Exception {
        // Initialize the database
        loggersRepository.saveAndFlush(loggers);

        int databaseSizeBeforeUpdate = loggersRepository.findAll().size();

        // Update the loggers using partial update
        Loggers partialUpdatedLoggers = new Loggers();
        partialUpdatedLoggers.setId(loggers.getId());

        partialUpdatedLoggers
            .logDetail(UPDATED_LOG_DETAIL)
            .logDetailContentType(UPDATED_LOG_DETAIL_CONTENT_TYPE)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restLoggersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoggers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoggers))
            )
            .andExpect(status().isOk());

        // Validate the Loggers in the database
        List<Loggers> loggersList = loggersRepository.findAll();
        assertThat(loggersList).hasSize(databaseSizeBeforeUpdate);
        Loggers testLoggers = loggersList.get(loggersList.size() - 1);
        assertThat(testLoggers.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLoggers.getLogDetail()).isEqualTo(UPDATED_LOG_DETAIL);
        assertThat(testLoggers.getLogDetailContentType()).isEqualTo(UPDATED_LOG_DETAIL_CONTENT_TYPE);
        assertThat(testLoggers.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void fullUpdateLoggersWithPatch() throws Exception {
        // Initialize the database
        loggersRepository.saveAndFlush(loggers);

        int databaseSizeBeforeUpdate = loggersRepository.findAll().size();

        // Update the loggers using partial update
        Loggers partialUpdatedLoggers = new Loggers();
        partialUpdatedLoggers.setId(loggers.getId());

        partialUpdatedLoggers
            .status(UPDATED_STATUS)
            .logDetail(UPDATED_LOG_DETAIL)
            .logDetailContentType(UPDATED_LOG_DETAIL_CONTENT_TYPE)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restLoggersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoggers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoggers))
            )
            .andExpect(status().isOk());

        // Validate the Loggers in the database
        List<Loggers> loggersList = loggersRepository.findAll();
        assertThat(loggersList).hasSize(databaseSizeBeforeUpdate);
        Loggers testLoggers = loggersList.get(loggersList.size() - 1);
        assertThat(testLoggers.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLoggers.getLogDetail()).isEqualTo(UPDATED_LOG_DETAIL);
        assertThat(testLoggers.getLogDetailContentType()).isEqualTo(UPDATED_LOG_DETAIL_CONTENT_TYPE);
        assertThat(testLoggers.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void patchNonExistingLoggers() throws Exception {
        int databaseSizeBeforeUpdate = loggersRepository.findAll().size();
        loggers.setId(count.incrementAndGet());

        // Create the Loggers
        LoggersDTO loggersDTO = loggersMapper.toDto(loggers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoggersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loggersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loggersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loggers in the database
        List<Loggers> loggersList = loggersRepository.findAll();
        assertThat(loggersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Loggers in Elasticsearch
        verify(mockLoggersSearchRepository, times(0)).save(loggers);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoggers() throws Exception {
        int databaseSizeBeforeUpdate = loggersRepository.findAll().size();
        loggers.setId(count.incrementAndGet());

        // Create the Loggers
        LoggersDTO loggersDTO = loggersMapper.toDto(loggers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoggersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loggersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loggers in the database
        List<Loggers> loggersList = loggersRepository.findAll();
        assertThat(loggersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Loggers in Elasticsearch
        verify(mockLoggersSearchRepository, times(0)).save(loggers);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoggers() throws Exception {
        int databaseSizeBeforeUpdate = loggersRepository.findAll().size();
        loggers.setId(count.incrementAndGet());

        // Create the Loggers
        LoggersDTO loggersDTO = loggersMapper.toDto(loggers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoggersMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(loggersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Loggers in the database
        List<Loggers> loggersList = loggersRepository.findAll();
        assertThat(loggersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Loggers in Elasticsearch
        verify(mockLoggersSearchRepository, times(0)).save(loggers);
    }

    @Test
    @Transactional
    void deleteLoggers() throws Exception {
        // Initialize the database
        loggersRepository.saveAndFlush(loggers);

        int databaseSizeBeforeDelete = loggersRepository.findAll().size();

        // Delete the loggers
        restLoggersMockMvc
            .perform(delete(ENTITY_API_URL_ID, loggers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Loggers> loggersList = loggersRepository.findAll();
        assertThat(loggersList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Loggers in Elasticsearch
        verify(mockLoggersSearchRepository, times(1)).deleteById(loggers.getId());
    }

    @Test
    @Transactional
    void searchLoggers() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loggersRepository.saveAndFlush(loggers);
        when(mockLoggersSearchRepository.search(queryStringQuery("id:" + loggers.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(loggers), PageRequest.of(0, 1), 1));

        // Search the loggers
        restLoggersMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + loggers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loggers.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].logDetailContentType").value(hasItem(DEFAULT_LOG_DETAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logDetail").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOG_DETAIL))))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
}
