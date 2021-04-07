package com.automation.professional.domain;

import com.automation.professional.domain.enumeration.Social;
import com.automation.professional.domain.enumeration.TaskType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Tasks.
 */
@Entity
@Table(name = "tasks")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tasks")
public class Tasks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "description")
    private String description;

    @Column(name = "source")
    private String source;

    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "social")
    private Social social;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TaskType type;

    @OneToMany(mappedBy = "tasks")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tasks", "taskFields" }, allowSetters = true)
    private Set<TaskValues> taskValues = new HashSet<>();

    @OneToMany(mappedBy = "tasks")
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

    public Tasks id(Long id) {
        this.id = id;
        return this;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public Tasks taskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return this.description;
    }

    public Tasks description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return this.source;
    }

    public Tasks source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getPrice() {
        return this.price;
    }

    public Tasks price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Social getSocial() {
        return this.social;
    }

    public Tasks social(Social social) {
        this.social = social;
        return this;
    }

    public void setSocial(Social social) {
        this.social = social;
    }

    public TaskType getType() {
        return this.type;
    }

    public Tasks type(TaskType type) {
        this.type = type;
        return this;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Set<TaskValues> getTaskValues() {
        return this.taskValues;
    }

    public Tasks taskValues(Set<TaskValues> taskValues) {
        this.setTaskValues(taskValues);
        return this;
    }

    public Tasks addTaskValues(TaskValues taskValues) {
        this.taskValues.add(taskValues);
        taskValues.setTasks(this);
        return this;
    }

    public Tasks removeTaskValues(TaskValues taskValues) {
        this.taskValues.remove(taskValues);
        taskValues.setTasks(null);
        return this;
    }

    public void setTaskValues(Set<TaskValues> taskValues) {
        if (this.taskValues != null) {
            this.taskValues.forEach(i -> i.setTasks(null));
        }
        if (taskValues != null) {
            taskValues.forEach(i -> i.setTasks(this));
        }
        this.taskValues = taskValues;
    }

    public Set<SchedulerTaskDevice> getSchedulerTaskDevices() {
        return this.schedulerTaskDevices;
    }

    public Tasks schedulerTaskDevices(Set<SchedulerTaskDevice> schedulerTaskDevices) {
        this.setSchedulerTaskDevices(schedulerTaskDevices);
        return this;
    }

    public Tasks addSchedulerTaskDevice(SchedulerTaskDevice schedulerTaskDevice) {
        this.schedulerTaskDevices.add(schedulerTaskDevice);
        schedulerTaskDevice.setTasks(this);
        return this;
    }

    public Tasks removeSchedulerTaskDevice(SchedulerTaskDevice schedulerTaskDevice) {
        this.schedulerTaskDevices.remove(schedulerTaskDevice);
        schedulerTaskDevice.setTasks(null);
        return this;
    }

    public void setSchedulerTaskDevices(Set<SchedulerTaskDevice> schedulerTaskDevices) {
        if (this.schedulerTaskDevices != null) {
            this.schedulerTaskDevices.forEach(i -> i.setTasks(null));
        }
        if (schedulerTaskDevices != null) {
            schedulerTaskDevices.forEach(i -> i.setTasks(this));
        }
        this.schedulerTaskDevices = schedulerTaskDevices;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tasks)) {
            return false;
        }
        return id != null && id.equals(((Tasks) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tasks{" +
            "id=" + getId() +
            ", taskName='" + getTaskName() + "'" +
            ", description='" + getDescription() + "'" +
            ", source='" + getSource() + "'" +
            ", price=" + getPrice() +
            ", social='" + getSocial() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
