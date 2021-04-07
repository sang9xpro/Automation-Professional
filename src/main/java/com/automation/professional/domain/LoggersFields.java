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
 * A LoggersFields.
 */
@Entity
@Table(name = "loggers_fields")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "loggersfields")
public class LoggersFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_name")
    private String fieldName;

    @OneToMany(mappedBy = "loggersFields")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "loggers", "loggersFields" }, allowSetters = true)
    private Set<LoggersValues> loggersValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoggersFields id(Long id) {
        this.id = id;
        return this;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public LoggersFields fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Set<LoggersValues> getLoggersValues() {
        return this.loggersValues;
    }

    public LoggersFields loggersValues(Set<LoggersValues> loggersValues) {
        this.setLoggersValues(loggersValues);
        return this;
    }

    public LoggersFields addLoggersValues(LoggersValues loggersValues) {
        this.loggersValues.add(loggersValues);
        loggersValues.setLoggersFields(this);
        return this;
    }

    public LoggersFields removeLoggersValues(LoggersValues loggersValues) {
        this.loggersValues.remove(loggersValues);
        loggersValues.setLoggersFields(null);
        return this;
    }

    public void setLoggersValues(Set<LoggersValues> loggersValues) {
        if (this.loggersValues != null) {
            this.loggersValues.forEach(i -> i.setLoggersFields(null));
        }
        if (loggersValues != null) {
            loggersValues.forEach(i -> i.setLoggersFields(this));
        }
        this.loggersValues = loggersValues;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoggersFields)) {
            return false;
        }
        return id != null && id.equals(((LoggersFields) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoggersFields{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            "}";
    }
}
