package com.automation.professional.service.dto;

import com.automation.professional.domain.enumeration.FbType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.Facebook} entity.
 */
public class FacebookDTO implements Serializable {

    private Long id;

    private String name;

    private String url;

    private String idOnFb;

    private FbType type;

    private CountryDTO country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdOnFb() {
        return idOnFb;
    }

    public void setIdOnFb(String idOnFb) {
        this.idOnFb = idOnFb;
    }

    public FbType getType() {
        return type;
    }

    public void setType(FbType type) {
        this.type = type;
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
        if (!(o instanceof FacebookDTO)) {
            return false;
        }

        FacebookDTO facebookDTO = (FacebookDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facebookDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacebookDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            ", idOnFb='" + getIdOnFb() + "'" +
            ", type='" + getType() + "'" +
            ", country=" + getCountry() +
            "}";
    }
}
