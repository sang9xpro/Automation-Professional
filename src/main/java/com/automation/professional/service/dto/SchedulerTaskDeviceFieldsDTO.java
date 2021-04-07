package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.SchedulerTaskDeviceFields} entity.
 */
public class SchedulerTaskDeviceFieldsDTO implements Serializable {

    private Long id;

    private String fieldName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulerTaskDeviceFieldsDTO)) {
            return false;
        }

        SchedulerTaskDeviceFieldsDTO schedulerTaskDeviceFieldsDTO = (SchedulerTaskDeviceFieldsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schedulerTaskDeviceFieldsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulerTaskDeviceFieldsDTO{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            "}";
    }
}
