package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.MostOfContValues} entity.
 */
public class MostOfContValuesDTO implements Serializable {

    private Long id;

    private String value;

    private MostOfContentDTO mostOfContent;

    private MostOfContFieldsDTO mostOfContFields;

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

    public MostOfContentDTO getMostOfContent() {
        return mostOfContent;
    }

    public void setMostOfContent(MostOfContentDTO mostOfContent) {
        this.mostOfContent = mostOfContent;
    }

    public MostOfContFieldsDTO getMostOfContFields() {
        return mostOfContFields;
    }

    public void setMostOfContFields(MostOfContFieldsDTO mostOfContFields) {
        this.mostOfContFields = mostOfContFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MostOfContValuesDTO)) {
            return false;
        }

        MostOfContValuesDTO mostOfContValuesDTO = (MostOfContValuesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mostOfContValuesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MostOfContValuesDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", mostOfContent=" + getMostOfContent() +
            ", mostOfContFields=" + getMostOfContFields() +
            "}";
    }
}
