package com.automation.professional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A AccountValues.
 */
@Entity
@Table(name = "account_values")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accountvalues")
public class AccountValues implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "country", "accountValues", "devices" }, allowSetters = true)
    private Accounts accounts;

    @ManyToOne
    @JsonIgnoreProperties(value = { "accountValues" }, allowSetters = true)
    private AccountFields accountFields;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountValues id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public AccountValues value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Accounts getAccounts() {
        return this.accounts;
    }

    public AccountValues accounts(Accounts accounts) {
        this.setAccounts(accounts);
        return this;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public AccountFields getAccountFields() {
        return this.accountFields;
    }

    public AccountValues accountFields(AccountFields accountFields) {
        this.setAccountFields(accountFields);
        return this;
    }

    public void setAccountFields(AccountFields accountFields) {
        this.accountFields = accountFields;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountValues)) {
            return false;
        }
        return id != null && id.equals(((AccountValues) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountValues{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
