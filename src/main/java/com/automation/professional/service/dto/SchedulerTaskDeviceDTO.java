package com.automation.professional.service.dto;

import com.automation.professional.domain.enumeration.SchedulerStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.SchedulerTaskDevice} entity.
 */
public class SchedulerTaskDeviceDTO implements Serializable {

    private Long id;

    private Instant startTime;

    private Instant endTime;

    private Integer countFrom;

    private Integer countTo;

    private Double point;

    private Instant lastUpdate;

    private String owner;

    private SchedulerStatus status;

    private SchedulersDTO schedulers;

    private TasksDTO tasks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Integer getCountFrom() {
        return countFrom;
    }

    public void setCountFrom(Integer countFrom) {
        this.countFrom = countFrom;
    }

    public Integer getCountTo() {
        return countTo;
    }

    public void setCountTo(Integer countTo) {
        this.countTo = countTo;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
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

    public SchedulersDTO getSchedulers() {
        return schedulers;
    }

    public void setSchedulers(SchedulersDTO schedulers) {
        this.schedulers = schedulers;
    }

    public TasksDTO getTasks() {
        return tasks;
    }

    public void setTasks(TasksDTO tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulerTaskDeviceDTO)) {
            return false;
        }

        SchedulerTaskDeviceDTO schedulerTaskDeviceDTO = (SchedulerTaskDeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schedulerTaskDeviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulerTaskDeviceDTO{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", countFrom=" + getCountFrom() +
            ", countTo=" + getCountTo() +
            ", point=" + getPoint() +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", owner='" + getOwner() + "'" +
            ", status='" + getStatus() + "'" +
            ", schedulers=" + getSchedulers() +
            ", tasks=" + getTasks() +
            "}";
    }
}
