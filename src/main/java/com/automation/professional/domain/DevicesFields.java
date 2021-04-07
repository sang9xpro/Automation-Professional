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
 * A DevicesFields.
 */
@Entity
@Table(name = "devices_fields")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "devicesfields")
public class DevicesFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fields_name")
    private String fieldsName;

    @OneToMany(mappedBy = "devicesFields")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "devices", "devicesFields" }, allowSetters = true)
    private Set<DeviceValues> deviceValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DevicesFields id(Long id) {
        this.id = id;
        return this;
    }

    public String getFieldsName() {
        return this.fieldsName;
    }

    public DevicesFields fieldsName(String fieldsName) {
        this.fieldsName = fieldsName;
        return this;
    }

    public void setFieldsName(String fieldsName) {
        this.fieldsName = fieldsName;
    }

    public Set<DeviceValues> getDeviceValues() {
        return this.deviceValues;
    }

    public DevicesFields deviceValues(Set<DeviceValues> deviceValues) {
        this.setDeviceValues(deviceValues);
        return this;
    }

    public DevicesFields addDeviceValues(DeviceValues deviceValues) {
        this.deviceValues.add(deviceValues);
        deviceValues.setDevicesFields(this);
        return this;
    }

    public DevicesFields removeDeviceValues(DeviceValues deviceValues) {
        this.deviceValues.remove(deviceValues);
        deviceValues.setDevicesFields(null);
        return this;
    }

    public void setDeviceValues(Set<DeviceValues> deviceValues) {
        if (this.deviceValues != null) {
            this.deviceValues.forEach(i -> i.setDevicesFields(null));
        }
        if (deviceValues != null) {
            deviceValues.forEach(i -> i.setDevicesFields(this));
        }
        this.deviceValues = deviceValues;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DevicesFields)) {
            return false;
        }
        return id != null && id.equals(((DevicesFields) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DevicesFields{" +
            "id=" + getId() +
            ", fieldsName='" + getFieldsName() + "'" +
            "}";
    }
}
