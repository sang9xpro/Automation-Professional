package com.automation.professional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.automation.professional.IntegrationTest;
import com.automation.professional.domain.Devices;
import com.automation.professional.domain.enumeration.DeviceStatus;
import com.automation.professional.domain.enumeration.DeviceType;
import com.automation.professional.repository.DevicesRepository;
import com.automation.professional.repository.search.DevicesSearchRepository;
import com.automation.professional.service.DevicesService;
import com.automation.professional.service.dto.DevicesDTO;
import com.automation.professional.service.mapper.DevicesMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
 * Integration tests for the {@link DevicesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DevicesResourceIT {

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_MAC_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_MAC_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_OS = "AAAAAAAAAA";
    private static final String UPDATED_OS = "BBBBBBBBBB";

    private static final DeviceType DEFAULT_DEVICE_TYPE = DeviceType.MOBILE;
    private static final DeviceType UPDATED_DEVICE_TYPE = DeviceType.COMPUTER;

    private static final DeviceStatus DEFAULT_STATUS = DeviceStatus.Online;
    private static final DeviceStatus UPDATED_STATUS = DeviceStatus.Offline;

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/devices";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DevicesRepository devicesRepository;

    @Mock
    private DevicesRepository devicesRepositoryMock;

    @Autowired
    private DevicesMapper devicesMapper;

    @Mock
    private DevicesService devicesServiceMock;

    /**
     * This repository is mocked in the com.automation.professional.repository.search test package.
     *
     * @see com.automation.professional.repository.search.DevicesSearchRepositoryMockConfiguration
     */
    @Autowired
    private DevicesSearchRepository mockDevicesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDevicesMockMvc;

    private Devices devices;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Devices createEntity(EntityManager em) {
        Devices devices = new Devices()
            .ipAddress(DEFAULT_IP_ADDRESS)
            .macAddress(DEFAULT_MAC_ADDRESS)
            .os(DEFAULT_OS)
            .deviceType(DEFAULT_DEVICE_TYPE)
            .status(DEFAULT_STATUS)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .owner(DEFAULT_OWNER);
        return devices;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Devices createUpdatedEntity(EntityManager em) {
        Devices devices = new Devices()
            .ipAddress(UPDATED_IP_ADDRESS)
            .macAddress(UPDATED_MAC_ADDRESS)
            .os(UPDATED_OS)
            .deviceType(UPDATED_DEVICE_TYPE)
            .status(UPDATED_STATUS)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .owner(UPDATED_OWNER);
        return devices;
    }

    @BeforeEach
    public void initTest() {
        devices = createEntity(em);
    }

    @Test
    @Transactional
    void createDevices() throws Exception {
        int databaseSizeBeforeCreate = devicesRepository.findAll().size();
        // Create the Devices
        DevicesDTO devicesDTO = devicesMapper.toDto(devices);
        restDevicesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(devicesDTO)))
            .andExpect(status().isCreated());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeCreate + 1);
        Devices testDevices = devicesList.get(devicesList.size() - 1);
        assertThat(testDevices.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testDevices.getMacAddress()).isEqualTo(DEFAULT_MAC_ADDRESS);
        assertThat(testDevices.getOs()).isEqualTo(DEFAULT_OS);
        assertThat(testDevices.getDeviceType()).isEqualTo(DEFAULT_DEVICE_TYPE);
        assertThat(testDevices.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDevices.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testDevices.getOwner()).isEqualTo(DEFAULT_OWNER);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(1)).save(testDevices);
    }

    @Test
    @Transactional
    void createDevicesWithExistingId() throws Exception {
        // Create the Devices with an existing ID
        devices.setId(1L);
        DevicesDTO devicesDTO = devicesMapper.toDto(devices);

        int databaseSizeBeforeCreate = devicesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDevicesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(devicesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeCreate);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(0)).save(devices);
    }

    @Test
    @Transactional
    void getAllDevices() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get all the devicesList
        restDevicesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(devices.getId().intValue())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].macAddress").value(hasItem(DEFAULT_MAC_ADDRESS)))
            .andExpect(jsonPath("$.[*].os").value(hasItem(DEFAULT_OS)))
            .andExpect(jsonPath("$.[*].deviceType").value(hasItem(DEFAULT_DEVICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDevicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(devicesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDevicesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(devicesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDevicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(devicesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDevicesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(devicesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDevices() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        // Get the devices
        restDevicesMockMvc
            .perform(get(ENTITY_API_URL_ID, devices.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(devices.getId().intValue()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.macAddress").value(DEFAULT_MAC_ADDRESS))
            .andExpect(jsonPath("$.os").value(DEFAULT_OS))
            .andExpect(jsonPath("$.deviceType").value(DEFAULT_DEVICE_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER));
    }

    @Test
    @Transactional
    void getNonExistingDevices() throws Exception {
        // Get the devices
        restDevicesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDevices() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        int databaseSizeBeforeUpdate = devicesRepository.findAll().size();

        // Update the devices
        Devices updatedDevices = devicesRepository.findById(devices.getId()).get();
        // Disconnect from session so that the updates on updatedDevices are not directly saved in db
        em.detach(updatedDevices);
        updatedDevices
            .ipAddress(UPDATED_IP_ADDRESS)
            .macAddress(UPDATED_MAC_ADDRESS)
            .os(UPDATED_OS)
            .deviceType(UPDATED_DEVICE_TYPE)
            .status(UPDATED_STATUS)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .owner(UPDATED_OWNER);
        DevicesDTO devicesDTO = devicesMapper.toDto(updatedDevices);

        restDevicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, devicesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(devicesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate);
        Devices testDevices = devicesList.get(devicesList.size() - 1);
        assertThat(testDevices.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testDevices.getMacAddress()).isEqualTo(UPDATED_MAC_ADDRESS);
        assertThat(testDevices.getOs()).isEqualTo(UPDATED_OS);
        assertThat(testDevices.getDeviceType()).isEqualTo(UPDATED_DEVICE_TYPE);
        assertThat(testDevices.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDevices.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testDevices.getOwner()).isEqualTo(UPDATED_OWNER);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository).save(testDevices);
    }

    @Test
    @Transactional
    void putNonExistingDevices() throws Exception {
        int databaseSizeBeforeUpdate = devicesRepository.findAll().size();
        devices.setId(count.incrementAndGet());

        // Create the Devices
        DevicesDTO devicesDTO = devicesMapper.toDto(devices);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDevicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, devicesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(devicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(0)).save(devices);
    }

    @Test
    @Transactional
    void putWithIdMismatchDevices() throws Exception {
        int databaseSizeBeforeUpdate = devicesRepository.findAll().size();
        devices.setId(count.incrementAndGet());

        // Create the Devices
        DevicesDTO devicesDTO = devicesMapper.toDto(devices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDevicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(devicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(0)).save(devices);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDevices() throws Exception {
        int databaseSizeBeforeUpdate = devicesRepository.findAll().size();
        devices.setId(count.incrementAndGet());

        // Create the Devices
        DevicesDTO devicesDTO = devicesMapper.toDto(devices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDevicesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(devicesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(0)).save(devices);
    }

    @Test
    @Transactional
    void partialUpdateDevicesWithPatch() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        int databaseSizeBeforeUpdate = devicesRepository.findAll().size();

        // Update the devices using partial update
        Devices partialUpdatedDevices = new Devices();
        partialUpdatedDevices.setId(devices.getId());

        partialUpdatedDevices.macAddress(UPDATED_MAC_ADDRESS).deviceType(UPDATED_DEVICE_TYPE).lastUpdate(UPDATED_LAST_UPDATE);

        restDevicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevices.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevices))
            )
            .andExpect(status().isOk());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate);
        Devices testDevices = devicesList.get(devicesList.size() - 1);
        assertThat(testDevices.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testDevices.getMacAddress()).isEqualTo(UPDATED_MAC_ADDRESS);
        assertThat(testDevices.getOs()).isEqualTo(DEFAULT_OS);
        assertThat(testDevices.getDeviceType()).isEqualTo(UPDATED_DEVICE_TYPE);
        assertThat(testDevices.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDevices.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testDevices.getOwner()).isEqualTo(DEFAULT_OWNER);
    }

    @Test
    @Transactional
    void fullUpdateDevicesWithPatch() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        int databaseSizeBeforeUpdate = devicesRepository.findAll().size();

        // Update the devices using partial update
        Devices partialUpdatedDevices = new Devices();
        partialUpdatedDevices.setId(devices.getId());

        partialUpdatedDevices
            .ipAddress(UPDATED_IP_ADDRESS)
            .macAddress(UPDATED_MAC_ADDRESS)
            .os(UPDATED_OS)
            .deviceType(UPDATED_DEVICE_TYPE)
            .status(UPDATED_STATUS)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .owner(UPDATED_OWNER);

        restDevicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevices.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevices))
            )
            .andExpect(status().isOk());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate);
        Devices testDevices = devicesList.get(devicesList.size() - 1);
        assertThat(testDevices.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testDevices.getMacAddress()).isEqualTo(UPDATED_MAC_ADDRESS);
        assertThat(testDevices.getOs()).isEqualTo(UPDATED_OS);
        assertThat(testDevices.getDeviceType()).isEqualTo(UPDATED_DEVICE_TYPE);
        assertThat(testDevices.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDevices.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testDevices.getOwner()).isEqualTo(UPDATED_OWNER);
    }

    @Test
    @Transactional
    void patchNonExistingDevices() throws Exception {
        int databaseSizeBeforeUpdate = devicesRepository.findAll().size();
        devices.setId(count.incrementAndGet());

        // Create the Devices
        DevicesDTO devicesDTO = devicesMapper.toDto(devices);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDevicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, devicesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(devicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(0)).save(devices);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDevices() throws Exception {
        int databaseSizeBeforeUpdate = devicesRepository.findAll().size();
        devices.setId(count.incrementAndGet());

        // Create the Devices
        DevicesDTO devicesDTO = devicesMapper.toDto(devices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDevicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(devicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(0)).save(devices);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDevices() throws Exception {
        int databaseSizeBeforeUpdate = devicesRepository.findAll().size();
        devices.setId(count.incrementAndGet());

        // Create the Devices
        DevicesDTO devicesDTO = devicesMapper.toDto(devices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDevicesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(devicesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Devices in the database
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(0)).save(devices);
    }

    @Test
    @Transactional
    void deleteDevices() throws Exception {
        // Initialize the database
        devicesRepository.saveAndFlush(devices);

        int databaseSizeBeforeDelete = devicesRepository.findAll().size();

        // Delete the devices
        restDevicesMockMvc
            .perform(delete(ENTITY_API_URL_ID, devices.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Devices> devicesList = devicesRepository.findAll();
        assertThat(devicesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Devices in Elasticsearch
        verify(mockDevicesSearchRepository, times(1)).deleteById(devices.getId());
    }

    @Test
    @Transactional
    void searchDevices() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        devicesRepository.saveAndFlush(devices);
        when(mockDevicesSearchRepository.search(queryStringQuery("id:" + devices.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(devices), PageRequest.of(0, 1), 1));

        // Search the devices
        restDevicesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + devices.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(devices.getId().intValue())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].macAddress").value(hasItem(DEFAULT_MAC_ADDRESS)))
            .andExpect(jsonPath("$.[*].os").value(hasItem(DEFAULT_OS)))
            .andExpect(jsonPath("$.[*].deviceType").value(hasItem(DEFAULT_DEVICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)));
    }
}
