package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.AccountValues} entity.
 */
public class AccountValuesDTO implements Serializable {

    private Long id;

    private String value;

    private AccountsDTO accounts;

    private AccountFieldsDTO accountFields;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AccountsDTO getAccounts() {
        return accounts;
    }

    public void setAccounts(AccountsDTO accounts) {
        this.accounts = accounts;
    }

    public AccountFieldsDTO getAccountFields() {
        return accountFields;
    }

    public void setAccountFields(AccountFieldsDTO accountFields) {
        this.accountFields = accountFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountValuesDTO)) {
            return false;
        }

        AccountValuesDTO accountValuesDTO = (AccountValuesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountValuesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountValuesDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", accounts=" + getAccounts() +
            ", accountFields=" + getAccountFields() +
            "}";
    }
}
