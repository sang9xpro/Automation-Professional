package com.automation.professional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "country")
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @JsonIgnoreProperties(value = { "country", "deviceValues", "loggers", "accounts", "schedulerTaskDevices" }, allowSetters = true)
    @OneToOne(mappedBy = "country")
    private Devices devices;

    @JsonIgnoreProperties(value = { "country", "accountValues", "devices" }, allowSetters = true)
    @OneToOne(mappedBy = "country")
    private Accounts accounts;

    @JsonIgnoreProperties(value = { "country", "facebookValues", "mostOfContents" }, allowSetters = true)
    @OneToOne(mappedBy = "country")
    private Facebook facebook;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Country name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Country code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Devices getDevices() {
        return this.devices;
    }

    public Country devices(Devices devices) {
        this.setDevices(devices);
        return this;
    }

    public void setDevices(Devices devices) {
        if (this.devices != null) {
            this.devices.setCountry(null);
        }
        if (devices != null) {
            devices.setCountry(this);
        }
        this.devices = devices;
    }

    public Accounts getAccounts() {
        return this.accounts;
    }

    public Country accounts(Accounts accounts) {
        this.setAccounts(accounts);
        return this;
    }

    public void setAccounts(Accounts accounts) {
        if (this.accounts != null) {
            this.accounts.setCountry(null);
        }
        if (accounts != null) {
            accounts.setCountry(this);
        }
        this.accounts = accounts;
    }

    public Facebook getFacebook() {
        return this.facebook;
    }

    public Country facebook(Facebook facebook) {
        this.setFacebook(facebook);
        return this;
    }

    public void setFacebook(Facebook facebook) {
        if (this.facebook != null) {
            this.facebook.setCountry(null);
        }
        if (facebook != null) {
            facebook.setCountry(this);
        }
        this.facebook = facebook;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return id != null && id.equals(((Country) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
