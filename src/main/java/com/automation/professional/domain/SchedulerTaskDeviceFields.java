package com.automation.professional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A SchedulerTaskDeviceFields.
 */
@Entity
@Table(name = "scheduler_task_device_fields")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "schedulertaskdevicefields")
public class SchedulerTaskDeviceFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_name")
    private String fieldName;

    @OneToMany(mappedBy = "schedulerTaskDeviceFields")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schedulerTaskDevice", "schedulerTaskDeviceFields" }, allowSetters = true)
    private Set<SchedulerTaskDeviceValues> schedulerTaskDeviceValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchedulerTaskDeviceFields id(Long id) {
        this.id = id;
        return this;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public SchedulerTaskDeviceFields fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Set<SchedulerTaskDeviceValues> getSchedulerTaskDeviceValues() {
        return this.schedulerTaskDeviceValues;
    }

    public SchedulerTaskDeviceFields schedulerTaskDeviceValues(Set<SchedulerTaskDeviceValues> schedulerTaskDeviceValues) {
        this.setSchedulerTaskDeviceValues(schedulerTaskDeviceValues);
        return this;
    }

    public SchedulerTaskDeviceFields addSchedulerTaskDeviceValues(SchedulerTaskDeviceValues schedulerTaskDeviceValues) {
        this.schedulerTaskDeviceValues.add(schedulerTaskDeviceValues);
        schedulerTaskDeviceValues.setSchedulerTaskDeviceFields(this);
        return this;
    }

    public SchedulerTaskDeviceFields removeSchedulerTaskDeviceValues(SchedulerTaskDeviceValues schedulerTaskDeviceValues) {
        this.schedulerTaskDeviceValues.remove(schedulerTaskDeviceValues);
        schedulerTaskDeviceValues.setSchedulerTaskDeviceFields(null);
        return this;
    }

    public void setSchedulerTaskDeviceValues(Set<SchedulerTaskDeviceValues> schedulerTaskDeviceValues) {
        if (this.schedulerTaskDeviceValues != null) {
            this.schedulerTaskDeviceValues.forEach(i -> i.setSchedulerTaskDeviceFields(null));
        }
        if (schedulerTaskDeviceValues != null) {
            schedulerTaskDeviceValues.forEach(i -> i.setSchedulerTaskDeviceFields(this));
        }
        this.schedulerTaskDeviceValues = schedulerTaskDeviceValues;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulerTaskDeviceFields)) {
            return false;
        }
        return id != null && id.equals(((SchedulerTaskDeviceFields) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulerTaskDeviceFields{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            "}";
    }
}
