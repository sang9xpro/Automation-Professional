package com.automation.professional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A SchedulerValue.
 */
@Entity
@Table(name = "scheduler_value")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "schedulervalue")
public class SchedulerValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "schedulerValues", "schedulerTaskDevices" }, allowSetters = true)
    private Schedulers schedulers;

    @ManyToOne
    @JsonIgnoreProperties(value = { "schedulerValues" }, allowSetters = true)
    private SchedulerFields schedulerFields;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchedulerValue id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public SchedulerValue value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Schedulers getSchedulers() {
        return this.schedulers;
    }

    public SchedulerValue schedulers(Schedulers schedulers) {
        this.setSchedulers(schedulers);
        return this;
    }

    public void setSchedulers(Schedulers schedulers) {
        this.schedulers = schedulers;
    }

    public SchedulerFields getSchedulerFields() {
        return this.schedulerFields;
    }

    public SchedulerValue schedulerFields(SchedulerFields schedulerFields) {
        this.setSchedulerFields(schedulerFields);
        return this;
    }

    public void setSchedulerFields(SchedulerFields schedulerFields) {
        this.schedulerFields = schedulerFields;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulerValue)) {
            return false;
        }
        return id != null && id.equals(((SchedulerValue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulerValue{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
