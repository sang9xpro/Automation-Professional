package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.SchedulerFields} entity.
 */
public class SchedulerFieldsDTO implements Serializable {

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
        if (!(o instanceof SchedulerFieldsDTO)) {
            return false;
        }

        SchedulerFieldsDTO schedulerFieldsDTO = (SchedulerFieldsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schedulerFieldsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulerFieldsDTO{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            "}";
    }
}
