package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.LoggersValues} entity.
 */
public class LoggersValuesDTO implements Serializable {

    private Long id;

    private String value;

    private LoggersDTO loggers;

    private LoggersFieldsDTO loggersFields;

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

    public LoggersDTO getLoggers() {
        return loggers;
    }

    public void setLoggers(LoggersDTO loggers) {
        this.loggers = loggers;
    }

    public LoggersFieldsDTO getLoggersFields() {
        return loggersFields;
    }

    public void setLoggersFields(LoggersFieldsDTO loggersFields) {
        this.loggersFields = loggersFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoggersValuesDTO)) {
            return false;
        }

        LoggersValuesDTO loggersValuesDTO = (LoggersValuesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loggersValuesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoggersValuesDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", loggers=" + getLoggers() +
            ", loggersFields=" + getLoggersFields() +
            "}";
    }
}
