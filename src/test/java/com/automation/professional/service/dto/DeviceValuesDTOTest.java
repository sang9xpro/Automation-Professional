package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeviceValuesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceValuesDTO.class);
        DeviceValuesDTO deviceValuesDTO1 = new DeviceValuesDTO();
        deviceValuesDTO1.setId(1L);
        DeviceValuesDTO deviceValuesDTO2 = new DeviceValuesDTO();
        assertThat(deviceValuesDTO1).isNotEqualTo(deviceValuesDTO2);
        deviceValuesDTO2.setId(deviceValuesDTO1.getId());
        assertThat(deviceValuesDTO1).isEqualTo(deviceValuesDTO2);
        deviceValuesDTO2.setId(2L);
        assertThat(deviceValuesDTO1).isNotEqualTo(deviceValuesDTO2);
        deviceValuesDTO1.setId(null);
        assertThat(deviceValuesDTO1).isNotEqualTo(deviceValuesDTO2);
    }
}
