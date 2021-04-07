package com.automation.professional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A History.
 */
@Entity
@Table(name = "history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "history")
public class History implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "device_id")
    private Integer deviceId;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @OneToMany(mappedBy = "history")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "history", "historyFields" }, allowSetters = true)
    private Set<HistoryValues> historyValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public History id(Long id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return this.url;
    }

    public History url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTaskId() {
        return this.taskId;
    }

    public History taskId(Integer taskId) {
        this.taskId = taskId;
        return this;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getDeviceId() {
        return this.deviceId;
    }

    public History deviceId(Integer deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getAccountId() {
        return this.accountId;
    }

    public History accountId(Integer accountId) {
        this.accountId = accountId;
        return this;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public History lastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<HistoryValues> getHistoryValues() {
        return this.historyValues;
    }

    public History historyValues(Set<HistoryValues> historyValues) {
        this.setHistoryValues(historyValues);
        return this;
    }

    public History addHistoryValues(HistoryValues historyValues) {
        this.historyValues.add(historyValues);
        historyValues.setHistory(this);
        return this;
    }

    public History removeHistoryValues(HistoryValues historyValues) {
        this.historyValues.remove(historyValues);
        historyValues.setHistory(null);
        return this;
    }

    public void setHistoryValues(Set<HistoryValues> historyValues) {
        if (this.historyValues != null) {
            this.historyValues.forEach(i -> i.setHistory(null));
        }
        if (historyValues != null) {
            historyValues.forEach(i -> i.setHistory(this));
        }
        this.historyValues = historyValues;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof History)) {
            return false;
        }
        return id != null && id.equals(((History) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "History{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", taskId=" + getTaskId() +
            ", deviceId=" + getDeviceId() +
            ", accountId=" + getAccountId() +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
    }
}
