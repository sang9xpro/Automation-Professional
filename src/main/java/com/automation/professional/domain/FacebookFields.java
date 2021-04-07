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
 * A FacebookFields.
 */
@Entity
@Table(name = "facebook_fields")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "facebookfields")
public class FacebookFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_name")
    private String fieldName;

    @OneToMany(mappedBy = "facebookFields")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "facebook", "facebookFields" }, allowSetters = true)
    private Set<FacebookValues> facebookValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FacebookFields id(Long id) {
        this.id = id;
        return this;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public FacebookFields fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Set<FacebookValues> getFacebookValues() {
        return this.facebookValues;
    }

    public FacebookFields facebookValues(Set<FacebookValues> facebookValues) {
        this.setFacebookValues(facebookValues);
        return this;
    }

    public FacebookFields addFacebookValues(FacebookValues facebookValues) {
        this.facebookValues.add(facebookValues);
        facebookValues.setFacebookFields(this);
        return this;
    }

    public FacebookFields removeFacebookValues(FacebookValues facebookValues) {
        this.facebookValues.remove(facebookValues);
        facebookValues.setFacebookFields(null);
        return this;
    }

    public void setFacebookValues(Set<FacebookValues> facebookValues) {
        if (this.facebookValues != null) {
            this.facebookValues.forEach(i -> i.setFacebookFields(null));
        }
        if (facebookValues != null) {
            facebookValues.forEach(i -> i.setFacebookFields(this));
        }
        this.facebookValues = facebookValues;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacebookFields)) {
            return false;
        }
        return id != null && id.equals(((FacebookFields) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacebookFields{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            "}";
    }
}
