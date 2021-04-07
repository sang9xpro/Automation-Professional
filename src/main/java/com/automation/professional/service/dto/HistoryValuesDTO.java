package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.HistoryValues} entity.
 */
public class HistoryValuesDTO implements Serializable {

    private Long id;

    private String value;

    private HistoryDTO history;

    private HistoryFieldsDTO historyFields;

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

    public HistoryDTO getHistory() {
        return history;
    }

    public void setHistory(HistoryDTO history) {
        this.history = history;
    }

    public HistoryFieldsDTO getHistoryFields() {
        return historyFields;
    }

    public void setHistoryFields(HistoryFieldsDTO historyFields) {
        this.historyFields = historyFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoryValuesDTO)) {
            return false;
        }

        HistoryValuesDTO historyValuesDTO = (HistoryValuesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historyValuesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoryValuesDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", history=" + getHistory() +
            ", historyFields=" + getHistoryFields() +
            "}";
    }
}
