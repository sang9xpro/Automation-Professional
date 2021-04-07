package com.automation.professional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A SchedulerTaskDeviceValues.
 */
@Entity
@Table(name = "scheduler_task_device_values")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "schedulertaskdevicevalues")
public class SchedulerTaskDeviceValues implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "schedulerTaskDeviceValues", "schedulers", "tasks", "devices" }, allowSetters = true)
    private SchedulerTaskDevice schedulerTaskDevice;

    @ManyToOne
    @JsonIgnoreProperties(value = { "schedulerTaskDeviceValues" }, allowSetters = true)
    private SchedulerTaskDeviceFields schedulerTaskDeviceFields;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchedulerTaskDeviceValues id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public SchedulerTaskDeviceValues value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SchedulerTaskDevice getSchedulerTaskDevice() {
        return this.schedulerTaskDevice;
    }

    public SchedulerTaskDeviceValues schedulerTaskDevice(SchedulerTaskDevice schedulerTaskDevice) {
        this.setSchedulerTaskDevice(schedulerTaskDevice);
        return this;
    }

    public void setSchedulerTaskDevice(SchedulerTaskDevice schedulerTaskDevice) {
        this.schedulerTaskDevice = schedulerTaskDevice;
    }

    public SchedulerTaskDeviceFields getSchedulerTaskDeviceFields() {
        return this.schedulerTaskDeviceFields;
    }

    public SchedulerTaskDeviceValues schedulerTaskDeviceFields(SchedulerTaskDeviceFields schedulerTaskDeviceFields) {
        this.setSchedulerTaskDeviceFields(schedulerTaskDeviceFields);
        return this;
    }

    public void setSchedulerTaskDeviceFields(SchedulerTaskDeviceFields schedulerTaskDeviceFields) {
        this.schedulerTaskDeviceFields = schedulerTaskDeviceFields;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulerTaskDeviceValues)) {
            return false;
        }
        return id != null && id.equals(((SchedulerTaskDeviceValues) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulerTaskDeviceValues{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
