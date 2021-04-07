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
 * A SchedulerFields.
 */
@Entity
@Table(name = "scheduler_fields")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "schedulerfields")
public class SchedulerFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_name")
    private String fieldName;

    @OneToMany(mappedBy = "schedulerFields")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "schedulers", "schedulerFields" }, allowSetters = true)
    private Set<SchedulerValue> schedulerValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchedulerFields id(Long id) {
        this.id = id;
        return this;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public SchedulerFields fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Set<SchedulerValue> getSchedulerValues() {
        return this.schedulerValues;
    }

    public SchedulerFields schedulerValues(Set<SchedulerValue> schedulerValues) {
        this.setSchedulerValues(schedulerValues);
        return this;
    }

    public SchedulerFields addSchedulerValue(SchedulerValue schedulerValue) {
        this.schedulerValues.add(schedulerValue);
        schedulerValue.setSchedulerFields(this);
        return this;
    }

    public SchedulerFields removeSchedulerValue(SchedulerValue schedulerValue) {
        this.schedulerValues.remove(schedulerValue);
        schedulerValue.setSchedulerFields(null);
        return this;
    }

    public void setSchedulerValues(Set<SchedulerValue> schedulerValues) {
        if (this.schedulerValues != null) {
            this.schedulerValues.forEach(i -> i.setSchedulerFields(null));
        }
        if (schedulerValues != null) {
            schedulerValues.forEach(i -> i.setSchedulerFields(this));
        }
        this.schedulerValues = schedulerValues;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulerFields)) {
            return false;
        }
        return id != null && id.equals(((SchedulerFields) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulerFields{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            "}";
    }
}
