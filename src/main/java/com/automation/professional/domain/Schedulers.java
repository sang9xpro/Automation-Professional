package com.automation.professional.domain;

import com.automation.professional.domain.enumeration.SchedulerStatus;
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
 * A Schedulers.
 */
@Entity
@Table(name = "schedulers")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "schedulers")
public class Schedulers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @Column(name = "other_source")
    private String otherSource;

    @Column(name = "count")
    private Integer count;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @Column(name = "owner")
    private String owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SchedulerStatus status;

    @OneToMany(mappedBy = "schedulers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schedulers", "schedulerFields" }, allowSetters = true)
    private Set<SchedulerValue> schedulerValues = new HashSet<>();

    @OneToMany(mappedBy = "schedulers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schedulerTaskDeviceValues", "schedulers", "tasks", "devices" }, allowSetters = true)
    private Set<SchedulerTaskDevice> schedulerTaskDevices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Schedulers id(Long id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return this.url;
    }

    public Schedulers url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public Schedulers title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOtherSource() {
        return this.otherSource;
    }

    public Schedulers otherSource(String otherSource) {
        this.otherSource = otherSource;
        return this;
    }

    public void setOtherSource(String otherSource) {
        this.otherSource = otherSource;
    }

    public Integer getCount() {
        return this.count;
    }

    public Schedulers count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public Schedulers lastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getOwner() {
        return this.owner;
    }

    public Schedulers owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public SchedulerStatus getStatus() {
        return this.status;
    }

    public Schedulers status(SchedulerStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(SchedulerStatus status) {
        this.status = status;
    }

    public Set<SchedulerValue> getSchedulerValues() {
        return this.schedulerValues;
    }

    public Schedulers schedulerValues(Set<SchedulerValue> schedulerValues) {
        this.setSchedulerValues(schedulerValues);
        return this;
    }

    public Schedulers addSchedulerValue(SchedulerValue schedulerValue) {
        this.schedulerValues.add(schedulerValue);
        schedulerValue.setSchedulers(this);
        return this;
    }

    public Schedulers removeSchedulerValue(SchedulerValue schedulerValue) {
        this.schedulerValues.remove(schedulerValue);
        schedulerValue.setSchedulers(null);
        return this;
    }

    public void setSchedulerValues(Set<SchedulerValue> schedulerValues) {
        if (this.schedulerValues != null) {
            this.schedulerValues.forEach(i -> i.setSchedulers(null));
        }
        if (schedulerValues != null) {
            schedulerValues.forEach(i -> i.setSchedulers(this));
        }
        this.schedulerValues = schedulerValues;
    }

    public Set<SchedulerTaskDevice> getSchedulerTaskDevices() {
        return this.schedulerTaskDevices;
    }

    public Schedulers schedulerTaskDevices(Set<SchedulerTaskDevice> schedulerTaskDevices) {
        this.setSchedulerTaskDevices(schedulerTaskDevices);
        return this;
    }

    public Schedulers addSchedulerTaskDevice(SchedulerTaskDevice schedulerTaskDevice) {
        this.schedulerTaskDevices.add(schedulerTaskDevice);
        schedulerTaskDevice.setSchedulers(this);
        return this;
    }

    public Schedulers removeSchedulerTaskDevice(SchedulerTaskDevice schedulerTaskDevice) {
        this.schedulerTaskDevices.remove(schedulerTaskDevice);
        schedulerTaskDevice.setSchedulers(null);
        return this;
    }

    public void setSchedulerTaskDevices(Set<SchedulerTaskDevice> schedulerTaskDevices) {
        if (this.schedulerTaskDevices != null) {
            this.schedulerTaskDevices.forEach(i -> i.setSchedulers(null));
        }
        if (schedulerTaskDevices != null) {
            schedulerTaskDevices.forEach(i -> i.setSchedulers(this));
        }
        this.schedulerTaskDevices = schedulerTaskDevices;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Schedulers)) {
            return false;
        }
        return id != null && id.equals(((Schedulers) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Schedulers{" +
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
