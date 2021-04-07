package com.automation.professional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A FacebookValues.
 */
@Entity
@Table(name = "facebook_values")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "facebookvalues")
public class FacebookValues implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "country", "facebookValues", "mostOfContents" }, allowSetters = true)
    private Facebook facebook;

    @ManyToOne
    @JsonIgnoreProperties(value = { "facebookValues" }, allowSetters = true)
    private FacebookFields facebookFields;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FacebookValues id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public FacebookValues value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Facebook getFacebook() {
        return this.facebook;
    }

    public FacebookValues facebook(Facebook facebook) {
        this.setFacebook(facebook);
        return this;
    }

    public void setFacebook(Facebook facebook) {
        this.facebook = facebook;
    }

    public FacebookFields getFacebookFields() {
        return this.facebookFields;
    }

    public FacebookValues facebookFields(FacebookFields facebookFields) {
        this.setFacebookFields(facebookFields);
        return this;
    }

    public void setFacebookFields(FacebookFields facebookFields) {
        this.facebookFields = facebookFields;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacebookValues)) {
            return false;
        }
        return id != null && id.equals(((FacebookValues) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacebookValues{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
