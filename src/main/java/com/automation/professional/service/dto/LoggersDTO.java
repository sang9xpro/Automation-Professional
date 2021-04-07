package com.automation.professional.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.automation.professional.domain.Loggers} entity.
 */
public class LoggersDTO implements Serializable {

    private Long id;

    private String status;

    @Lob
    private byte[] logDetail;

    private String logDetailContentType;
    private Instant lastUpdate;

    private DevicesDTO devices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(byte[] logDetail) {
        this.logDetail = logDetail;
    }

    public String getLogDetailContentType() {
        return logDetailContentType;
    }

    public void setLogDetailContentType(String logDetailContentType) {
        this.logDetailContentType = logDetailContentType;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public DevicesDTO getDevices() {
        return devices;
    }

    public void setDevices(DevicesDTO devices) {
        this.devices = devices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoggersDTO)) {
            return false;
        }

        LoggersDTO loggersDTO = (LoggersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loggersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoggersDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", logDetail='" + getLogDetail() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", devices=" + getDevices() +
            "}";
    }
}
