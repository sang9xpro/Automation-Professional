package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.SchedulerValue} entity.
 */
public class SchedulerValueDTO implements Serializable {

    private Long id;

    private String value;

    private SchedulersDTO schedulers;

    private SchedulerFieldsDTO schedulerFields;

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

    public SchedulersDTO getSchedulers() {
        return schedulers;
    }

    public void setSchedulers(SchedulersDTO schedulers) {
        this.schedulers = schedulers;
    }

    public SchedulerFieldsDTO getSchedulerFields() {
        return schedulerFields;
    }

    public void setSchedulerFields(SchedulerFieldsDTO schedulerFields) {
        this.schedulerFields = schedulerFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulerValueDTO)) {
            return false;
        }

        SchedulerValueDTO schedulerValueDTO = (SchedulerValueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schedulerValueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulerValueDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", schedulers=" + getSchedulers() +
            ", schedulerFields=" + getSchedulerFields() +
            "}";
    }
}
