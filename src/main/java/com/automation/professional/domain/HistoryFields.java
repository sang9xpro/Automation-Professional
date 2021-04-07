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
 * A HistoryFields.
 */
@Entity
@Table(name = "history_fields")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "historyfields")
public class HistoryFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_name")
    private String fieldName;

    @OneToMany(mappedBy = "historyFields")
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

    public HistoryFields id(Long id) {
        this.id = id;
        return this;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public HistoryFields fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Set<HistoryValues> getHistoryValues() {
        return this.historyValues;
    }

    public HistoryFields historyValues(Set<HistoryValues> historyValues) {
        this.setHistoryValues(historyValues);
        return this;
    }

    public HistoryFields addHistoryValues(HistoryValues historyValues) {
        this.historyValues.add(historyValues);
        historyValues.setHistoryFields(this);
        return this;
    }

    public HistoryFields removeHistoryValues(HistoryValues historyValues) {
        this.historyValues.remove(historyValues);
        historyValues.setHistoryFields(null);
        return this;
    }

    public void setHistoryValues(Set<HistoryValues> historyValues) {
        if (this.historyValues != null) {
            this.historyValues.forEach(i -> i.setHistoryFields(null));
        }
        if (historyValues != null) {
            historyValues.forEach(i -> i.setHistoryFields(this));
        }
        this.historyValues = historyValues;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoryFields)) {
            return false;
        }
        return id != null && id.equals(((HistoryFields) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoryFields{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            "}";
    }
}
