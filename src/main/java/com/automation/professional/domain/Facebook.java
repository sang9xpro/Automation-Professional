package com.automation.professional.domain;

import com.automation.professional.domain.enumeration.FbType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Facebook.
 */
@Entity
@Table(name = "facebook")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "facebook")
public class Facebook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "id_on_fb")
    private String idOnFb;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FbType type;

    @JsonIgnoreProperties(value = { "devices", "accounts", "facebook" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Country country;

    @OneToMany(mappedBy = "facebook")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "facebook", "facebookFields" }, allowSetters = true)
    private Set<FacebookValues> facebookValues = new HashSet<>();

    @OneToMany(mappedBy = "facebook")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "mostOfContValues", "comments", "facebook" }, allowSetters = true)
    private Set<MostOfContent> mostOfContents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Facebook id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Facebook name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public Facebook url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdOnFb() {
        return this.idOnFb;
    }

    public Facebook idOnFb(String idOnFb) {
        this.idOnFb = idOnFb;
        return this;
    }

    public void setIdOnFb(String idOnFb) {
        this.idOnFb = idOnFb;
    }

    public FbType getType() {
        return this.type;
    }

    public Facebook type(FbType type) {
        this.type = type;
        return this;
    }

    public void setType(FbType type) {
        this.type = type;
    }

    public Country getCountry() {
        return this.country;
    }

    public Facebook country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<FacebookValues> getFacebookValues() {
        return this.facebookValues;
    }

    public Facebook facebookValues(Set<FacebookValues> facebookValues) {
        this.setFacebookValues(facebookValues);
        return this;
    }

    public Facebook addFacebookValues(FacebookValues facebookValues) {
        this.facebookValues.add(facebookValues);
        facebookValues.setFacebook(this);
        return this;
    }

    public Facebook removeFacebookValues(FacebookValues facebookValues) {
        this.facebookValues.remove(facebookValues);
        facebookValues.setFacebook(null);
        return this;
    }

    public void setFacebookValues(Set<FacebookValues> facebookValues) {
        if (this.facebookValues != null) {
            this.facebookValues.forEach(i -> i.setFacebook(null));
        }
        if (facebookValues != null) {
            facebookValues.forEach(i -> i.setFacebook(this));
        }
        this.facebookValues = facebookValues;
    }

    public Set<MostOfContent> getMostOfContents() {
        return this.mostOfContents;
    }

    public Facebook mostOfContents(Set<MostOfContent> mostOfContents) {
        this.setMostOfContents(mostOfContents);
        return this;
    }

    public Facebook addMostOfContent(MostOfContent mostOfContent) {
        this.mostOfContents.add(mostOfContent);
        mostOfContent.setFacebook(this);
        return this;
    }

    public Facebook removeMostOfContent(MostOfContent mostOfContent) {
        this.mostOfContents.remove(mostOfContent);
        mostOfContent.setFacebook(null);
        return this;
    }

    public void setMostOfContents(Set<MostOfContent> mostOfContents) {
        if (this.mostOfContents != null) {
            this.mostOfContents.forEach(i -> i.setFacebook(null));
        }
        if (mostOfContents != null) {
            mostOfContents.forEach(i -> i.setFacebook(this));
        }
        this.mostOfContents = mostOfContents;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facebook)) {
            return false;
        }
        return id != null && id.equals(((Facebook) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facebook{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            ", idOnFb='" + getIdOnFb() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
