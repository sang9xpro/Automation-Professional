package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.FacebookValues} entity.
 */
public class FacebookValuesDTO implements Serializable {

    private Long id;

    private String value;

    private FacebookDTO facebook;

    private FacebookFieldsDTO facebookFields;

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

    public FacebookDTO getFacebook() {
        return facebook;
    }

    public void setFacebook(FacebookDTO facebook) {
        this.facebook = facebook;
    }

    public FacebookFieldsDTO getFacebookFields() {
        return facebookFields;
    }

    public void setFacebookFields(FacebookFieldsDTO facebookFields) {
        this.facebookFields = facebookFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacebookValuesDTO)) {
            return false;
        }

        FacebookValuesDTO facebookValuesDTO = (FacebookValuesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facebookValuesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacebookValuesDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", facebook=" + getFacebook() +
            ", facebookFields=" + getFacebookFields() +
            "}";
    }
}
