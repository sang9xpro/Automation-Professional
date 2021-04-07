package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DevicesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DevicesDTO.class);
        DevicesDTO devicesDTO1 = new DevicesDTO();
        devicesDTO1.setId(1L);
        DevicesDTO devicesDTO2 = new DevicesDTO();
        assertThat(devicesDTO1).isNotEqualTo(devicesDTO2);
        devicesDTO2.setId(devicesDTO1.getId());
        assertThat(devicesDTO1).isEqualTo(devicesDTO2);
        devicesDTO2.setId(2L);
        assertThat(devicesDTO1).isNotEqualTo(devicesDTO2);
        devicesDTO1.setId(null);
        assertThat(devicesDTO1).isNotEqualTo(devicesDTO2);
    }
}
