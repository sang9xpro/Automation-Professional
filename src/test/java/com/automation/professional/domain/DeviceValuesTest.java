package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeviceValuesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceValues.class);
        DeviceValues deviceValues1 = new DeviceValues();
        deviceValues1.setId(1L);
        DeviceValues deviceValues2 = new DeviceValues();
        deviceValues2.setId(deviceValues1.getId());
        assertThat(deviceValues1).isEqualTo(deviceValues2);
        deviceValues2.setId(2L);
        assertThat(deviceValues1).isNotEqualTo(deviceValues2);
        deviceValues1.setId(null);
        assertThat(deviceValues1).isNotEqualTo(deviceValues2);
    }
}
