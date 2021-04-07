package com.automation.professional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DevicesFieldsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DevicesFieldsDTO.class);
        DevicesFieldsDTO devicesFieldsDTO1 = new DevicesFieldsDTO();
        devicesFieldsDTO1.setId(1L);
        DevicesFieldsDTO devicesFieldsDTO2 = new DevicesFieldsDTO();
        assertThat(devicesFieldsDTO1).isNotEqualTo(devicesFieldsDTO2);
        devicesFieldsDTO2.setId(devicesFieldsDTO1.getId());
        assertThat(devicesFieldsDTO1).isEqualTo(devicesFieldsDTO2);
        devicesFieldsDTO2.setId(2L);
        assertThat(devicesFieldsDTO1).isNotEqualTo(devicesFieldsDTO2);
        devicesFieldsDTO1.setId(null);
        assertThat(devicesFieldsDTO1).isNotEqualTo(devicesFieldsDTO2);
    }
}
