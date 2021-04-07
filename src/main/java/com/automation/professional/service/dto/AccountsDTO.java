package com.automation.professional.service.dto;

import com.automation.professional.domain.enumeration.Social;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.automation.professional.domain.Accounts} entity.
 */
public class AccountsDTO implements Serializable {

    private Long id;

    private String userName;

    private String password;

    private Social type;

    private String urlLogin;

    private String profileFirefox;

    private String profileChrome;

    private Instant lastUpdate;

    private String owner;

    @Min(value = 1)
    @Max(value = 1)
    private Integer actived;

    private CountryDTO country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Social getType() {
        return type;
    }

    public void setType(Social type) {
        this.type = type;
    }

    public String getUrlLogin() {
        return urlLogin;
    }

    public void setUrlLogin(String urlLogin) {
        this.urlLogin = urlLogin;
    }

    public String getProfileFirefox() {
        return profileFirefox;
    }

    public void setProfileFirefox(String profileFirefox) {
        this.profileFirefox = profileFirefox;
    }

    public String getProfileChrome() {
        return profileChrome;
    }

    public void setProfileChrome(String profileChrome) {
        this.profileChrome = profileChrome;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getActived() {
        return actived;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountsDTO)) {
            return false;
        }

        AccountsDTO accountsDTO = (AccountsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountsDTO{" +
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
            ", country=" + getCountry() +
            "}";
    }
}
