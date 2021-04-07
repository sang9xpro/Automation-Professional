package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.AccountFields;
import com.automation.professional.repository.AccountFieldsRepository;
import com.automation.professional.repository.search.AccountFieldsSearchRepository;
import com.automation.professional.service.dto.AccountFieldsDTO;
import com.automation.professional.service.mapper.AccountFieldsMapper;
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
 * Integration tests for the {@link AccountFieldsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AccountFieldsResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/account-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/account-fields";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountFieldsRepository accountFieldsRepository;

    @Autowired
    private AccountFieldsMapper accountFieldsMapper;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.AccountFieldsSearchRepositoryMockConfiguration
     */
    @Autowired
    private AccountFieldsSearchRepository mockAccountFieldsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountFieldsMockMvc;

    private AccountFields accountFields;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountFields createEntity(EntityManager em) {
        AccountFields accountFields = new AccountFields().fieldName(DEFAULT_FIELD_NAME);
        return accountFields;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountFields createUpdatedEntity(EntityManager em) {
        AccountFields accountFields = new AccountFields().fieldName(UPDATED_FIELD_NAME);
        return accountFields;
    }

    @BeforeEach
    public void initTest() {
        accountFields = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountFields() throws Exception {
        int databaseSizeBeforeCreate = accountFieldsRepository.findAll().size();
        // Create the AccountFields
        AccountFieldsDTO accountFieldsDTO = accountFieldsMapper.toDto(accountFields);
        restAccountFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountFieldsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AccountFields in the database
        List<AccountFields> accountFieldsList = accountFieldsRepository.findAll();
        assertThat(accountFieldsList).hasSize(databaseSizeBeforeCreate + 1);
        AccountFields testAccountFields = accountFieldsList.get(accountFieldsList.size() - 1);
        assertThat(testAccountFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);

        // Validate the AccountFields in Elasticsearch
        verify(mockAccountFieldsSearchRepository, times(1)).save(testAccountFields);
    }

    @Test
    @Transactional
    void createAccountFieldsWithExistingId() throws Exception {
        // Create the AccountFields with an existing ID
        accountFields.setId(1L);
        AccountFieldsDTO accountFieldsDTO = accountFieldsMapper.toDto(accountFields);

        int databaseSizeBeforeCreate = accountFieldsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountFields in the database
        List<AccountFields> accountFieldsList = accountFieldsRepository.findAll();
        assertThat(accountFieldsList).hasSize(databaseSizeBeforeCreate);

        // Validate the AccountFields in Elasticsearch
        verify(mockAccountFieldsSearchRepository, times(0)).save(accountFields);
    }

    @Test
    @Transactional
    void getAllAccountFields() throws Exception {
        // Initialize the database
        accountFieldsRepository.saveAndFlush(accountFields);

        // Get all the accountFieldsList
        restAccountFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }

    @Test
    @Transactional
    void getAccountFields() throws Exception {
        // Initialize the database
        accountFieldsRepository.saveAndFlush(accountFields);

        // Get the accountFields
        restAccountFieldsMockMvc
            .perform(get(ENTITY_API_URL_ID, accountFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountFields.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAccountFields() throws Exception {
        // Get the accountFields
        restAccountFieldsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccountFields() throws Exception {
        // Initialize the database
        accountFieldsRepository.saveAndFlush(accountFields);

        int databaseSizeBeforeUpdate = accountFieldsRepository.findAll().size();

        // Update the accountFields
        AccountFields updatedAccountFields = accountFieldsRepository.findById(accountFields.getId()).get();
        // Disconnect from session so that the updates on updatedAccountFields are not directly saved in db
        em.detach(updatedAccountFields);
        updatedAccountFields.fieldName(UPDATED_FIELD_NAME);
        AccountFieldsDTO accountFieldsDTO = accountFieldsMapper.toDto(updatedAccountFields);

        restAccountFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountFieldsDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccountFields in the database
        List<AccountFields> accountFieldsList = accountFieldsRepository.findAll();
        assertThat(accountFieldsList).hasSize(databaseSizeBeforeUpdate);
        AccountFields testAccountFields = accountFieldsList.get(accountFieldsList.size() - 1);
        assertThat(testAccountFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);

        // Validate the AccountFields in Elasticsearch
        verify(mockAccountFieldsSearchRepository).save(testAccountFields);
    }

    @Test
    @Transactional
    void putNonExistingAccountFields() throws Exception {
        int databaseSizeBeforeUpdate = accountFieldsRepository.findAll().size();
        accountFields.setId(count.incrementAndGet());

        // Create the AccountFields
        AccountFieldsDTO accountFieldsDTO = accountFieldsMapper.toDto(accountFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountFieldsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountFields in the database
        List<AccountFields> accountFieldsList = accountFieldsRepository.findAll();
        assertThat(accountFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountFields in Elasticsearch
        verify(mockAccountFieldsSearchRepository, times(0)).save(accountFields);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountFields() throws Exception {
        int databaseSizeBeforeUpdate = accountFieldsRepository.findAll().size();
        accountFields.setId(count.incrementAndGet());

        // Create the AccountFields
        AccountFieldsDTO accountFieldsDTO = accountFieldsMapper.toDto(accountFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountFields in the database
        List<AccountFields> accountFieldsList = accountFieldsRepository.findAll();
        assertThat(accountFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountFields in Elasticsearch
        verify(mockAccountFieldsSearchRepository, times(0)).save(accountFields);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountFields() throws Exception {
        int databaseSizeBeforeUpdate = accountFieldsRepository.findAll().size();
        accountFields.setId(count.incrementAndGet());

        // Create the AccountFields
        AccountFieldsDTO accountFieldsDTO = accountFieldsMapper.toDto(accountFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountFieldsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountFields in the database
        List<AccountFields> accountFieldsList = accountFieldsRepository.findAll();
        assertThat(accountFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountFields in Elasticsearch
        verify(mockAccountFieldsSearchRepository, times(0)).save(accountFields);
    }

    @Test
    @Transactional
    void partialUpdateAccountFieldsWithPatch() throws Exception {
        // Initialize the database
        accountFieldsRepository.saveAndFlush(accountFields);

        int databaseSizeBeforeUpdate = accountFieldsRepository.findAll().size();

        // Update the accountFields using partial update
        AccountFields partialUpdatedAccountFields = new AccountFields();
        partialUpdatedAccountFields.setId(accountFields.getId());

        restAccountFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountFields))
            )
            .andExpect(status().isOk());

        // Validate the AccountFields in the database
        List<AccountFields> accountFieldsList = accountFieldsRepository.findAll();
        assertThat(accountFieldsList).hasSize(databaseSizeBeforeUpdate);
        AccountFields testAccountFields = accountFieldsList.get(accountFieldsList.size() - 1);
        assertThat(testAccountFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAccountFieldsWithPatch() throws Exception {
        // Initialize the database
        accountFieldsRepository.saveAndFlush(accountFields);

        int databaseSizeBeforeUpdate = accountFieldsRepository.findAll().size();

        // Update the accountFields using partial update
        AccountFields partialUpdatedAccountFields = new AccountFields();
        partialUpdatedAccountFields.setId(accountFields.getId());

        partialUpdatedAccountFields.fieldName(UPDATED_FIELD_NAME);

        restAccountFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountFields))
            )
            .andExpect(status().isOk());

        // Validate the AccountFields in the database
        List<AccountFields> accountFieldsList = accountFieldsRepository.findAll();
        assertThat(accountFieldsList).hasSize(databaseSizeBeforeUpdate);
        AccountFields testAccountFields = accountFieldsList.get(accountFieldsList.size() - 1);
        assertThat(testAccountFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAccountFields() throws Exception {
        int databaseSizeBeforeUpdate = accountFieldsRepository.findAll().size();
        accountFields.setId(count.incrementAndGet());

        // Create the AccountFields
        AccountFieldsDTO accountFieldsDTO = accountFieldsMapper.toDto(accountFields);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountFieldsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountFields in the database
        List<AccountFields> accountFieldsList = accountFieldsRepository.findAll();
        assertThat(accountFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountFields in Elasticsearch
        verify(mockAccountFieldsSearchRepository, times(0)).save(accountFields);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountFields() throws Exception {
        int databaseSizeBeforeUpdate = accountFieldsRepository.findAll().size();
        accountFields.setId(count.incrementAndGet());

        // Create the AccountFields
        AccountFieldsDTO accountFieldsDTO = accountFieldsMapper.toDto(accountFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountFieldsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountFields in the database
        List<AccountFields> accountFieldsList = accountFieldsRepository.findAll();
        assertThat(accountFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountFields in Elasticsearch
        verify(mockAccountFieldsSearchRepository, times(0)).save(accountFields);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountFields() throws Exception {
        int databaseSizeBeforeUpdate = accountFieldsRepository.findAll().size();
        accountFields.setId(count.incrementAndGet());

        // Create the AccountFields
        AccountFieldsDTO accountFieldsDTO = accountFieldsMapper.toDto(accountFields);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountFieldsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountFields in the database
        List<AccountFields> accountFieldsList = accountFieldsRepository.findAll();
        assertThat(accountFieldsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountFields in Elasticsearch
        verify(mockAccountFieldsSearchRepository, times(0)).save(accountFields);
    }

    @Test
    @Transactional
    void deleteAccountFields() throws Exception {
        // Initialize the database
        accountFieldsRepository.saveAndFlush(accountFields);

        int databaseSizeBeforeDelete = accountFieldsRepository.findAll().size();

        // Delete the accountFields
        restAccountFieldsMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountFields.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountFields> accountFieldsList = accountFieldsRepository.findAll();
        assertThat(accountFieldsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AccountFields in Elasticsearch
        verify(mockAccountFieldsSearchRepository, times(1)).deleteById(accountFields.getId());
    }

    @Test
    @Transactional
    void searchAccountFields() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        accountFieldsRepository.saveAndFlush(accountFields);
        when(mockAccountFieldsSearchRepository.search(queryStringQuery("id:" + accountFields.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(accountFields), PageRequest.of(0, 1), 1));

        // Search the accountFields
        restAccountFieldsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accountFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }
}
