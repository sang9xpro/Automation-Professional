package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.DevicesFields} entity.
 */
public class DevicesFieldsDTO implements Serializable {

    private Long id;

    private String fieldsName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldsName() {
        return fieldsName;
    }

    public void setFieldsName(String fieldsName) {
        this.fieldsName = fieldsName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DevicesFieldsDTO)) {
            return false;
        }

        DevicesFieldsDTO devicesFieldsDTO = (DevicesFieldsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, devicesFieldsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DevicesFieldsDTO{" +
            "id=" + getId() +
            ", fieldsName='" + getFieldsName() + "'" +
            "}";
    }
}
