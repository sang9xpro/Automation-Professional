package com.automation.professional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.professional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DevicesFieldsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DevicesFields.class);
        DevicesFields devicesFields1 = new DevicesFields();
        devicesFields1.setId(1L);
        DevicesFields devicesFields2 = new DevicesFields();
        devicesFields2.setId(devicesFields1.getId());
        assertThat(devicesFields1).isEqualTo(devicesFields2);
        devicesFields2.setId(2L);
        assertThat(devicesFields1).isNotEqualTo(devicesFields2);
        devicesFields1.setId(null);
        assertThat(devicesFields1).isNotEqualTo(devicesFields2);
    }
}
