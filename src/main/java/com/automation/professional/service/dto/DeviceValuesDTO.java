package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.DeviceValues} entity.
 */
public class DeviceValuesDTO implements Serializable {

    private Long id;

    private String value;

    private DevicesDTO devices;

    private DevicesFieldsDTO devicesFields;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DevicesDTO getDevices() {
        return devices;
    }

    public void setDevices(DevicesDTO devices) {
        this.devices = devices;
    }

    public DevicesFieldsDTO getDevicesFields() {
        return devicesFields;
    }

    public void setDevicesFields(DevicesFieldsDTO devicesFields) {
        this.devicesFields = devicesFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceValuesDTO)) {
            return false;
        }

        DeviceValuesDTO deviceValuesDTO = (DeviceValuesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceValuesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceValuesDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", devices=" + getDevices() +
            ", devicesFields=" + getDevicesFields() +
            "}";
    }
}
