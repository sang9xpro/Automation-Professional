package com.automation.professional.service.dto;

import com.automation.professional.domain.enumeration.SchedulerStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.Schedulers} entity.
 */
public class SchedulersDTO implements Serializable {

    private Long id;

    private String url;

    private String title;

    private String otherSource;

    private Integer count;

    private Instant lastUpdate;

    private String owner;

    private SchedulerStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOtherSource() {
        return otherSource;
    }

    public void setOtherSource(String otherSource) {
        this.otherSource = otherSource;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public SchedulerStatus getStatus() {
        return status;
    }

    public void setStatus(SchedulerStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulersDTO)) {
            return false;
        }

        SchedulersDTO schedulersDTO = (SchedulersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schedulersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulersDTO{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", title='" + getTitle() + "'" +
            ", otherSource='" + getOtherSource() + "'" +
            ", count=" + getCount() +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", owner='" + getOwner() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
