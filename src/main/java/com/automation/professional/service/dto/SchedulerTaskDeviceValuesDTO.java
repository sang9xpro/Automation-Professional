package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.SchedulerTaskDeviceValues} entity.
 */
public class SchedulerTaskDeviceValuesDTO implements Serializable {

    private Long id;

    private String value;

    private SchedulerTaskDeviceDTO schedulerTaskDevice;

    private SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFields;

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

    public SchedulerTaskDeviceDTO getSchedulerTaskDevice() {
        return schedulerTaskDevice;
    }

    public void setSchedulerTaskDevice(SchedulerTaskDeviceDTO schedulerTaskDevice) {
        this.schedulerTaskDevice = schedulerTaskDevice;
    }

    public SchedulerTaskDeviceFieldsDTO getSchedulerTaskDeviceFields() {
        return schedulerTaskDeviceFields;
    }

    public void setSchedulerTaskDeviceFields(SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFields) {
        this.schedulerTaskDeviceFields = schedulerTaskDeviceFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulerTaskDeviceValuesDTO)) {
            return false;
        }

        SchedulerTaskDeviceValuesDTO schedulerTaskDeviceValuesDTO = (SchedulerTaskDeviceValuesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schedulerTaskDeviceValuesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulerTaskDeviceValuesDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", schedulerTaskDevice=" + getSchedulerTaskDevice() +
            ", schedulerTaskDeviceFields=" + getSchedulerTaskDeviceFields() +
            "}";
    }
}
