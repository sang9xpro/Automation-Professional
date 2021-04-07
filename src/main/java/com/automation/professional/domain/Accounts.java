package com.automation.professional.domain;

import com.automation.professional.domain.enumeration.Social;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Accounts.
 */
@Entity
@Table(name = "accounts")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accounts")
public class Accounts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Social type;

    @Column(name = "url_login")
    private String urlLogin;

    @Column(name = "profile_firefox")
    private String profileFirefox;

    @Column(name = "profile_chrome")
    private String profileChrome;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @Column(name = "owner")
    private String owner;

    @Min(value = 1)
    @Max(value = 1)
    @Column(name = "actived")
    private Integer actived;

    @JsonIgnoreProperties(value = { "devices", "accounts", "facebook" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Country country;

    @OneToMany(mappedBy = "accounts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "accounts", "accountFields" }, allowSetters = true)
    private Set<AccountValues> accountValues = new HashSet<>();

    @ManyToMany(mappedBy = "accounts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "country", "deviceValues", "loggers", "accounts", "schedulerTaskDevices" }, allowSetters = true)
    private Set<Devices> devices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Accounts id(Long id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return this.userName;
    }

    public Accounts userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public Accounts password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Social getType() {
        return this.type;
    }

    public Accounts type(Social type) {
        this.type = type;
        return this;
    }

    public void setType(Social type) {
        this.type = type;
    }

    public String getUrlLogin() {
        return this.urlLogin;
    }

    public Accounts urlLogin(String urlLogin) {
        this.urlLogin = urlLogin;
        return this;
    }

    public void setUrlLogin(String urlLogin) {
        this.urlLogin = urlLogin;
    }

    public String getProfileFirefox() {
        return this.profileFirefox;
    }

    public Accounts profileFirefox(String profileFirefox) {
        this.profileFirefox = profileFirefox;
        return this;
    }

    public void setProfileFirefox(String profileFirefox) {
        this.profileFirefox = profileFirefox;
    }

    public String getProfileChrome() {
        return this.profileChrome;
    }

    public Accounts profileChrome(String profileChrome) {
        this.profileChrome = profileChrome;
        return this;
    }

    public void setProfileChrome(String profileChrome) {
        this.profileChrome = profileChrome;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public Accounts lastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getOwner() {
        return this.owner;
    }

    public Accounts owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getActived() {
        return this.actived;
    }

    public Accounts actived(Integer actived) {
        this.actived = actived;
        return this;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
    }

    public Country getCountry() {
        return this.country;
    }

    public Accounts country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<AccountValues> getAccountValues() {
        return this.accountValues;
    }

    public Accounts accountValues(Set<AccountValues> accountValues) {
        this.setAccountValues(accountValues);
        return this;
    }

    public Accounts addAccountValues(AccountValues accountValues) {
        this.accountValues.add(accountValues);
        accountValues.setAccounts(this);
        return this;
    }

    public Accounts removeAccountValues(AccountValues accountValues) {
        this.accountValues.remove(accountValues);
        accountValues.setAccounts(null);
        return this;
    }

    public void setAccountValues(Set<AccountValues> accountValues) {
        if (this.accountValues != null) {
            this.accountValues.forEach(i -> i.setAccounts(null));
        }
        if (accountValues != null) {
            accountValues.forEach(i -> i.setAccounts(this));
        }
        this.accountValues = accountValues;
    }

    public Set<Devices> getDevices() {
        return this.devices;
    }

    public Accounts devices(Set<Devices> devices) {
        this.setDevices(devices);
        return this;
    }

    public Accounts addDevices(Devices devices) {
        this.devices.add(devices);
        devices.getAccounts().add(this);
        return this;
    }

    public Accounts removeDevices(Devices devices) {
        this.devices.remove(devices);
        devices.getAccounts().remove(this);
        return this;
    }

    public void setDevices(Set<Devices> devices) {
        if (this.devices != null) {
            this.devices.forEach(i -> i.removeAccounts(this));
        }
        if (devices != null) {
            devices.forEach(i -> i.addAccounts(this));
        }
        this.devices = devices;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Accounts)) {
            return false;
        }
        return id != null && id.equals(((Accounts) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Accounts{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", password='" + getPassword() + "'" +
            ", type='" + getType() + "'" +
            ", urlLogin='" + getUrlLogin() + "'" +
            ", profileFirefox='" + getProfileFirefox() + "'" +
            ", profileChrome='" + getProfileChrome() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", owner='" + getOwner() + "'" +
            ", actived=" + getActived() +
            "}";
    }
}
