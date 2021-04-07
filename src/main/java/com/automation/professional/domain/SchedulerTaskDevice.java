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
 * A SchedulerTaskDevice.
 */
@Entity
@Table(name = "scheduler_task_device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "schedulertaskdevice")
public class SchedulerTaskDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "count_from")
    private Integer countFrom;

    @Column(name = "count_to")
    private Integer countTo;

    @Column(name = "point")
    private Double point;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @Column(name = "owner")
    private String owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SchedulerStatus status;

    @OneToMany(mappedBy = "schedulerTaskDevice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schedulerTaskDevice", "schedulerTaskDeviceFields" }, allowSetters = true)
    private Set<SchedulerTaskDeviceValues> schedulerTaskDeviceValues = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "schedulerValues", "schedulerTaskDevices" }, allowSetters = true)
    private Schedulers schedulers;

    @ManyToOne
    @JsonIgnoreProperties(value = { "taskValues", "schedulerTaskDevices" }, allowSetters = true)
    private Tasks tasks;

    @ManyToMany(mappedBy = "schedulerTaskDevices")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "country", "deviceValues", "loggers", "accounts", "schedulerTaskDevices" }, allowSetters = true)
    private Set<Devices> devices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchedulerTaskDevice id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public SchedulerTaskDevice startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public SchedulerTaskDevice endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Integer getCountFrom() {
        return this.countFrom;
    }

    public SchedulerTaskDevice countFrom(Integer countFrom) {
        this.countFrom = countFrom;
        return this;
    }

    public void setCountFrom(Integer countFrom) {
        this.countFrom = countFrom;
    }

    public Integer getCountTo() {
        return this.countTo;
    }

    public SchedulerTaskDevice countTo(Integer countTo) {
        this.countTo = countTo;
        return this;
    }

    public void setCountTo(Integer countTo) {
        this.countTo = countTo;
    }

    public Double getPoint() {
        return this.point;
    }

    public SchedulerTaskDevice point(Double point) {
        this.point = point;
        return this;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public SchedulerTaskDevice lastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getOwner() {
        return this.owner;
    }

    public SchedulerTaskDevice owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public SchedulerStatus getStatus() {
        return this.status;
    }

    public SchedulerTaskDevice status(SchedulerStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(SchedulerStatus status) {
        this.status = status;
    }

    public Set<SchedulerTaskDeviceValues> getSchedulerTaskDeviceValues() {
        return this.schedulerTaskDeviceValues;
    }

    public SchedulerTaskDevice schedulerTaskDeviceValues(Set<SchedulerTaskDeviceValues> schedulerTaskDeviceValues) {
        this.setSchedulerTaskDeviceValues(schedulerTaskDeviceValues);
        return this;
    }

    public SchedulerTaskDevice addSchedulerTaskDeviceValues(SchedulerTaskDeviceValues schedulerTaskDeviceValues) {
        this.schedulerTaskDeviceValues.add(schedulerTaskDeviceValues);
        schedulerTaskDeviceValues.setSchedulerTaskDevice(this);
        return this;
    }

    public SchedulerTaskDevice removeSchedulerTaskDeviceValues(SchedulerTaskDeviceValues schedulerTaskDeviceValues) {
        this.schedulerTaskDeviceValues.remove(schedulerTaskDeviceValues);
        schedulerTaskDeviceValues.setSchedulerTaskDevice(null);
        return this;
    }

    public void setSchedulerTaskDeviceValues(Set<SchedulerTaskDeviceValues> schedulerTaskDeviceValues) {
        if (this.schedulerTaskDeviceValues != null) {
            this.schedulerTaskDeviceValues.forEach(i -> i.setSchedulerTaskDevice(null));
        }
        if (schedulerTaskDeviceValues != null) {
            schedulerTaskDeviceValues.forEach(i -> i.setSchedulerTaskDevice(this));
        }
        this.schedulerTaskDeviceValues = schedulerTaskDeviceValues;
    }

    public Schedulers getSchedulers() {
        return this.schedulers;
    }

    public SchedulerTaskDevice schedulers(Schedulers schedulers) {
        this.setSchedulers(schedulers);
        return this;
    }

    public void setSchedulers(Schedulers schedulers) {
        this.schedulers = schedulers;
    }

    public Tasks getTasks() {
        return this.tasks;
    }

    public SchedulerTaskDevice tasks(Tasks tasks) {
        this.setTasks(tasks);
        return this;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    public Set<Devices> getDevices() {
        return this.devices;
    }

    public SchedulerTaskDevice devices(Set<Devices> devices) {
        this.setDevices(devices);
        return this;
    }

    public SchedulerTaskDevice addDevices(Devices devices) {
        this.devices.add(devices);
        devices.getSchedulerTaskDevices().add(this);
        return this;
    }

    public SchedulerTaskDevice removeDevices(Devices devices) {
        this.devices.remove(devices);
        devices.getSchedulerTaskDevices().remove(this);
        return this;
    }

    public void setDevices(Set<Devices> devices) {
        if (this.devices != null) {
            this.devices.forEach(i -> i.removeSchedulerTaskDevice(this));
        }
        if (devices != null) {
            devices.forEach(i -> i.addSchedulerTaskDevice(this));
        }
        this.devices = devices;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulerTaskDevice)) {
            return false;
        }
        return id != null && id.equals(((SchedulerTaskDevice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulerTaskDevice{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", countFrom=" + getCountFrom() +
            ", countTo=" + getCountTo() +
            ", point=" + getPoint() +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", owner='" + getOwner() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
