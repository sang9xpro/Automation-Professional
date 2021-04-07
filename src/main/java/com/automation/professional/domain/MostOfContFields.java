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
 * A MostOfContFields.
 */
@Entity
@Table(name = "most_of_cont_fields")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "mostofcontfields")
public class MostOfContFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_name")
    private String fieldName;

    @OneToMany(mappedBy = "mostOfContFields")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "mostOfContent", "mostOfContFields" }, allowSetters = true)
    private Set<MostOfContValues> mostOfContValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MostOfContFields id(Long id) {
        this.id = id;
        return this;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public MostOfContFields fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Set<MostOfContValues> getMostOfContValues() {
        return this.mostOfContValues;
    }

    public MostOfContFields mostOfContValues(Set<MostOfContValues> mostOfContValues) {
        this.setMostOfContValues(mostOfContValues);
        return this;
    }

    public MostOfContFields addMostOfContValues(MostOfContValues mostOfContValues) {
        this.mostOfContValues.add(mostOfContValues);
        mostOfContValues.setMostOfContFields(this);
        return this;
    }

    public MostOfContFields removeMostOfContValues(MostOfContValues mostOfContValues) {
        this.mostOfContValues.remove(mostOfContValues);
        mostOfContValues.setMostOfContFields(null);
        return this;
    }

    public void setMostOfContValues(Set<MostOfContValues> mostOfContValues) {
        if (this.mostOfContValues != null) {
            this.mostOfContValues.forEach(i -> i.setMostOfContFields(null));
        }
        if (mostOfContValues != null) {
            mostOfContValues.forEach(i -> i.setMostOfContFields(this));
        }
        this.mostOfContValues = mostOfContValues;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MostOfContFields)) {
            return false;
        }
        return id != null && id.equals(((MostOfContFields) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MostOfContFields{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            "}";
    }
}
